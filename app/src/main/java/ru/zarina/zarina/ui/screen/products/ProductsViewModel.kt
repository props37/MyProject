package ru.zarina.zarina.ui.screen.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.common.Sorting
import ru.zarina.zarina.domain.rework.filter.Filters
import ru.zarina.zarina.domain.rework.filter.combineWith
import ru.zarina.zarina.domain.rework.filter.selected
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.products.ProductsViewModel.SideEffect
import ru.zarina.zarina.usecase.rework.category.GetCategoryFlowUseCase
import ru.zarina.zarina.usecase.rework.product.GetProductPagingDataFlowUseCase
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

    private val categoryFetchRequests = MutableSharedFlow<Unit>(replay = 1)
        .also { it.tryEmit(Unit) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryResult: StateFlow<Result<Category>?> = combine(
        categoryId,
        categoryFetchRequests,
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

    private val filters = MutableStateFlow(
        Filters.create(
            sorting = Filters.getDefaultSorting(Sorting.getDefault()),
        )
    )

    private var availableFilters: Filters? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val productPagingDataFlow: Flow<PagingData<Product>> = combine(
        categoryId,
        filters,
    ) { categoryId, filters ->
        val sorting = filters.sorting?.selected ?: Sorting.getDefault()
        GetProductPagingDataFlowUseCase.Params(
            categoryId = categoryId,
            filters = filters,
            sorting = sorting,
            onAvailableFiltersReceived = { availableFilters = it },
        )
    }
        .flatMapLatest { params ->
            interactor.getProductPagingDataFlow(params)
        }
        .cachedIn(viewModelScope)

    init {
        handleFiltersResult(backStackEntrySavedStateHandle)
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.NavigateBackward)
        }
    }

    fun onSearchClicked() {
        // TODO: [High] Implement
    }

    fun onFiltersClicked() {
        navigationThrottler.throttle {
            val availableFilters = availableFilters
            val combinedFilters =
                availableFilters?.let { filters.value.combineWith(it) } ?: filters.value
            val action = ProductsScreenAction.FiltersClicked(
                categoryId = categoryId.value,
                filters = combinedFilters,
            )
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
        categoryFetchRequests.tryEmit(Unit)
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

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateForward(val action: ProductsScreenAction) : SideEffect
        data object NavigateBackward : SideEffect
    }

    @AssistedFactory
    interface Factory {
        fun create(backStackEntrySavedStateHandle: SavedStateHandle): ProductsViewModel
    }
}

