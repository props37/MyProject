package ru.zarina.zarina.ui.screen.sizetable

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.screen.sizetable.SizeTableViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class SizeTableViewModel @Inject constructor(
    private val interactor: SizeTableInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    sealed interface SideEffect : SideEffectSource.SideEffect
}
