package ru.livetyping.zarina.ui.screens.pickup.details

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import ru.livetyping.zarina.ui.common.base.ISideEffectSource
import ru.livetyping.zarina.ui.common.base.SideEffectQueue

@KoinViewModel
class DetailsViewModel : ViewModel(),
    ISideEffectSource<DetailsViewModel.SideEffect> by SideEffectQueue() {

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
