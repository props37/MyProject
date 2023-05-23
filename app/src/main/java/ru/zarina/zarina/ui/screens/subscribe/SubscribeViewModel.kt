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
import kotlinx.coroutines.flow.update
import ru.zarina.zarina.R
import ru.zarina.zarina.data.StaticPages
import ru.zarina.zarina.domain.exception.validation.EmptyException
import ru.zarina.zarina.domain.exception.validation.FormatException
import ru.zarina.zarina.domain.exception.validation.IllegalContentsException
import ru.zarina.zarina.domain.exception.validation.TooLongException
import ru.zarina.zarina.ui.common.base.FocusState
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.Text
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: SubscribeInteractor,
) : ViewModel(),
    ISideEffectSource<SubscribeViewModel.SideEffect> by SideEffectQueue() {

    val name = savedStateHandle.getStateFlow(KEY_NAME, "")
    private val nameFocusState = MutableStateFlow(FocusState())
    private val nameValidation = name
        .map { interactor.validateName(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val nameError = combine(nameFocusState, nameValidation) { focus, validation ->
        val exception = validation?.exceptionOrNull()
        when {
            exception is TooLongException ->
                Text.Resource(R.string.max_length_symbols, exception.maxLength)

            exception is EmptyException && focus.everLostFocus -> Text.Resource(R.string.field_should_be_filled)
            exception is IllegalContentsException -> Text.Resource(R.string.allowed_symbols)
            else -> null
        }
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val email = savedStateHandle.getStateFlow(KEY_EMAIL, "")
    private val emailFocusState = MutableStateFlow(FocusState())
    private val emailValidation = email
        .map { interactor.validateEmail(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val emailError = combine(emailFocusState, emailValidation) { focus, validation ->
        val exception = validation?.exceptionOrNull()
        when {
            exception is EmptyException && focus.everLostFocus -> Text.Resource(R.string.field_should_be_filled)
            exception is FormatException && focus.everLostFocus -> Text.Resource(R.string.illegal_email_format)
            else -> null
        }
    }
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

    fun onNameFocusChange(isFocused: Boolean) {
        nameFocusState.update { it.updated(isFocused) }
    }

    fun onEmailChange(email: String) {
        savedStateHandle[KEY_EMAIL] = email
    }

    fun onEmailFocusChange(isFocused: Boolean) {
        emailFocusState.update { it.updated(isFocused) }
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
