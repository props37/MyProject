package ru.zarina.zarina.ui.screen.products

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
import ru.zarina.zarina.R
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.base.throttler.Throttler
import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.common.Sorting
import ru.zarina.zarina.domain.filter.Filters
import ru.zarina.zarina.domain.filter.coerceInAvailable
import ru.zarina.zarina.domain.filter.selected
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.ui.base.text.Text
import ru.zarina.zarina.ui.common.util.ScreenResultHandler
import ru.zarina.zarina.ui.common.util.getNavigationThrottler
import ru.zarina.zarina.ui.common.util.library.paging.mapProducts
import ru.zarina.zarina.ui.common.zarinatoast.ZarinaMessage
import ru.zarina.zarina.ui.model.filter.FiltersParcelable
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.destination.graph.SizeSelectorGraph
import ru.zarina.zarina.ui.screen.products.ProductsViewModel.SideEffect
import ru.zarina.zarina.usecase.cart.AddProductToCartUseCase
import ru.zarina.zarina.usecase.category.GetCategoryFlowUseCase
import ru.zarina.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.zarina.zarina.usecase.product.GetProductPagingDataFlowUseCase
import ru.zarina.zarina.util.base.usecase.invoke
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import ru.zarina.zarina.util.library.coroutines.mapState
import timber.log.Timber

@HiltViewModel(assistedFactory = ProductsViewModel.Factory::class)
class ProductsViewModel @AssistedInject constructor(
    @Assisted
    backStackEntrySavedStateHandle: SavedStateHandle,
    private val savedStateHandle: SavedStateHandle,
    private val interactor: ProductsInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

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
        GetCategoryFlowUseCase.Params(id)
    }
        .flatMapLatest { params ->
            interactor.getCategoryFlow(params)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val category: StateFlow<Category?> = categoryResult
        .map { it?.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    val tagListState: StateFlow<TagListState?> = categoryResult.mapState(
        scope = viewModelScope,
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
    val productPagingDataFlow: Flow<PagingData<Product>> = combine(
        categoryId,
        selectedTagId,
        filters,
    ) { categoryId, selectedTagId, filters ->
        val sorting = filters.sorting?.selected ?: Sorting.getDefault()
        GetProductPagingDataFlowUseCase.Params(
            categoryId = selectedTagId ?: categoryId,
            filters = filters,
            sorting = sorting,
            onAvailableFiltersReceived = { availableFilters = it },
        )
    }
        .flatMapLatest { params ->
            interactor.getProductPagingDataFlow(params)
        }
        .cachedIn(viewModelScope)
        .mapProducts(
            favoriteProductIdsResultFlow =  interactor.getFavoriteProductIdsFlow(),
            cartProductIdsResultFlow = interactor.getCartProductIdsFlow(),
        )
        .cachedIn(viewModelScope)

    val appliedFilterCount: StateFlow<Int> = filters.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { it.appliedFilterCount }

    init {
        categoryFetchRequests.trySend(Unit)

        handleFiltersResult()
        handleSizeSelectorResult()
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.NavigateBackward)
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
        // TODO: [High] Implement
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
            emitSideEffect(SideEffect.NavigateForward(action))
        }
    }

    fun onTagClicked(tag: Category) {
        if (tag.children.isNullOrEmpty()) {
            _selectedTagId.value = if (selectedTagId.value != tag.id) tag.id else null
        } else {
            navigationThrottler.throttle {
                val action = ProductsScreenAction.TagClicked(tag = tag, filters = filters.value)
                emitSideEffect(SideEffect.NavigateForward(action))
                _selectedTagId.value = null
            }
        }
    }

    fun onProductClicked(product: Product) {
        // TODO: [High] Implement
    }

    fun onAddProductToFavoritesClicked(product: Product) {
        viewModelScope.launch {
            val params = ToggleProductPresenceInFavoritesUseCase.Params(product.id)
            interactor.toggleProductPresenceInFavorites(params)
                .onFailure {
                    val messageResId = if (product.isInFavorites) {
                        R.string.product_removing_from_favorites_error
                    } else {
                        R.string.product_adding_to_favorites_error
                    }
                    val message = Text.Resource(messageResId)
                    emitSideEffect(SideEffect.ShowToast(message))
                }
        }
    }

    fun onAddProductToCartClicked(product: Product) {
        if (product.offers.size > 1) {
            navigationThrottler.throttle {
                val action = ProductsScreenAction.AddProductToCartClicked(product)
                emitSideEffect(SideEffect.NavigateForward(action))
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
            emitSideEffect(SideEffect.NavigateForward(action))
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
        viewModelScope.launch {
            val params = AddProductToCartUseCase.Params(
                productId = productId,
                barcode = barcode,
                count = 1,
            )
            interactor.addProductToCart(params)
                .onSuccess {
                    val messageText = Text.Resource(R.string.product_adding_to_cart_completed)
                    val message = ZarinaMessage(messageText)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
                .onFailure {
                    val message = Text.Resource(R.string.product_adding_to_cart_error)
                    emitSideEffect(SideEffect.ShowToast(message))
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
                    productId = result.product.toProduct().id,
                    barcode = result.offer.toProductOffer().barcode,
                )
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateForward(val action: ProductsScreenAction) : SideEffect

        data object NavigateBackward : SideEffect

        data class ShowZarinaToast(val message: ZarinaMessage) : SideEffect

        data class ShowToast(val message: Text) : SideEffect
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
