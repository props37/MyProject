package ru.livetyping.zarina.ui.screen.shops

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ShopsInteractor,
) : ViewModel(), SideEffectSource<ShopsViewModel.SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val viewModes: StateFlow<ImmutableList<ViewMode>> =
        MutableStateFlow(ViewMode.entries.toImmutableList()).asStateFlow()

    private val _currentViewMode = MutableStateFlow(ViewMode.MAP)
    val currentViewMode: StateFlow<ViewMode> = _currentViewMode.asStateFlow()

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ShopsScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onViewModeChanged(mode: ViewMode) {
        _currentViewMode.value = mode
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ShopsScreenAction) : SideEffect
    }

    enum class ViewMode { MAP, LIST }
}
