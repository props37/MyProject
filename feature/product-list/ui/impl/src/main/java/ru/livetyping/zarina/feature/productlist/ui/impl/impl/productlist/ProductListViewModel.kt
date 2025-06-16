package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.coroutinesutil.WhileUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.combine
import ru.livetyping.zarina.core.domain.analytics.toAppMetricaCategory
import ru.livetyping.zarina.core.domain.analytics.toAppMetricaCategoryPath
import ru.livetyping.zarina.core.domain.analytics.toAppMetricaFilters
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.category.CategoryPath
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.product.filter.list.selected
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoryPathUseCase
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoryUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.core.navigationutil.ScreenResultHandler
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage2
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridSideEffect
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavEntry
import ru.livetyping.zarina.feature.productlist.ui.impl.R
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.FiltrationResult
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.component.CategoryComponent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.component.FilterComponent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model.ProductListEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model.ProductListState
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model.SubcategoryListState
import java.io.IOException
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel(assistedFactory = ProductListViewModel.Factory::class)
internal class ProductListViewModel @AssistedInject constructor(
    @Assisted
    filtrationResultFlow: Flow<FiltrationResult?>,
    savedStateHandle: SavedStateHandle,
    private val deps: ProductListDependencies,
) : ViewModel(), SideEffectSource<ProductListSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var reportScreenCreatedJob: Job? = null

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val categoryComponent = CategoryComponent(
        getCategoryUseCase = deps.getCategory,
    )
    private val filterComponent = FilterComponent(savedStateHandle, viewModelScope)

    private val navEntry = savedStateHandle.toRoute<ProductListFeature.NavEntry>(
        typeMap = ProductListNavEntry.typeMap(),
    )

    init {
        categoryComponent.setInitialCategoryId(navEntry.getCategoryId())
        fetchCategory()
        filterComponent.initialFilters = navEntry.filters?.toProductFilters()
    }

    private val _productGridSideEffects = Channel<ProductGridSideEffect>(Channel.UNLIMITED)
    val productGridSideEffects: Flow<ProductGridSideEffect> = _productGridSideEffects.receiveAsFlow()

    private val subcategoryListStateBuilder = SubcategoryListState.Builder()
    private val subcategoryListState = combine(
        categoryComponent.categoryResult,
        categoryComponent.selectedSubcategoryId,
    ) { categoryResult, selectedSubcategoryId ->
        subcategoryListStateBuilder.build(categoryResult, selectedSubcategoryId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SubcategoryListState.Loading,
    )

    private val wishlistProductIdsParams =
        GetWishlistProductIdsFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())

    private val cartProductIdsParams =
        GetCartProductIdsFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val productPagingDataFlow: Flow<PagingData<ProductShort>> = combine(
        categoryComponent.currentCategoryId,
        filterComponent.currentFilters,
    ) { currentCategoryId, filters ->
        val sorting = filters.sorting?.selected ?: ProductSorting.getDefault()
        deps.productPager.getProductPagingDataFlow(
            categoryId = currentCategoryId,
            filters = filters,
            sorting = sorting,
            onAvailableFiltersReceived = { filterComponent.availableFilters = it },
        )
    }
        .flatMapLatest { it }
        .cachedIn(viewModelScope)
        .onEach {
            val se = ProductGridSideEffect.ScrollToTop(animate = false)
            _productGridSideEffects.trySend(se)
        }
        .transformProductPagingData()
        .cachedIn(viewModelScope)

    private val isProductEndlessLoadingEnabled = MutableStateFlow(false)

    private val interceptSystemBack = categoryComponent.selectedSubcategoryId
        .map { selectedSubcategoryId ->
            selectedSubcategoryId != null
        }

    private val initialProductListState = ProductListState(
        categoryName = null,
        subcategoryListState = SubcategoryListState.Loading,
        appliedFilterCount = 0,
        productPagingDataFlow = productPagingDataFlow,
        isLoadMoreProductsButtonVisible = !isProductEndlessLoadingEnabled.value,
        isProductEndlessLoadingEnabled = isProductEndlessLoadingEnabled.value,
        interceptSystemBack = false,
    )

    val productListState: StateFlow<ProductListState> = combine(
        categoryComponent.categoryResult,
        subcategoryListState,
        filterComponent.currentFilters,
        isProductEndlessLoadingEnabled,
        interceptSystemBack,
    ) { categoryResult, subcategoryListState, appliedFilters, isProductEndlessLoadingEnabled, interceptSystemBack ->
        ProductListState(
            categoryName = categoryResult?.getOrNull()?.name,
            subcategoryListState = subcategoryListState,
            appliedFilterCount = appliedFilters.appliedFilterCount,
            productPagingDataFlow = productPagingDataFlow,
            isLoadMoreProductsButtonVisible = !isProductEndlessLoadingEnabled,
            isProductEndlessLoadingEnabled = isProductEndlessLoadingEnabled,
            interceptSystemBack = interceptSystemBack,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = initialProductListState,
    )

    init {
        handleFiltrationResult(filtrationResultFlow)
    }

    fun onProductListEvent(event: ProductListEvent) {
        when (event) {
            ProductListEvent.BackClicked -> onBackClicked()
            ProductListEvent.SearchClicked -> onSearchClicked()
            ProductListEvent.FiltersClicked -> onFiltersClicked()
            ProductListEvent.SeeAllProductsInCategoryClicked -> onSeeAllProductsInCategoryClicked()
            is ProductListEvent.SubcategoryClicked -> onSubcategoryClicked(event)
            is ProductListEvent.ProductClicked -> onProductClicked(event)
            is ProductListEvent.AddToWishlistClicked -> onAddToWishlistClicked(event)
            ProductListEvent.LoadMoreProductsClicked -> isProductEndlessLoadingEnabled.value = true
            ProductListEvent.PullRefreshTriggered -> onRefresh()
            is ProductListEvent.ProductAppendError -> onProductsPaginationError()
            is ProductListEvent.ProductPrependError -> onProductsPaginationError()
            ProductListEvent.RefreshClicked -> onRefresh()
            is ProductListEvent.CategoryShortcutClicked -> onCategoryShortcutClicked(event)
            ProductListEvent.SystemBackClicked -> onSystemBackClicked()
        }
    }

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> reportScreenCreated()
            LifecycleEvent.ON_START -> Unit
            LifecycleEvent.ON_RESUME -> Unit
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductListScreenAction.BackClicked
            emitSideEffect(ProductListSideEffect.Navigate(action))
        }
    }

    private fun onSearchClicked() {
        navigationThrottler.throttle {
            val action = ProductListScreenAction.SearchClicked
            emitSideEffect(ProductListSideEffect.Navigate(action))
        }
    }

    private fun onFiltersClicked() {
        navigationThrottler.throttle {
            val filters = filterComponent.getCurrentFilters()
            val availableFilters = filterComponent.availableFilters
            val combinedFilters = availableFilters
                ?.let { filters?.coerceInAvailable(availableFilters) }
                ?: filters
            val action = ProductListScreenAction.FiltersClicked(
                categoryId = categoryComponent.requireInitialCategoryId(),
                filters = combinedFilters,
            )
            emitSideEffect(ProductListSideEffect.Navigate(action))
        }
    }

    private fun onSeeAllProductsInCategoryClicked() {
        if (categoryComponent.selectedSubcategoryId.value != null) {
            categoryComponent.setSelectedSubcategoryId(null)
            reportScreenCreated()
        }
    }

    private fun onSubcategoryClicked(event: ProductListEvent.SubcategoryClicked) {
        val category = event.category
        if (category.children.isNullOrEmpty()) {
            val newSelectedSubcategoryId =
                if (categoryComponent.selectedSubcategoryId.value != category.id) {
                    category.id
                } else {
                    null
                }
            categoryComponent.setSelectedSubcategoryId(newSelectedSubcategoryId)
            reportScreenCreated()
        } else {
            navigationThrottler.throttle {
                val filters = filterComponent.getCurrentFilters()
                val action = ProductListScreenAction.SubcategoryClicked(category, filters)
                emitSideEffect(ProductListSideEffect.Navigate(action))
                categoryComponent.setSelectedSubcategoryId(null)
            }
        }
    }

    private fun onProductClicked(event: ProductListEvent.ProductClicked) {
        navigationThrottler.throttle {
            val action = ProductListScreenAction.ProductClicked(event.product)
            emitSideEffect(ProductListSideEffect.Navigate(action))
        }
    }

    private fun onAddToWishlistClicked(event: ProductListEvent.AddToWishlistClicked) {
        viewModelScope.launch {
            val params = ToggleProductInWishlistUseCase.Params.Product(event.product)
            deps.toggleProductInWishlist(params)
                .onSuccess { isInWishlist ->
                    if (isInWishlist) {
                        val message = ZarinaToastMessage2.productAddedToWishlist(event.product)
                        emitSideEffect(ProductListSideEffect.ShowZarinaToast(message))
                    }
                }
                .onFailure(::onToggleProductInWishlistFailure)
        }
    }

    private fun onRefresh() {
        if (!categoryComponent.isCategoryFetched()) {
            fetchCategory()
        }
    }

    private fun onProductsPaginationError() {
        val message = ZarinaToastMessage2(
            text = Text.Resource(R.string.product_list_product_pagination_error),
            startContent = ZarinaToastMessage2.StartContent.Icon.genericError(),
        )
        emitSideEffect(ProductListSideEffect.ShowZarinaToast(message))
    }

    private fun onCategoryShortcutClicked(event: ProductListEvent.CategoryShortcutClicked) {
        navigationThrottler.throttle {
            val action = ProductListScreenAction.CategoryShortcutClicked(event.categoryId)
            emitSideEffect(ProductListSideEffect.Navigate(action))
        }
    }

    private fun onSystemBackClicked() {
        if (categoryComponent.selectedSubcategoryId.value != null) {
            categoryComponent.setSelectedSubcategoryId(null)
        } else {
            onBackClicked()
        }
    }

    private fun fetchCategory() {
        viewModelScope.launch {
            categoryComponent.fetchCategory()
        }
    }

    private fun reportScreenCreated() {
        reportScreenCreatedJob?.cancel()
        reportScreenCreatedJob = viewModelScope.launch {
            val currentCategoryPath = getCurrentCategoryPath()
            val appMetricaScreen =
                Screen.ProductList(currentCategoryPath?.toAppMetricaCategoryPath())
            deps.appMetrica.reportScreenOpened(appMetricaScreen)
        }
    }

    private suspend fun getCurrentCategoryPath(): CategoryPath? {
        val currentCategoryId = categoryComponent.getCurrentCategoryId()
        val params = GetCategoryPathUseCase.Params(currentCategoryId, CachePolicy.LocalOnly)
        return deps.getCategoryPath(params).getOrNull()
    }

    private suspend fun reportFiltersApplied(filters: ProductFilters) {
        val currentCategoryId = categoryComponent.getCurrentCategoryId()
        val category = getCategory(currentCategoryId)
        if (category != null) {
            deps.appMetrica.reportProductFiltersApplied(
                category = category.toAppMetricaCategory(),
                appliedFilters = filters.toAppMetricaFilters(),
            )
        }
    }

    private suspend fun getCategory(categoryId: Category.Id): Category? {
        val params = GetCategoryUseCase.Params(categoryId, CachePolicy.LocalOnly)
        return deps.getCategory(params).getOrNull()
    }

    private fun onToggleProductInWishlistFailure(t: Throwable) {
        val message = when (t) {
            is IOException -> ZarinaToastMessage2.networkError()
            else -> {
                ZarinaToastMessage2(
                    text = Text.Resource(RCommon.string.res_product_adding_to_wishlist_error),
                    startContent = ZarinaToastMessage2.StartContent.Icon.genericError(),
                )
            }
        }
        emitSideEffect(ProductListSideEffect.ShowZarinaToast(message))
    }

    private fun handleFiltrationResult(resultFlow: Flow<FiltrationResult?>) {
        viewModelScope.launch {
            screenResultHandler.handle(
                resultFlow = resultFlow,
                key = Keys.FILTRATION_RESULT.key,
            ) { result ->
                val filters = result.filters.toProductFilters()
                filterComponent.setCurrentFilters(filters)
                reportFiltersApplied(filters)
            }
        }
    }

    // TODO: [High] Extract?
    private fun Flow<PagingData<ProductShort>>.transformProductPagingData(): Flow<PagingData<ProductShort>> {
        return this.combine(
            deps.getWishlistProductIdsFlow(wishlistProductIdsParams),
            deps.getCartProductIdsFlow(cartProductIdsParams),
        ) { productPagingData, wishlistProductIdsResult, cartProductIdsResult ->
            val wishlistProductIds = wishlistProductIdsResult.getOrDefault(emptySet())
            val cartProductIds = cartProductIdsResult.getOrDefault(emptySet())
            val productIdSet = HashSet<Product.Id>()

            productPagingData
                .filter { product ->
                    productIdSet.add(product.id)
                }
                .map { product ->
                    product.copy(
                        isInWishlist = product.id in wishlistProductIds,
                        isInCart = product.id in cartProductIds,
                    )
                }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(filtrationResultFlow: Flow<FiltrationResult?>): ProductListViewModel
    }

    private enum class Keys {
        FILTRATION_RESULT;

        val key: String get() = name
    }
}
