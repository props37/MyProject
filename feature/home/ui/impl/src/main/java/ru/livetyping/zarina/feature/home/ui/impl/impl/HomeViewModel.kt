package ru.livetyping.zarina.feature.home.ui.impl.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutines.util.FlowRequest
import ru.livetyping.zarina.core.coroutines.util.FlowRequester
import ru.livetyping.zarina.core.coroutines.util.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutines.util.mapState
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.domain.usecase.GetHomeContentFlowUseCase
import ru.livetyping.zarina.feature.home.ui.impl.impl.gender.GenderSelectorEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.gender.GenderSelectorState
import ru.livetyping.zarina.feature.home.ui.impl.impl.gender.GenderTab
import ru.livetyping.zarina.feature.home.ui.impl.impl.homecontent.HomeContentEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.homecontent.HomeContentState
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getHomeContentFlow: GetHomeContentFlowUseCase,
) : ViewModel() {

    private val currentGender = MutableStateFlow(getCurrentGenderInitialValue())

    val genderSelectorState: StateFlow<GenderSelectorState> = currentGender.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
    ) { currentGender ->
        GenderSelectorState(
            genders = GenderTab.getTabs().toImmutableList(),
            currentGender = currentGender,
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

    val isRefreshing: StateFlow<Boolean> = homeContentRequester.loadingState.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
    ) { loadingState ->
        loadingState.isLoading() && loadingState.request == HomeContentRequest.REFRESHING
    }

    fun onGenderSelectorEvent(event: GenderSelectorEvent) {
        when (event) {
            is GenderSelectorEvent.GenderChanged -> {
                currentGender.value = event.gender
                // TODO: [Top] Save gender as last user's selected gender
            }
        }
    }

    fun onHomeContentEvent(event: HomeContentEvent) {
        when (event) {
            is HomeContentEvent.BannerClicked -> {
                // TODO: [Top] Perform navigation
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
        // TODO: [Top] Return last user's selected gender
        return GenderTab.WOMEN
    }

    private fun createHomeContentState(
        loadingState: FlowRequester.LoadingState,
        result: Result<HomeContent>?,
    ): HomeContentState {
        val isLoading =
            loadingState.isLoading() && loadingState.request == HomeContentRequest.LOADING
        return if (isLoading || result == null) {
            HomeContentState.Loading
        } else {
            result.fold(
                onSuccess = { homeContent ->
                    HomeContentState.Success(homeContent)
                },
                onFailure = { t ->
                    // TODO: [Top] Implement
                    TODO()
                },
            )
        }
    }

    private enum class HomeContentRequest : FlowRequest { LOADING, REFRESHING }
}
