package ru.zarina.zarina.ui.screens.pickup.selectsize

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class SelectSizeViewModel @Inject constructor(
    private val interactor: SelectSizeInteractor,
) : ViewModel(),
    ISideEffectSource<SelectSizeViewModel.SideEffect> by SideEffectQueue() {

    fun onSizeClick() {
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
