package ru.livetyping.zarina.presentation.screen.products

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.filter.coerceInAvailable
import ru.livetyping.zarina.domain.filter.selected
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.util.library.paging.mapProducts
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.model.filter.FiltersParcelable
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.SizeSelectorGraph
import ru.livetyping.zarina.presentation.screen.products.ProductsViewModel.SideEffect
import ru.livetyping.zarina.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.usecase.category.GetCategoryFlowUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import timber.log.Timber

@HiltViewModel(assistedFactory = ProductsViewModel.Factory::class)
class ProductsViewModel @AssistedInject constructor(
    @Assisted
    backStackEntrySavedStateHandle: SavedStateHandle,
    private val savedStateHandle: SavedStateHandle,
    private val interactor: ProductsInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val viewModelScopeDefault = viewModelScope + Dispatchers.Default

    private val screenResultHandler = ScreenResultHandler(
        backStackEntrySavedStateHandle = backStackEntrySavedStateHandle,
        savedStateHandle = savedStateHandle,
    )

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val categoryId: StateFlow<Category.Id> = savedStateHandle
        .getStateFlow<Long?>(
            key = UnscopedDestinations.Products.ARG_KEY_CATEGORY_ID,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { value ->
            checkNotNull(value) { "categoryId is null" }
            Category.Id(value)
        }

    private val categoryFetchRequests = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryResult: StateFlow<Result<Category>?> = combine(
        categoryId,
        categoryFetchRequests.receiveAsFlow(),
    ) { id, _ ->
        val params = GetCategoryFlowUseCase.Params(id)
        interactor.getCategoryFlow(params)
    }
        .flatMapLatest { it }
        .stateIn(
            scope = viewModelScopeDefault,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val category: StateFlow<Category?> = categoryResult
        .map { it?.getOrNull() }
        .stateIn(
            scope = viewModelScopeDefault,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    val tagListState: StateFlow<TagListState?> = categoryResult.mapState(
        scope = viewModelScopeDefault,
        started = SharingStarted.WhileUiSubscribed,
    ) { result ->
        if (result != null) {
            result.fold(
                onSuccess = { category ->
                    if (!category.children.isNullOrEmpty()) {
                        val tags = category.children.toImmutableList()
                        TagListState.TagList(tags)
                    } else {
                        null
                    }
                },
                onFailure = { TagListState.Loading },
            )
        } else {
            TagListState.Loading
        }
    }

    private val _selectedTagId = MutableStateFlow<Category.Id?>(null)
    val selectedTagId = _selectedTagId.asStateFlow()

    private val initialFilters: StateFlow<Filters?> = savedStateHandle
        .getStateFlow<FiltersParcelable?>(
            key = UnscopedDestinations.Products.ARG_KEY_FILTERS,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            parcelable?.toFilters()
        }

    private val filters: StateFlow<Filters> = savedStateHandle
        .getStateFlow<FiltersParcelable?>(
            key = KEY_FILTERS,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            val fallbackFilters = initialFilters.value ?: Filters.create(
                sorting = Filters.getDefaultSorting(Sorting.getDefault()),
            )
            it?.toFilters() ?: fallbackFilters
        }

    private var availableFilters: Filters? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val productPagingDataFlow: Flow<PagingData<ProductItem>> = combine(
        categoryId,
        selectedTagId,
        filters,
    ) { categoryId, selectedTagId, filters ->
        val sorting = filters.sorting?.selected ?: Sorting.getDefault()
        interactor.productPager.getProductPagingDataFlow(
            categoryId = selectedTagId ?: categoryId,
            filters = filters,
            sorting = sorting,
            onAvailableFiltersReceived = { availableFilters = it },
        )
    }
        .flatMapLatest { it }
        .cachedIn(viewModelScopeDefault)
        .mapProducts(
            favoriteProductIdsResultFlow = interactor.getFavoriteProductIdsFlow(),
            cartProductIdsResultFlow = interactor.getCartProductIdsFlow(),
        )
        .cachedIn(viewModelScopeDefault)

    val appliedFilterCount: StateFlow<Int> = filters.mapState(
        scope = viewModelScopeDefault,
        started = SharingStarted.WhileUiSubscribed,
    ) { it.appliedFilterCount }

    init {
        categoryFetchRequests.trySend(Unit)

        handleFiltersResult()
        handleSizeSelectorResult()
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductsScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onSystemBackClicked() {
        if (selectedTagId.value != null) {
            _selectedTagId.value = null
        } else {
            onBackClicked()
        }
    }

    fun onSearchClicked() {
        navigationThrottler.throttle {
            val action = ProductsScreenAction.SearchClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onFiltersClicked() {
        navigationThrottler.throttle {
            val availableFilters = availableFilters
            val combinedFilters =
                availableFilters?.let { filters.value.coerceInAvailable(it) } ?: filters.value
            val action = ProductsScreenAction.FiltersClicked(
                categoryId = categoryId.value,
                filters = combinedFilters,
            )
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onTagClicked(tag: Category) {
        if (tag.children.isNullOrEmpty()) {
            _selectedTagId.value = if (selectedTagId.value != tag.id) tag.id else null
        } else {
            navigationThrottler.throttle {
                val action = ProductsScreenAction.TagClicked(tag = tag, filters = filters.value)
                emitSideEffect(SideEffect.Navigate(action))
                _selectedTagId.value = null
            }
        }
    }

    fun onProductClicked(product: Product) {
        navigationThrottler.throttle {
            val action = ProductsScreenAction.ProductClicked(product)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onAddProductToFavoritesClicked(product: Product) {
        viewModelScopeDefault.launch {
            val params = ToggleProductPresenceInFavoritesUseCase.Params(product.id)
            interactor.toggleProductPresenceInFavorites(params)
                .onSuccess { isProductInFavorites ->
                    if (isProductInFavorites) {
                        val text = Text.Resource(R.string.product_adding_to_favorites_completed)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(SideEffect.ShowZarinaToast(message))
                    }
                }
                .onFailure {
                    val messageResId = if (product.isInFavorites) {
                        R.string.product_removing_from_favorites_error
                    } else {
                        R.string.product_adding_to_favorites_error
                    }
                    val text = Text.Resource(messageResId)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    fun onAddProductToCartClicked(product: Product) {
        if (product.offers.size > 1) {
            navigationThrottler.throttle {
                val action = ProductsScreenAction.AddProductToCartClicked(product)
                emitSideEffect(SideEffect.Navigate(action))
            }
        } else {
            val offer = product.offers.firstOrNull() ?: run {
                Timber.e("Could not add product $product to cart because it has no offers")
                return
            }
            addProductToCart(product.id, offer.barcode)
        }
    }

    fun onSubscribeToProductClicked(product: Product) {
        navigationThrottler.throttle {
            val action = ProductsScreenAction.SubscribeToProductClicked(product)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onRefreshProducts() {
        if (category.value == null) {
            fetchCategory()
        }
    }

    fun onProductsErrorRefreshClicked() {
        if (category.value == null) {
            fetchCategory()
        }
    }

    private fun fetchCategory() {
        categoryFetchRequests.trySend(Unit)
    }

    private fun addProductToCart(productId: Product.Id, barcode: Barcode) {
        viewModelScopeDefault.launch {
            val params = AddProductToCartUseCase.Params(
                productId = productId,
                barcode = barcode,
                count = 1,
            )
            interactor.addProductToCart(params)
                .onSuccess {
                    val text = Text.Resource(R.string.product_adding_to_cart_completed)
                    val message = ZarinaToastMessage(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
                .onFailure {
                    val text = Text.Resource(R.string.product_adding_to_cart_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    private fun handleFiltersResult() {
        viewModelScope.launch {
            screenResultHandler.handle<UnscopedDestinations.Filters.Result>(
                key = UnscopedDestinations.Filters.RESULT_KEY,
            ) { result ->
                val filters = result.filters.toFilters()
                val filtersParcelable = FiltersParcelable.from(filters)
                savedStateHandle[KEY_FILTERS] = filtersParcelable
            }
        }
    }

    private fun handleSizeSelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<SizeSelectorGraph.Result>(
                key = SizeSelectorGraph.RESULT_KEY,
            ) { result ->
                addProductToCart(
                    productId = result.product.toProductItem().id,
                    barcode = result.offer.toProductOffer().barcode,
                )
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProductsScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    @Stable
    sealed class TagListState {
        data object Loading : TagListState()

        @Immutable
        data class TagList(val tags: ImmutableList<Category>) : TagListState()
    }

    @AssistedFactory
    interface Factory {
        fun create(backStackEntrySavedStateHandle: SavedStateHandle): ProductsViewModel
    }

    companion object {
        private const val KEY_FILTERS = "filters"
    }
}
