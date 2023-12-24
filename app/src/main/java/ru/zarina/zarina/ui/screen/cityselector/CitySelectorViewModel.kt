package ru.zarina.zarina.ui.screen.cityselector

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class CitySelectorViewModel @Inject constructor(
    private val interactor: CitySelectorInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    sealed interface SideEffect : SideEffectSource.SideEffect
}

