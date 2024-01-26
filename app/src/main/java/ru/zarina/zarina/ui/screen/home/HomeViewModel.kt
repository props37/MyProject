package ru.zarina.zarina.ui.screen.home

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.parcelize.Parcelize
import ru.zarina.zarina.domain.rework.content.HomeContent
import ru.zarina.zarina.ui.common.base.ErrorStateRework
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import ru.zarina.zarina.utils.clean.invoke
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: HomeInteractor,
) : ViewModel(), SideEffectSource<HomeViewModel.SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val genderTabs: StateFlow<ImmutableList<GenderTab>> =
        MutableStateFlow(GenderTab.entries.toImmutableList()).asStateFlow()

    // TODO: [Low] Store the last selected tab on the disk
    val currentGenderTab: StateFlow<GenderTab> = savedStateHandle.getStateFlow(
        key = KEY_CURRENT_GENDER_TAB,
        initialValue = GenderTab.WOMEN,
    )

    private val contentFetchRequests = MutableSharedFlow<Unit>(replay = 1)
        .also { it.tryEmit(Unit) }

    private val isFetchingContent = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val contentResult: StateFlow<Result<HomeContent>?> = contentFetchRequests
        .flatMapLatest { interactor.getHomeContentFlow() }
        .onEach { isFetchingContent.value = false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val contentState: StateFlow<ContentState> = combine(
        isFetchingContent,
        contentResult,
    ) { isFetchingContent, contentResult ->
        if (isFetchingContent || contentResult == null) {
            ContentState.Loading
        } else {
            contentResult.fold(
                onSuccess = { content ->
                    ContentState.Success(content)
                },
                onFailure = { throwable ->
                    val errorState = when (throwable) {
                        is IOException -> ErrorStateRework.NETWORK
                        else -> ErrorStateRework.GENERIC
                    }
                    ContentState.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = ContentState.Loading,
    )

    fun onGenderTabClicked(tab: GenderTab) {
        savedStateHandle[KEY_CURRENT_GENDER_TAB] = tab
    }

    fun onBannerClicked(banner: HomeContent.Banner) {
        navigationThrottler.throttle {
            val action = HomeScreenAction.BannerClicked(banner)
            emitSideEffect(SideEffect.NavigateForward(action))
        }
    }

    fun onContentErrorRefreshClicked() {
        contentFetchRequests.tryEmit(Unit)
        isFetchingContent.value = true
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateForward(val action: HomeScreenAction) : SideEffect
    }

    @Parcelize
    enum class GenderTab : Parcelable { WOMEN, MEN }

    @Stable
    sealed class ContentState {
        data object Loading : ContentState()

        @Immutable
        data class Success(val content: HomeContent) : ContentState()

        @Immutable
        data class Error(val errorState: ErrorStateRework) : ContentState()
    }

    companion object {
        private const val KEY_CURRENT_GENDER_TAB = "current_gender_tab"
    }
}
