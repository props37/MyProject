package ru.livetyping.zarina.presentation.screen.home

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.data.analytics.AppMetricaHelper
import ru.livetyping.zarina.data.analytics.AppMetricaScreen
import ru.livetyping.zarina.domain.common.Gender
import ru.livetyping.zarina.domain.content.HomeContent
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.usecase.user.SetUserContentGenderUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.ImmutableStateFlow
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: HomeInteractor,
) : ViewModel(), SideEffectSource<HomeViewModel.SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val genderTabs: StateFlow<ImmutableList<GenderTab>> =
        ImmutableStateFlow(GenderTab.entries.toImmutableList())

    val currentGenderTab: StateFlow<GenderTab> = savedStateHandle.getStateFlow(
        key = KEY_CURRENT_GENDER_TAB,
        initialValue = runBlocking {
            val gender = interactor.getUserContentGenderFlow()
                .firstOrNull()
                ?.getOrNull()
                ?: Gender.getDefault()
            GenderTab.from(gender)
        },
    )

    private val contentRequester = FlowRequester(ContentRequest.LOADING) {
        interactor.getHomeContentFlow()
    }

    private val contentResult: StateFlow<Result<HomeContent>?> = contentRequester.flow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val contentState: StateFlow<ContentState> = combine(
        contentRequester.loadingState,
        contentResult,
    ) { contentLoadingState, contentResult ->
        val isLoading = contentLoadingState is FlowRequester.LoadingState.Loading
                && contentLoadingState.request == ContentRequest.LOADING
        if (isLoading || contentResult == null) {
            ContentState.Loading
        } else {
            contentResult.fold(
                onSuccess = { content ->
                    ContentState.Success(content)
                },
                onFailure = { throwable ->
                    val errorState = ErrorState.from(throwable)
                    ContentState.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = ContentState.Loading,
    )

    val isRefreshing: StateFlow<Boolean> = contentRequester.loadingState.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) {
        it is FlowRequester.LoadingState.Loading && it.request == ContentRequest.REFRESHING
    }

    fun onScreenCreated() {
        reportScreenCreated()
    }

    fun onGenderTabChanged(tab: GenderTab) {
        savedStateHandle[KEY_CURRENT_GENDER_TAB] = tab
        viewModelScope.launch {
            val params = SetUserContentGenderUseCase.Params(tab.toGender())
            interactor.setUserContentGender(params)
        }
    }

    fun onBannerClicked(banner: HomeContent.Banner) {
        navigationThrottler.throttle {
            val action = HomeScreenAction.BannerClicked(banner)
            emitSideEffect(SideEffect.NavigateForward(action))
        }
    }

    fun onRefreshTriggered() {
        requestContent(ContentRequest.REFRESHING)
    }

    fun onContentErrorRefreshClicked() {
        requestContent(ContentRequest.LOADING)
    }

    private fun reportScreenCreated() {
        AppMetricaHelper.reportScreenOpened(AppMetricaScreen.Home)
    }

    private fun requestContent(request: ContentRequest) {
        contentRequester.request(request)
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateForward(val action: HomeScreenAction) : SideEffect
    }

    @Parcelize
    enum class GenderTab : Parcelable {
        WOMEN,
        MEN;

        fun toGender(): Gender = when (this) {
            WOMEN -> Gender.FEMALE
            MEN -> Gender.MALE
        }

        companion object {
            fun from(gender: Gender): GenderTab = when (gender) {
                Gender.FEMALE -> WOMEN
                Gender.MALE -> MEN
            }
        }
    }

    @Stable
    sealed class ContentState {
        data object Loading : ContentState()

        @Immutable
        data class Success(val content: HomeContent) : ContentState()

        @Immutable
        data class Error(val errorState: ErrorState) : ContentState()
    }

    private enum class ContentRequest : FlowRequester.Request { LOADING, REFRESHING }

    companion object {
        private const val KEY_CURRENT_GENDER_TAB = "current_gender_tab"
    }
}
