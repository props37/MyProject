package ru.zarina.zarina.ui.screens.subscribe

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val interactor: SubscribeInteractor,
) : ViewModel(),
    ISideEffectSource<SubscribeViewModel.SideEffect> by SideEffectQueue() {

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _isSwitchChecked = MutableStateFlow(false)
    val isSwitchChecked = _isSwitchChecked.asStateFlow()

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onSwitchCheckedChange(isChecked: Boolean) {
        _isSwitchChecked.value = isChecked
    }

    fun onBackClick() {
        // TODO
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
