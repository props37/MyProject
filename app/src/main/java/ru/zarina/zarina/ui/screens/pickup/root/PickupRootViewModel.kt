package ru.zarina.zarina.ui.screens.pickup.root

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class PickupRootViewModel @Inject constructor(
    private val interactor: PickupRootInteractor,
) : ViewModel(),
    ISideEffectSource<PickupRootViewModel.SideEffect> by SideEffectQueue() {

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    fun onSelectSizeClick() {
        sideEffect(SideEffect.ShowSelectSize)
    }

    fun onSelectCityClick() {
        sideEffect(SideEffect.ShowSelectCity)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
        object ShowSelectSize : SideEffect
        object ShowSelectCity : SideEffect
    }

}
