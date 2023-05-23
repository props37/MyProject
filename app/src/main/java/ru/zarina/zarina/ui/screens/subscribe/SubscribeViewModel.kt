package ru.zarina.zarina.ui.screens.subscribe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.data.StaticPages
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: SubscribeInteractor,
) : ViewModel(),
    ISideEffectSource<SubscribeViewModel.SideEffect> by SideEffectQueue() {

    val name = savedStateHandle.getStateFlow(KEY_NAME, "")
    private val nameValidation = name
        .map { interactor.validateName(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val email = savedStateHandle.getStateFlow(KEY_EMAIL, "")
    private val emailValidation = email
        .map { interactor.validateEmail(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _isSwitchChecked = MutableStateFlow(false)
    val isSwitchChecked = _isSwitchChecked.asStateFlow()

    val isSubscribeButtonEnabled = combine(
        nameValidation,
        emailValidation,
        _isSwitchChecked
    ) { nameValidation, emailValidation, isChecked ->
        nameValidation?.isSuccess == true
                && emailValidation?.isSuccess == true
                && isChecked
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun onNameChange(name: String) {
        savedStateHandle[KEY_NAME] = name
    }

    fun onEmailChange(email: String) {
        savedStateHandle[KEY_EMAIL] = email
    }

    fun onSwitchCheckedChange(isChecked: Boolean) {
        _isSwitchChecked.value = isChecked
    }

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    fun onSubscribeClick() {
        // TODO
    }

    fun onLinkClick(link: Link) {
        val url = when (link) {
            Link.POLICY -> StaticPages.PRIVACY_POLICY_URL
            Link.RULES -> StaticPages.CONDITIONS_URL
            Link.DATA -> StaticPages.DATA_POLICY_URL
        }
        sideEffect(SideEffect.ShowBrowser(url))
    }

    enum class Link { POLICY, RULES, DATA }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
        data class ShowBrowser(val url: String) : SideEffect
    }

    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
    }

}
