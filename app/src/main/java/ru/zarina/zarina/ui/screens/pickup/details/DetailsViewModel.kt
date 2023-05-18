package ru.zarina.zarina.ui.screens.pickup.details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val interactor: DetailsInteractor,
) : ViewModel(),
    ISideEffectSource<DetailsViewModel.SideEffect> by SideEffectQueue() {

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
