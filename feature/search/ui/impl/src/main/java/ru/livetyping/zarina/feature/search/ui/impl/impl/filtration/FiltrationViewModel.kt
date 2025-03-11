package ru.livetyping.zarina.feature.search.ui.impl.impl.filtration

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.usecase.search.SearchFlowUseCase
import ru.livetyping.zarina.core.navigationutil.ScreenResultHandler
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationEvent
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationState
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationTopBarEvent
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationTopBarState
import ru.livetyping.zarina.core.uicomponent.filtration.viewmodel.ProductFiltrationComponent
import ru.livetyping.zarina.feature.search.ui.impl.impl.filtration.model.FiltrationStateBuilder
import ru.livetyping.zarina.feature.search.ui.impl.impl.listfilter.ListFilterResult

@HiltViewModel(assistedFactory = FiltrationViewModel.Factory::class)
internal class FiltrationViewModel @AssistedInject constructor(
    @Assisted
    listFilterResultFlow: Flow<ListFilterResult?>,
    savedStateHandle: SavedStateHandle,
    private val deps: FiltrationDependencies,
) : ViewModel(), SideEffectSource<FiltrationSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val navEntry = savedStateHandle.toRoute<FiltrationNavEntry>(
        typeMap = FiltrationNavEntry.typeMap(),
    )
    private val searchQuery = navEntry.searchQuery
    private val initialFilters = navEntry.filters?.toProductFilters()

    private val filtrationComponent = ProductFiltrationComponent(
        savedStateHandle = savedStateHandle,
        initialFilters = initialFilters,
        coroutineScope = viewModelScope,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val searchResultRequester = FlowRequester(SearchResultRequester) {
        filtrationComponent.filters
            .onEach { filtrationComponent.setIsRefreshing(true) }
            .flatMapLatest { filters ->
                val params = SearchFlowUseCase.Params(
                    query = searchQuery,
                    sorting = ProductSorting.NEW,
                    filters = filters,
                    offset = 0,
                )
                deps.search(params)
            }
            .onEach { filtrationComponent.setIsRefreshing(false) }
    }

    val topBarState: StateFlow<ProductFiltrationTopBarState> = filtrationComponent.isResetFiltersButtonVisible
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
        ) { isResetFiltersButtonVisible ->
            ProductFiltrationTopBarState(isResetFiltersButtonVisible)
        }

    private val filtrationStateBuilder = FiltrationStateBuilder()
    private val initialFiltrationState = filtrationStateBuilder.build(
        filters = initialFilters,
        searchResult = null,
        isPickupStoreFilterVisible = filtrationComponent.isPickupStoreFilterVisible.value,
        isRefreshing = filtrationComponent.isRefreshing.value,
    )
    val filtrationState: StateFlow<ProductFiltrationState> = combine(
        filtrationComponent.filters,
        searchResultRequester.flow,
        filtrationComponent.isPickupStoreFilterVisible,
        filtrationComponent.isRefreshing,
    ) { filters, searchResult, isPickupStoreFilterVisible, isRefreshing ->
        filtrationStateBuilder.build(
            filters = filters,
            searchResult = searchResult,
            isPickupStoreFilterVisible = isPickupStoreFilterVisible,
            isRefreshing = isRefreshing,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = initialFiltrationState,
    )

    init {
        handleListFilterResult(listFilterResultFlow)
    }

    fun onTopBarEvent(event: ProductFiltrationTopBarEvent) {
        when (event) {
            ProductFiltrationTopBarEvent.BackClicked -> onBackClicked()
            ProductFiltrationTopBarEvent.ResetFiltersClicked -> onResetFiltersClicked()
        }
    }

    fun onFiltrationEvent(event: ProductFiltrationEvent) {
        when (event) {
            is ProductFiltrationEvent.FilterChanged -> onFilterChanged(event)
            is ProductFiltrationEvent.FilterClicked -> onFilterClicked(event)
            ProductFiltrationEvent.ShowProductsClicked -> onShowProductsClicked()
            ProductFiltrationEvent.ErrorRefreshClicked -> onErrorRefreshClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = FiltrationScreenAction.BackClicked
            emitSideEffect(FiltrationSideEffect.Navigate(action))
        }
    }

    private fun onResetFiltersClicked() {
        filtrationComponent.resetFilters()
    }

    private fun onFilterChanged(event: ProductFiltrationEvent.FilterChanged) {
        filtrationComponent.updateFiltersWith(event.filter)
    }

    private fun onFilterClicked(event: ProductFiltrationEvent.FilterClicked) {
        navigationThrottler.throttle {
            val action = FiltrationScreenAction.FilterClicked(event.filter)
            emitSideEffect(FiltrationSideEffect.Navigate(action))
        }
    }

    private fun onShowProductsClicked() {
        navigationThrottler.throttle {
            viewModelScope.launch {
                val filters = filtrationComponent.filters.firstOrNull()
                val action = if (filters != null) {
                    FiltrationScreenAction.ShowProductsClicked(filters)
                } else {
                    FiltrationScreenAction.BackClicked
                }
                emitSideEffect(FiltrationSideEffect.Navigate(action))
            }
        }
    }

    private fun onErrorRefreshClicked() {
        searchResultRequester.request(SearchResultRequester)
    }

    private fun handleListFilterResult(resultFlow: Flow<ListFilterResult?>) {
        viewModelScope.launch {
            screenResultHandler.handle(
                resultFlow = resultFlow,
                key = Keys.LIST_FILTER_RESULT.key,
            ) { result ->
                val filter = result.filter.toListFilter()
                filtrationComponent.updateFiltersWith(filter)
            }
        }
    }

    @AssistedFactory
    internal interface Factory {
        fun create(listFilterResultFlow: Flow<ListFilterResult?>): FiltrationViewModel
    }

    private data object SearchResultRequester : FlowRequest

    private enum class Keys {
        LIST_FILTER_RESULT;

        val key: String get() = name
    }
}
