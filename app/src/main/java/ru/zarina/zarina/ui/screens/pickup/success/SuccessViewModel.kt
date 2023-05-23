package ru.zarina.zarina.ui.screens.pickup.success

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class SuccessViewModel @Inject constructor(
    private val interactor: SuccessInteractor,
) : ViewModel(),
    ISideEffectSource<SuccessViewModel.SideEffect> by SideEffectQueue() {

    fun onContinueShoppingClick() {
        sideEffect(SideEffect.GoBack)
    }

    fun onCloseClick() {
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
