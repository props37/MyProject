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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.common.Sorting
import ru.zarina.zarina.domain.rework.filter.Filters
import ru.zarina.zarina.domain.rework.filter.coerceInAvailable
import ru.zarina.zarina.domain.rework.filter.selected
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductOffer
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.common.paging.mapFavorites
import ru.zarina.zarina.ui.model.filter.FiltersParcelable
import ru.zarina.zarina.ui.navigation.rework.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.rework.destination.graph.SizeSelectorGraph
import ru.zarina.zarina.ui.screen.products.ProductsViewModel.SideEffect
import ru.zarina.zarina.usecase.rework.cart.AddProductToCartUseCase
import ru.zarina.zarina.usecase.rework.category.GetCategoryFlowUseCase
import ru.zarina.zarina.usecase.rework.favorite.AddProductToFavoritesUseCase
import ru.zarina.zarina.usecase.rework.favorite.RemoveProductFromFavoritesUseCase
import ru.zarina.zarina.usecase.rework.product.GetProductPagingDataFlowUseCase
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import ru.zarina.zarina.util.library.coroutines.mapState
import ru.zarina.zarina.utils.clean.invoke
import timber.log.Timber

@HiltViewModel(assistedFactory = ProductsViewModel.Factory::class)
class ProductsViewModel @AssistedInject constructor(
    @Assisted
    backStackEntrySavedStateHandle: SavedStateHandle,
    private val savedStateHandle: SavedStateHandle,
    private val interactor: ProductsInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

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

    private val filters = MutableStateFlow(
        initialFilters.value ?: Filters.create(
            sorting = Filters.getDefaultSorting(Sorting.getDefault()),
        )
    )

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
        .mapFavorites(interactor.getFavoriteProductIdsFlow())
        .cachedIn(viewModelScope)

    val appliedFilterCount: StateFlow<Int> = filters.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { it.appliedFilterCount }

    init {
        categoryFetchRequests.trySend(Unit)

        handleFiltersResult(backStackEntrySavedStateHandle)
        handleSizeSelectorResult(backStackEntrySavedStateHandle)
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
            val result = if (product.isInFavorites) {
                val params = RemoveProductFromFavoritesUseCase.Params(product.id)
                interactor.removeProductFromFavorites(params)
            } else {
                val params = AddProductToFavoritesUseCase.Params(product.id)
                interactor.addProductToFavorites(params)
            }
            result.onFailure {
                val messageResId = if (product.isInFavorites) {
                    R.string.removing_product_from_favorites_error_toast
                } else {
                    R.string.adding_product_to_favorites_error_toast
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
            addProductToCart(offer)
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

    private fun addProductToCart(offer: ProductOffer) {
        viewModelScope.launch {
            val params = AddProductToCartUseCase.Params(
                barcode = offer.barcode,
                count = 1,
            )
            interactor.addProductToCart(params)
                .onFailure {
                    val message = Text.Resource(R.string.adding_product_to_cart_error_toast)
                    emitSideEffect(SideEffect.ShowToast(message))
                }
        }
    }

    private fun handleFiltersResult(backStackEntrySavedStateHandle: SavedStateHandle) {
        backStackEntrySavedStateHandle.getStateFlow<UnscopedDestinations.Filters.Result?>(
            key = UnscopedDestinations.Filters.RESULT_KEY,
            initialValue = null,
        )
            .onEach { result ->
                if (result != null) {
                    Timber.v("Filters screen result: $result")
                    val filters = result.filters.toFilters()
                    this.filters.value = filters
                }
            }
            .launchIn(viewModelScope)
    }

    private fun handleSizeSelectorResult(backStackEntrySavedStateHandle: SavedStateHandle) {
        backStackEntrySavedStateHandle.getStateFlow<SizeSelectorGraph.Result?>(
            key = SizeSelectorGraph.RESULT_KEY,
            initialValue = null,
        )
            .onEach { result ->
                val previousSizeSelectorResult: String? =
                    savedStateHandle[KEY_PREV_SIZE_SELECTOR_RESULT]
                if (result != null && result.id != previousSizeSelectorResult) {
                    Timber.v("SizeSelector screen result: $result")
                    addProductToCart(result.offer.toProductOffer())
                    savedStateHandle[KEY_PREV_SIZE_SELECTOR_RESULT] = result.id
                }
            }
            .launchIn(viewModelScope)
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateForward(val action: ProductsScreenAction) : SideEffect

        data object NavigateBackward : SideEffect

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
        private const val KEY_PREV_SIZE_SELECTOR_RESULT = "prev_size_selector_result"
    }
}
