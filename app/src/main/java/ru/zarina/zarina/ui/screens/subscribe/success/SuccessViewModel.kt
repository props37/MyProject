package ru.zarina.zarina.ui.screens.subscribe.success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.navigation.destinations.Subscribe
import javax.inject.Inject

@HiltViewModel
class SuccessViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: SuccessInteractor,
) : ViewModel(),
    ISideEffectSource<SuccessViewModel.SideEffect> by SideEffectQueue() {

    val email = savedStateHandle.getStateFlow(
        key = Subscribe.Success.ARGUMENT_EMAIL,
        initialValue = ""
    )

    fun onCloseClick() {
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
