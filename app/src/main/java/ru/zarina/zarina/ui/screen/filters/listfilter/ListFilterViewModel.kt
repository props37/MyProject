package ru.zarina.zarina.ui.screen.filters.listfilter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class ListFilterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    sealed interface SideEffect : SideEffectSource.SideEffect
}
