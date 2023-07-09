package ru.zarina.zarina.ui.screens.catalog.selectshop

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue

@KoinViewModel
class SelectShopViewModel(
    private val filtersSavedStateHandle: SavedStateHandle,
) : ViewModel(),
    ISideEffectSource<SelectShopViewModel.SideEffect> by SideEffectQueue() {

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
