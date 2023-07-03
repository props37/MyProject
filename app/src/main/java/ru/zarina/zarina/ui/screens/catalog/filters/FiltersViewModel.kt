package ru.zarina.zarina.ui.screens.catalog.filters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@KoinViewModel
class FiltersViewModel @Inject constructor(
    productsSavedStateHandle: SavedStateHandle,
    private val interactor: FiltersInteractor,
) : ViewModel(),
    ISideEffectSource<FiltersViewModel.SideEffect> by SideEffectQueue() {

    fun onCloseClick() {
        sideEffect(SideEffect.GoBack)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}
