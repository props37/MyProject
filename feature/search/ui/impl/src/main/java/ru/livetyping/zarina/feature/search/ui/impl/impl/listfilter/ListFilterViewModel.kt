package ru.livetyping.zarina.feature.search.ui.impl.impl.listfilter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.nameResId
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model.ListFilterEvent
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model.ListFilterState
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model.ListFilterTopBarEvent
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model.ListFilterTopBarState
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.viewmodel.ListFilterComponent
import javax.inject.Inject

@HiltViewModel
internal class ListFilterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserCityFlow: GetUserCityFlowUseCase,
) : ViewModel(), SideEffectSource<ListFilterSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<ListFilterNavEntry>(
        typeMap = ListFilterNavEntry.typeMap(),
    )
    private val initialListFilter = navEntry.listFilter.toListFilter()

    private val listFilterComponent = ListFilterComponent(
        savedStateHandle = savedStateHandle,
        initialFilter = initialListFilter,
        coroutineScope = viewModelScope,
    )

    val topBarState: StateFlow<ListFilterTopBarState> = listFilterComponent.isResetFilterButtonVisible
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
        ) { isResetFilterButtonVisible ->
            ListFilterTopBarState(
                title = Text.Resource(initialListFilter.type.nameResId),
                isResetFilterButtonVisible = isResetFilterButtonVisible,
            )
        }

    private val userCityFlow = if (initialListFilter.type == ProductFilter.Type.PICKUP_STORES) {
        val userCityFlowParams = GetUserCityFlowUseCase.Params(CachePolicy.LocalOnly)
        getUserCityFlow(userCityFlowParams).map { it.getOrNull() }
    } else {
        flowOf(null)
    }

    val listFilterState: StateFlow<ListFilterState> = combine(
        listFilterComponent.filter,
        listFilterComponent.isApplyButtonVisible,
        userCityFlow,
    ) { filter, isApplyButtonVisible, city ->
        ListFilterState(
            filter = filter,
            isApplyButtonVisible = isApplyButtonVisible,
            cityHeader = city,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = ListFilterState(
            filter = initialListFilter,
            isApplyButtonVisible = false,
            cityHeader = null,
        ),
    )

    fun onTopBarEvent(event: ListFilterTopBarEvent) {
        when (event) {
            ListFilterTopBarEvent.BackClicked -> onBackClicked()
            ListFilterTopBarEvent.ResetFilterClicked -> onResetFilterClicked()
        }
    }

    fun onListFilterEvent(event: ListFilterEvent) {
        when (event) {
            ListFilterEvent.ApplyClicked -> onApplyClicked()
            is ListFilterEvent.ItemClicked -> onItemClicked(event)
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ListFilterScreenAction.BackClicked
            emitSideEffect(ListFilterSideEffect.Navigate(action))
        }
    }

    private fun onResetFilterClicked() {
        if (initialListFilter.type != ProductFilter.Type.SORTING) {
            listFilterComponent.resetFilter()
            listFilterComponent.setIsApplyButtonVisible(true)
        }
    }

    private fun onApplyClicked() {
        navigationThrottler.throttle {
            viewModelScope.launch {
                val filter = listFilterComponent.filter.firstOrNull() ?: return@launch
                val action = ListFilterScreenAction.AppliedClicked(filter)
                emitSideEffect(ListFilterSideEffect.Navigate(action))
            }
        }
    }

    private fun onItemClicked(event: ListFilterEvent.ItemClicked) {
        listFilterComponent.toggleItem(event.item)

        if (initialListFilter.type != ProductFilter.Type.SORTING) {
            listFilterComponent.setIsApplyButtonVisible(true)
        } else {
            onApplyClicked()
        }
    }
}
