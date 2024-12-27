package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.usecase.location.GetCurrentLocationFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.store.GetStoresFlowUseCase
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListViewMode
import javax.inject.Inject

@HiltViewModel
internal class StoreListViewModel @Inject constructor(
    getStoresFlow: GetStoresFlowUseCase,
    getCurrentLocationFlow: GetCurrentLocationFlowUseCase,
) : ViewModel(), SideEffectSource<StoreListSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val viewModes = StoreListViewMode.getAll().toImmutableList()
    private val currentViewMode = MutableStateFlow(StoreListViewMode.MAP)

    val viewModeSelectorState: StateFlow<TabRowState<StoreListViewMode>> = currentViewMode.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
    ) { currentViewMode ->
        TabRowState(
            tabs = viewModes,
            currentTab = currentViewMode,
        )
    }

    private val storeRequester = FlowRequester(StoreRequest) {
        val params = GetStoresFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())
        getStoresFlow(params)
    }

    private val currentLocationRequester = FlowRequester(LocationRequest) {
        getCurrentLocationFlow()
    }

    private val currentLocation = currentLocationRequester.flow
        .map { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    fun onStoreListEvent(event: StoreListEvent) {
        when (event) {
            StoreListEvent.BackClicked -> onBackClicked()
        }
    }

    fun onViewModeSelectorEvent(event: TabRowEvent<StoreListViewMode>) {
        when (event) {
            is TabRowEvent.TabChanged -> currentViewMode.value = event.tab
            is TabRowEvent.TabReselected -> Unit
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = StoreListScreenAction.BackClicked
            emitSideEffect(StoreListSideEffect.Navigate(action))
        }
    }

    private data object StoreRequest : FlowRequest

    private data object LocationRequest : FlowRequest
}
