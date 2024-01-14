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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import ru.zarina.zarina.domain.rework.content.HomeContent
import ru.zarina.zarina.ui.common.base.ErrorStateRework
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.utils.clean.invoke
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: HomeInteractor,
) : ViewModel(), SideEffectSource<HomeViewModel.SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var fetchContentJob: Job? = null

    val genderTabs: StateFlow<ImmutableList<GenderTab>> =
        MutableStateFlow(GenderTab.entries.toImmutableList()).asStateFlow()

    // TODO: [Low] Store the last selected tab on the disk
    val currentGenderTab: StateFlow<GenderTab> = savedStateHandle.getStateFlow(
        key = KEY_CURRENT_GENDER_TAB,
        initialValue = GenderTab.WOMEN,
    )

    private val _contentState = MutableStateFlow<ContentState>(ContentState.Loading)
    val contentState: StateFlow<ContentState> = _contentState.asStateFlow()

    init {
        fetchContent()
    }

    fun onGenderTabClicked(tab: GenderTab) {
        savedStateHandle[KEY_CURRENT_GENDER_TAB] = tab
    }

    fun onBannerClicked(banner: HomeContent.Banner) {
        navigationThrottler.throttle {
            // TODO: [High] Implement
        }
    }

    fun onContentErrorRefreshClicked() {
        _contentState.value = ContentState.Loading
        fetchContent()
    }

    private fun fetchContent() {
        fetchContentJob?.cancel()
        fetchContentJob = viewModelScope.launch {
            interactor.getHomeContentFlow().collect { result ->
                val contentState = result.fold(
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
                _contentState.value = contentState
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect

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
