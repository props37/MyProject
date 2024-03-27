package ru.zarina.zarina.ui.screen.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.screen.signup.SignUpViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val interactor: SignUpInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    sealed interface SideEffect : SideEffectSource.SideEffect
}
