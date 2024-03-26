package ru.zarina.zarina.ui.screens.subscribe.success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.navigation.old.destinations.Subscribe

@KoinViewModel
class SuccessViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel(),
    ISideEffectSource<SuccessViewModel.SideEffect> by SideEffectQueue() {

    val email = savedStateHandle.getStateFlow(
        key = Subscribe.Success.ARGUMENT_EMAIL,
        initialValue = ""
    )

    fun onContinueClick() {
        sideEffect(SideEffect.GoBack)
    }

    fun onCloseClick() {
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
