package ru.zarina.zarina.ui.screen.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.screen.profile.ProfileViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: ProfileInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    sealed interface SideEffect : SideEffectSource.SideEffect
}
