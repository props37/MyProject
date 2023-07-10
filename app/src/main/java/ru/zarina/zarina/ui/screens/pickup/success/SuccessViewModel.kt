package ru.zarina.zarina.ui.screens.pickup.success

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue

@KoinViewModel
class SuccessViewModel(
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
