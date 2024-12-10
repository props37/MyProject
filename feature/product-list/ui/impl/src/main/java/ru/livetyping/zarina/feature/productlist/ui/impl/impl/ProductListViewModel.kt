package ru.livetyping.zarina.feature.productlist.ui.impl.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.combine
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.product.filter.list.selected
import ru.livetyping.zarina.core.domain.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoryFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uikit.sizeselector.SizeSelectorEvent
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavEntry
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.ProductEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TagListEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TagListState
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TopBarEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TopBarState
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class ProductListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: ProductListDependencies,
) : ViewModel(), SideEffectSource<ProductListSideEffect> by SideEffectSourceImpl() {

    // TODO: [Top] Inject dispatcher
    private val viewModelScopeDefault = viewModelScope + Dispatchers.Default

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<ProductListNavEntry>(
        typeMap = ProductListNavEntry.typeMap(),
    )
    private val categoryId = Category.Id(navEntry.categoryId)
    private val initialFilters = navEntry.filters?.toFilters()

    private val categoryRequester = FlowRequester(CategoryRequest) {
        val params = GetCategoryFlowUseCase.Params(
            id = categoryId,
            cachePolicy = CachePolicy.LocalFirstThenRemote(),
        )
        deps.getCategoryFlow(params)
    }

    private val category = categoryRequester.flow
        .map { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val selectedTagId = MutableStateFlow<Category.Id?>(null)

    private val filtersValueHolder = savedStateHandle.createValueHolder<ProductFiltersParcelable?>(
        key = Keys.FILTERS.key,
        initialValue = null,
    )
    private val filters: StateFlow<ProductFilters> = filtersValueHolder.stateFlow.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
    ) {
        it?.toFilters() ?: run {
            initialFilters ?: ProductFilters.create(sorting = ProductFilters.getDefaultSorting())
        }
    }

    private var availableFilters: ProductFilters? = null

    val topBarState: StateFlow<TopBarState> = combine(
        category,
        filters,
    ) { category, filters ->
        TopBarState(
            categoryName = category?.name,
            appliedFilterCount = filters.appliedFilterCount,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = TopBarState(
            categoryName = null,
            appliedFilterCount = filters.value.appliedFilterCount,
        ),
    )

    val tagListState: StateFlow<TagListState> = combine(
        category,
        selectedTagId,
    ) { category, selectedTagId ->
        if (category != null) {
            val children = category.children
            if (!children.isNullOrEmpty()) {
                val tags = children.toImmutableList()
                TagListState.Success(tags, selectedTagId)
            } else {
                TagListState.Empty
            }
        } else {
            TagListState.Loading
        }
    }.stateIn(
        scope = viewModelScopeDefault,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = TagListState.Loading,
    )

    private val wishlistProductIdsParams =
        GetWishlistProductIdsFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())

    private val cartProductIdsParams =
        GetCartProductIdsFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())

    @OptIn(ExperimentalCoroutinesApi::class)
    val productPagingDataFlow: Flow<PagingData<ProductShort>> = combine(
        selectedTagId,
        filters,
    ) { selectedTagId, filters ->
        val sorting = filters.sorting?.selected ?: ProductSorting.getDefault()
        deps.productPager.getProductPagingDataFlow(
            categoryId = selectedTagId ?: categoryId,
            filters = filters,
            sorting = sorting,
            onAvailableFiltersReceived = { availableFilters = it },
        )
    }
        .flatMapLatest { it }
        .cachedIn(viewModelScopeDefault)
        .combine(
            deps.getWishlistProductIdsFlow(wishlistProductIdsParams),
            deps.getCartProductIdsFlow(cartProductIdsParams),
        ) { productPagingData, wishlistProductIdsResult, cartProductIdsResult ->
            val wishlistProductIds = wishlistProductIdsResult.getOrDefault(emptySet())
            val cartProductIds = cartProductIdsResult.getOrDefault(emptySet())
            productPagingData.map { product ->
                product.copy(
                    isInWishlist = product.id in wishlistProductIds,
                    isInCart = product.id in cartProductIds,
                )
            }
        }
        .cachedIn(viewModelScopeDefault)

    private val _visibleProductSizeSelector = MutableStateFlow<Product?>(null)
    val visibleProductSizeSelector: StateFlow<Product?> = _visibleProductSizeSelector.asStateFlow()

    val shouldSystemBackBeIntercepted: StateFlow<Boolean> = selectedTagId.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
    ) { selectedTagId ->
        selectedTagId != null
    }

    fun onTopBarEvent(event: TopBarEvent) {
        when (event) {
            TopBarEvent.BackClicked -> onBackClicked()
            TopBarEvent.SearchClicked -> TODO()
            TopBarEvent.FiltersClicked -> TODO()
        }
    }

    fun onTagListEvent(event: TagListEvent) {
        when (event) {
            is TagListEvent.TagClicked -> {
                val tag = event.tag
                if (tag.children.isNullOrEmpty()) {
                    selectedTagId.value = if (selectedTagId.value != tag.id) tag.id else null
                } else {
                    navigationThrottler.throttle {
                        val action = ProductListScreenAction.TagClicked(
                            tag = tag,
                            filters = filters.value,
                        )
                        emitSideEffect(ProductListSideEffect.Navigate(action))
                        selectedTagId.value = null
                    }
                }
            }
        }
    }

    fun onProductEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.ProductClicked -> TODO()
            is ProductEvent.AddToWishlistClicked -> onAddProductToWishlistClicked(event)
            is ProductEvent.AddToCartClicked -> onAddProductToCartClicked(event)
            is ProductEvent.SubscribeClicked -> onSubscribeToProductClicked(event)
            ProductEvent.ProductsRefreshed -> {
                if (category.value == null) requestCategory()
            }

            ProductEvent.ProductsErrorRefreshClicked -> {
                if (category.value == null) requestCategory()
            }
        }
    }

    fun onSizeSelectorEvent(event: SizeSelectorEvent) {
        when (event) {
            SizeSelectorEvent.DismissRequested -> _visibleProductSizeSelector.value = null
            is SizeSelectorEvent.SizeSelected -> {
                _visibleProductSizeSelector.value = null
                val product = event.product
                val offer = event.offer
                if (event.offer.isAvailable) {
                    addProductToCart(product, offer)
                } else {
                    val action = ProductListScreenAction.SubscribeToProductClicked(product, offer)
                    emitSideEffect(ProductListSideEffect.Navigate(action))
                }
            }
        }
    }

    fun onSystemBackClicked() {
        if (selectedTagId.value != null) {
            selectedTagId.value = null
        } else {
            onBackClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductListScreenAction.BackClicked
            emitSideEffect(ProductListSideEffect.Navigate(action))
        }
    }

    private fun onAddProductToWishlistClicked(event: ProductEvent.AddToWishlistClicked) {
        viewModelScope.launch {
            val product = event.product
            val params = ToggleProductInWishlistUseCase.Params(product.id)
            deps.toggleProductInWishlist(params)
                .onSuccess { isInWishlist ->
                    if (isInWishlist) {
                        val text = Text.Resource(RCommon.string.res_product_added_to_wishlist)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(ProductListSideEffect.ShowZarinaToast(message))
                    }
                }
                .onFailure {
                    val textResId = if (product.isInWishlist) {
                        RCommon.string.res_product_removing_from_wishlist_error
                    } else {
                        RCommon.string.res_product_adding_to_wishlist_error
                    }
                    val text = Text.Resource(textResId)
                    showZarinaErrorToast(text)
                }
        }
    }

    private fun onAddProductToCartClicked(event: ProductEvent.AddToCartClicked) {
        val product = event.product
        if (product.offers.size > 1) {
            _visibleProductSizeSelector.value = product
        } else {
            val offer = product.offers.firstOrNull() ?: return
            if (offer.isAvailable) {
                addProductToCart(product, offer)
            } else {
                val action = ProductListScreenAction.SubscribeToProductClicked(product, offer)
                emitSideEffect(ProductListSideEffect.Navigate(action))
            }
        }
    }

    private fun onSubscribeToProductClicked(event: ProductEvent.SubscribeClicked) {
        val product = event.product
        val offer = product.offers.firstOrNull() ?: return
        val action = ProductListScreenAction.SubscribeToProductClicked(product, offer)
        emitSideEffect(ProductListSideEffect.Navigate(action))
    }

    private fun addProductToCart(product: Product, offer: ProductOffer) {
        viewModelScope.launch {
            val params = AddProductToCartUseCase.Params(
                productId = product.id,
                barcode = offer.barcode,
                count = 1,
            )
            deps.addProductToCart(params)
                .onSuccess {
                    val text = Text.Resource(RCommon.string.res_product_added_to_cart)
                    val message = ZarinaToastMessage(text)
                    emitSideEffect(ProductListSideEffect.ShowZarinaToast(message))
                }
                .onFailure {
                    val text = Text.Resource(RCommon.string.res_product_adding_to_cart_error)
                    showZarinaErrorToast(text)
                }
        }
    }

    private fun requestCategory() {
        categoryRequester.request(CategoryRequest)
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(ProductListSideEffect.ShowZarinaToast(message))
    }

    private enum class Keys {
        FILTERS;

        val key: String get() = name
    }

    private data object CategoryRequest : FlowRequest
}
