package ru.zarina.zarina.ui.screens.catalog.selectcity

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue

@KoinViewModel
class SelectCityViewModel(
    private val interactor: SelectCityInteractor,
) : ViewModel(),
    ISideEffectSource<SelectCityViewModel.SideEffect> by SideEffectQueue() {

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}