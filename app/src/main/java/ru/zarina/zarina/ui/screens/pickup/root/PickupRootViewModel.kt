package ru.zarina.zarina.ui.screens.pickup.root

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue

@KoinViewModel
class PickupRootViewModel(
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

    fun onStockPickupClick() {
        sideEffect(SideEffect.ShowDetails)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
        object ShowSelectSize : SideEffect
        object ShowSelectCity : SideEffect
        object ShowDetails : SideEffect
    }

}
