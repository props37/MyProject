package ru.livetyping.zarina.presentation.screen.generic.bottomsheet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.generic.bottomsheet.GenericBottomSheetViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class GenericBottomSheetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val title: StateFlow<Text> = savedStateHandle
        .getStateFlow<Text?>(
            key = UnscopedDestinations.GenericBottomSheet.ARG_KEY_TITLE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { title ->
            checkNotNull(title) { "title is null" }
        }

    val body: StateFlow<Text> = savedStateHandle
        .getStateFlow<Text?>(
            key = UnscopedDestinations.GenericBottomSheet.ARG_KEY_BODY,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { body ->
            checkNotNull(body) { "body is null" }
        }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = GenericBottomSheetScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: GenericBottomSheetScreenAction) : SideEffect
    }
}
