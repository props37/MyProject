package ru.zarina.zarina.ui.screens.pickup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class PickupViewModel @Inject constructor(
    private val interactor: PickupInteractor,
) : ViewModel(),
    ISideEffectSource<PickupViewModel.SideEffect> by SideEffectQueue() {

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
