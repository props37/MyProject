package ru.livetyping.zarina.presentation.screen.productsearch.filters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.filter.Filter
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.filter.ListFilter
import ru.livetyping.zarina.domain.filter.coerceInAvailable
import ru.livetyping.zarina.domain.filter.reset
import ru.livetyping.zarina.domain.filter.updateWith
import ru.livetyping.zarina.domain.productsearch.ProductSearchResult
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.filter.FiltersParcelable
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.filters.FilterListState
import ru.livetyping.zarina.presentation.screen.productsearch.filters.ProductSearchFiltersViewModel.SideEffect
import ru.livetyping.zarina.usecase.productsearch.SearchProductsFlowUseCase
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import timber.log.Timber

// TODO: [High] DRY!

@HiltViewModel(assistedFactory = ProductSearchFiltersViewModel.Factory::class)
class ProductSearchFiltersViewModel @AssistedInject constructor(
    @Assisted
    private val listFilterResultFlow: StateFlow<UnscopedDestinations.ListFilter.Result?>,
    private val savedStateHandle: SavedStateHandle,
    private val interactor: ProductSearchFiltersInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val searchQuery: StateFlow<String> = savedStateHandle
        .getStateFlow<String?>(
            key = UnscopedDestinations.ProductSearchFilters.ARG_KEY_SEARCH_QUERY,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { value ->
            checkNotNull(value) { "searchQuery is null" }
        }

    private val initialFilters: StateFlow<Filters?> = savedStateHandle
        .getStateFlow<FiltersParcelable?>(
            key = UnscopedDestinations.ProductSearchFilters.ARG_KEY_FILTERS,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { it?.toFilters() }

    private val filters: StateFlow<Filters?> = savedStateHandle
        .getStateFlow<FiltersParcelable?>(
            key = KEY_FILTERS,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            parcelable?.toFilters() ?: initialFilters.value
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryProductInfoRequester = FlowRequester(CategoryProductInfoRequest.GENERAL) {
        combine(searchQuery, filters) { query, filters ->
            val params = SearchProductsFlowUseCase.Params(
                query = query,
                sorting = Sorting.NEW,
                filters = filters,
                offset = 0,
            )
            interactor.searchProductsFlow(params)
        }
            .flatMapLatest { it }
    }

    private val productSearchResult: StateFlow<Result<ProductSearchResult>?> =
        categoryProductInfoRequester.flow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    val filterListState: StateFlow<FilterListState> = combine(
        filters,
        productSearchResult,
    ) { filters, productSearchResult ->
        if (filters != null) {
            val availableFilters = productSearchResult?.getOrNull()?.availableFilters
            val combinedFilters = availableFilters?.let { filters.coerceInAvailable(it) } ?: filters
            FilterListState.FilterList(combinedFilters)
        } else {
            productSearchResult?.fold(
                onSuccess = { info ->
                    FilterListState.FilterList(info.availableFilters)
                },
                onFailure = { throwable ->
                    val errorState = ErrorState.from(throwable)
                    FilterListState.Error(errorState)
                },
            ) ?: FilterListState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = filters.value?.let { filters ->
            FilterListState.FilterList(filters)
        } ?: FilterListState.Loading,
    )

    val isPickupStoresFilterVisible: StateFlow<Boolean> = filters.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { filters ->
        filters?.storePickupAvailability?.isEnabled == true
    }

    val isResetFiltersButtonVisible: StateFlow<Boolean> = filters.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { it?.hasAppliedIgnoringSorting == true }

    init {
        handleListFilterResult()
    }

    val productCount: StateFlow<Int?> = productSearchResult
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
        ) { it?.getOrNull()?.productTotalCount }

    fun onBackClicked() {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.NavigateBackward(ProductSearchFiltersScreenResult.ScreenClosed))
        }
    }

    fun onResetFiltersClicked() {
        val filters = filters.value
        if (filters != null) {
            val newFilters = filters.reset()
            savedStateHandle[KEY_FILTERS] = FiltersParcelable.from(newFilters)
        } else {
            Timber.w("Could not reset filters since it is null")
        }
    }

    fun onFilterChanged(filter: Filter) {
        val newFilters = filters.value?.updateWith(filter)
        savedStateHandle[KEY_FILTERS] = newFilters?.let { FiltersParcelable.from(it) }
    }

    fun onFilterClicked(filter: Filter) {
        if (filter is ListFilter<*>) {
            navigationThrottler.throttle {
                val action = ProductSearchFiltersScreenAction.ListFilterClicked(filter)
                emitSideEffect(SideEffect.NavigateForward(action))
            }
        }
    }

    fun onShowProductsClicked() {
        navigationThrottler.throttle {
            val filters = filters.value
            val result = if (filters != null) {
                ProductSearchFiltersScreenResult.FiltersChanged(filters)
            } else {
                ProductSearchFiltersScreenResult.ScreenClosed
            }
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    fun onFilterListErrorRefreshClicked() {
        categoryProductInfoRequester.request(CategoryProductInfoRequest.GENERAL)
    }

    private fun handleListFilterResult() {
        viewModelScope.launch {
            screenResultHandler.handle<UnscopedDestinations.ListFilter.Result>(
                resultFlow = listFilterResultFlow,
                key = KEY_RESULT_LIST_FILTER,
            ) { result ->
                val filter = result.filter.toListFilter()
                val newFilters = filters.value?.updateWith(filter)
                savedStateHandle[KEY_FILTERS] = newFilters?.let { FiltersParcelable.from(it) }
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateForward(val action: ProductSearchFiltersScreenAction) : SideEffect

        data class NavigateBackward(val result: ProductSearchFiltersScreenResult) : SideEffect
    }

    private enum class CategoryProductInfoRequest : FlowRequester.Request { GENERAL }

    @AssistedFactory
    interface Factory {
        fun create(
            listFilterResultFlow: StateFlow<UnscopedDestinations.ListFilter.Result?>,
        ): ProductSearchFiltersViewModel
    }

    companion object {
        private const val KEY_FILTERS = "filters"
        private const val KEY_RESULT_LIST_FILTER = "result_list_filter"
    }
}
