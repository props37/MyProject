package ru.livetyping.zarina.feature.home.ui.impl.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.usecase.gender.GetLastContentGenderFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.gender.SetLastContentGenderUseCase
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.domain.usecase.GetHomeContentFlowUseCase
import ru.livetyping.zarina.feature.home.ui.impl.impl.model.HomeContentEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.model.HomeContentState
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getLastContentGenderFlow: GetLastContentGenderFlowUseCase,
    private val setLastContentGender: SetLastContentGenderUseCase,
    private val getHomeContentFlow: GetHomeContentFlowUseCase,
) : ViewModel(), SideEffectSource<HomeSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val currentGender = MutableStateFlow(getCurrentGenderInitialValue())

    val genderSelectorState: StateFlow<TabRowState<GenderTab>> = currentGender.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
    ) { currentGender ->
        TabRowState(
            tabs = GenderTab.getTabs().toImmutableList(),
            currentTab = currentGender,
        )
    }

    private val homeContentRequester = FlowRequester(HomeContentRequest.LOADING) {
        getHomeContentFlow()
    }

    private val homeContentResult: StateFlow<Result<HomeContent>?> = homeContentRequester.flow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
            initialValue = null,
        )

    val homeContentState: StateFlow<HomeContentState> = combine(
        homeContentRequester.loadingState,
        homeContentResult,
    ) { loadingState, result ->
        createHomeContentState(loadingState, result)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = HomeContentState.Loading,
    )

    fun onGenderSelectorEvent(event: TabRowEvent<GenderTab>) {
        when (event) {
            is TabRowEvent.TabChanged -> {
                val genderTab = event.tab
                currentGender.value = genderTab
                viewModelScope.launch {
                    val params = SetLastContentGenderUseCase.Params(genderTab.toGender())
                    setLastContentGender(params)
                }
            }

            // TODO: [Low] Implement
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

            HomeContentEvent.RefreshTriggered -> {
                homeContentRequester.request(HomeContentRequest.REFRESHING)
            }

            HomeContentEvent.ErrorRefreshClicked -> {
                homeContentRequester.request(HomeContentRequest.LOADING)
            }
        }
    }

    private fun getCurrentGenderInitialValue(): GenderTab {
        return runBlocking {
            val genderResult = getLastContentGenderFlow().firstOrNull()
            val gender = genderResult?.getOrNull() ?: Gender.getDefault()
            GenderTab.from(gender)
        }
    }

    private fun createHomeContentState(
        loadingState: FlowRequester.LoadingState,
        result: Result<HomeContent>?,
    ): HomeContentState {
        val isLoading =
            loadingState.isLoading() && loadingState.loadingRequest == HomeContentRequest.LOADING
        return if (isLoading || result == null) {
            HomeContentState.Loading
        } else {
            result.fold(
                onSuccess = { homeContent ->
                    val isRefreshing = loadingState.isLoading()
                            && loadingState.loadingRequest == HomeContentRequest.REFRESHING
                    HomeContentState.Success(homeContent, isRefreshing)
                },
                onFailure = { t ->
                    val errorState = ZarinaErrorScreenState.from(t)
                    HomeContentState.Error(errorState)
                },
            )
        }
    }

    private enum class HomeContentRequest : FlowRequest { LOADING, REFRESHING }
}
