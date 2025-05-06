package ru.livetyping.zarina.feature.home.ui.impl.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.ui.impl.impl.model.HomeContentEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.model.HomeContentState
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val deps: HomeDependencies,
) : ViewModel(), SideEffectSource<HomeSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var homeContentJob: Job? = null

    private val genders = GenderTab.getTabs().toImmutableList()
    private val currentGender = MutableStateFlow(GenderTab.WOMEN)

    val genderSelectorState: StateFlow<TabRowState<GenderTab>> = currentGender.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
    ) { currentGender ->
        TabRowState(
            tabs = genders,
            currentTab = currentGender,
        )
    }

    private val homeContentResult = MutableStateFlow<Result<HomeContent>?>(null)

    private val homeContentStateBuilder = HomeContentState.Builder()
    val homeContentState: StateFlow<HomeContentState> = combine(
        homeContentResult,
        operationTracker.ongoingOperationKeys,
    ) { result, ongoingOperations ->
        homeContentStateBuilder.build(
            result = result,
            isLoading = HomeContentRequest.LOADING in ongoingOperations,
            isRefreshing = HomeContentRequest.REFRESHING in ongoingOperations,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // TODO: [Low] Migrate to extension
        initialValue = HomeContentState.Loading,
    )

    init {
        fetchHomeContent(HomeContentRequest.LOADING)
    }

    fun onGenderSelectorEvent(event: TabRowEvent<GenderTab>) {
        when (event) {
            is TabRowEvent.TabChanged -> currentGender.value = event.tab
            is TabRowEvent.TabReselected -> Unit
        }
    }

    fun onHomeContentEvent(event: HomeContentEvent) {
        when (event) {
            is HomeContentEvent.BannerClicked -> {
                navigationThrottler.throttle {
                    val action = HomeScreenAction.BannerClicked(event.banner)
                    emitSideEffect(HomeSideEffect.Navigate(action))
                }
            }

            HomeContentEvent.RefreshTriggered -> fetchHomeContent(HomeContentRequest.REFRESHING)
            HomeContentEvent.ErrorRefreshClicked -> fetchHomeContent(HomeContentRequest.LOADING)
        }
    }

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> deps.appMetrica.reportScreenOpened(Screen.Home)
            LifecycleEvent.ON_START -> Unit
            LifecycleEvent.ON_RESUME -> Unit
        }
    }

    private fun fetchHomeContent(request: HomeContentRequest) {
        if (homeContentJob?.isActive == true) return

        homeContentJob = viewModelScope.launch {
            operationTracker.track(request) {
                homeContentResult.value = deps.getHomeContent()
            }
        }
    }

    private enum class HomeContentRequest : OperationKey { LOADING, REFRESHING }
}
