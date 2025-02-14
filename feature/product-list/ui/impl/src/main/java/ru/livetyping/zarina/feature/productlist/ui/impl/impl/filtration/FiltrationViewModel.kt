package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.usecase.product.GetCategoryInfoFlowUseCase
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicomponent.filtration.FiltrationComponent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.model.FiltrationState
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.model.FiltrationStateBuilder
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.model.TopBarEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.model.TopBarState
import javax.inject.Inject

@HiltViewModel
internal class FiltrationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    deps: FiltrationDependencies,
) : ViewModel(), SideEffectSource<FiltrationSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<FiltrationNavEntry>(
        typeMap = FiltrationNavEntry.typeMap(),
    )
    private val categoryId = navEntry.getCategoryId()
    private val initialFilters = navEntry.filters?.toFilters()

    private val filtrationComponent = FiltrationComponent(
        savedStateHandle = savedStateHandle,
        initialFilters = initialFilters,
        coroutineScope = viewModelScope,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryInfoRequester = FlowRequester(CategoryInfoRequester) {
        filtrationComponent.filters
            .onEach { filtrationComponent.setIsRefreshing(true) }
            .flatMapLatest { filters ->
                val params = GetCategoryInfoFlowUseCase.Params(categoryId, filters)
                deps.getCategoryInfoFlow(params)
            }
            .onEach { filtrationComponent.setIsRefreshing(false) }
    }

    private val categoryInfoResultFlow = categoryInfoRequester.flow
        .conflate()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    val topBarState: StateFlow<TopBarState> = filtrationComponent.isResetFiltersButtonVisible
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
        ) { isResetFiltersButtonVisible ->
            TopBarState(isResetFiltersButtonVisible)
        }

    private val filtrationStateBuilder = FiltrationStateBuilder()
    private val filtrationInitialState = filtrationStateBuilder.build(
        filters = filtrationComponent.filters.value,
        categoryInfoResult = null,
        isPickupStoreFilterVisible = filtrationComponent.isPickupStoreFilterVisible.value,
        isRefreshing = filtrationComponent.isRefreshing.value,
    )
    val filtrationState: StateFlow<FiltrationState> = combine(
        filtrationComponent.filters,
        categoryInfoResultFlow,
        filtrationComponent.isPickupStoreFilterVisible,
        filtrationComponent.isRefreshing,
    ) { filters, categoryInfoResult, isPickupStoreFilterVisible, isRefreshing ->
        filtrationStateBuilder.build(
            filters = filters,
            categoryInfoResult = categoryInfoResult,
            isPickupStoreFilterVisible = isPickupStoreFilterVisible,
            isRefreshing = isRefreshing,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = filtrationInitialState,
    )

    fun onTopBarEvent(event: TopBarEvent) {
        when (event) {
            TopBarEvent.BackClicked -> onBackClicked()
            TopBarEvent.ResetFiltersClicked -> onResetFiltersClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = FiltrationScreenAction.BackClicked
            emitSideEffect(FiltrationSideEffect.Navigate(action))
        }
    }

    private fun onResetFiltersClicked() {
        val filters = filtrationComponent.filters.value
        if (filters != null) {
            val newFilters = filters.reset()
            filtrationComponent.setFilters(newFilters)
        }
    }

    private data object CategoryInfoRequester : FlowRequest
}
