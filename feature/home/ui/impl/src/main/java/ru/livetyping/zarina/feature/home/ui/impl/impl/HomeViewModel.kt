package ru.livetyping.zarina.feature.home.ui.impl.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.coroutinesutil.WhileUiSubscribed
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicomponent.gender.GenderPickerComponent
import ru.livetyping.zarina.feature.home.ui.impl.impl.component.HomeContentComponent
import ru.livetyping.zarina.feature.home.ui.impl.impl.model.HomeContentState
import ru.livetyping.zarina.feature.home.ui.impl.impl.model.HomeEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.model.HomeState
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val deps: HomeDependencies,
) : ViewModel(), SideEffectSource<HomeSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val genderPickerComponent = GenderPickerComponent(viewModelScope)
    private val homeContentComponent = HomeContentComponent(deps.getHomeContent)

    private val homeStateBuilder = HomeState.Builder()
    val homeState: StateFlow<HomeState> = combine(
        genderPickerComponent.genderPickerState,
        homeContentComponent.contentResult,
        homeContentComponent.isContentLoading,
        homeContentComponent.isContentRefreshing,
    ) { genderPickerState, contentResult, isContentLoading, isContentRefreshing ->
        homeStateBuilder.build(
            genderPickerState = genderPickerState,
            homeContentResult = contentResult,
            isContentLoading = isContentLoading,
            isContentRefreshing = isContentRefreshing,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = HomeState(HomeContentState.Loading),
    )

    init {
        fetchHomeContent(HomeContentComponent.ContentRequest.LOADING)
    }

    fun onHomeEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.GenderSelected -> genderPickerComponent.onGenderSelected(event.tab)
            is HomeEvent.BannerClicked -> onBannerClicked(event)
            HomeEvent.PullRefreshTriggered -> {
                fetchHomeContent(HomeContentComponent.ContentRequest.REFRESHING)
            }

            HomeEvent.RefreshClicked -> {
                fetchHomeContent(HomeContentComponent.ContentRequest.LOADING)
            }
        }
    }

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> deps.appMetrica.reportScreenOpened(Screen.Home)
            LifecycleEvent.ON_START -> Unit
            LifecycleEvent.ON_RESUME -> Unit
        }
    }

    private fun onBannerClicked(event: HomeEvent.BannerClicked) {
        navigationThrottler.throttle {
            val action = HomeScreenAction.BannerClicked(event.banner)
            emitSideEffect(HomeSideEffect.Navigate(action))
        }
    }

    private fun fetchHomeContent(request: HomeContentComponent.ContentRequest) {
        viewModelScope.launch {
            homeContentComponent.fetchContent(request)
        }
    }
}
