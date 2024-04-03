package ru.livetyping.zarina.ui.screens.subscribe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.data.old.StaticPages
import ru.livetyping.zarina.domain.old.Barcode
import ru.livetyping.zarina.domain.old.exception.validation.EmptyException
import ru.livetyping.zarina.domain.old.exception.validation.FormatException
import ru.livetyping.zarina.domain.old.exception.validation.IllegalContentsException
import ru.livetyping.zarina.domain.old.exception.validation.TooLongException
import ru.livetyping.zarina.ui.base.text.Text
import ru.livetyping.zarina.ui.common.base.FocusState
import ru.livetyping.zarina.ui.common.base.ISideEffectSource
import ru.livetyping.zarina.ui.common.base.SideEffectQueue
import ru.livetyping.zarina.ui.navigation.old.destinations.Subscribe
import ru.livetyping.zarina.utils.coroutine.mapState

@KoinViewModel
class SubscribeViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: SubscribeInteractor,
) : ViewModel(),
    ISideEffectSource<SubscribeViewModel.SideEffect> by SideEffectQueue() {

    private val offerBarcode = savedStateHandle.getStateFlow(Subscribe.ARGUMENT_OFFER_BARCODE, "")
        .mapState(viewModelScope) { Barcode(it) }

    private val operationTracker = OperationTracker()

    private val isSubscriptionInProgress = operationTracker.isOperationOngoing(Operation.SUBSCRIBE)
        .stateIn(viewModelScope, SharingStarted.Lazily, false)
    val isInputEnabled = isSubscriptionInProgress.mapState(viewModelScope) { !it }
    val isLoaderVisible = isSubscriptionInProgress

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
        if (!isInputEnabled.value) return
        savedStateHandle[KEY_NAME] = name
    }

    fun onNameFocusChange(isFocused: Boolean) {
        nameFocusState.update { it.updated(isFocused) }
    }

    fun onEmailChange(email: String) {
        if (!isInputEnabled.value) return
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
        viewModelScope.launch {
            operationTracker.track(Operation.SUBSCRIBE) {
                val email = email.value
                interactor.subscribeToOffer(
                    offerBarcode = offerBarcode.value,
                    name = name.value,
                    email = email
                )
                    .onSuccess {
                        sideEffect(SideEffect.ShowSuccess(email))
                    }
                    .onFailure {
                        sideEffect(SideEffect.ShowError(Text.Resource(R.string.unable_to_subscribe)))
                    }
            }
        }
    }

    fun onLinkClick(link: Link) {
        val url = when (link) {
            Link.POLICY -> StaticPages.PRIVACY_POLICY_URL
            Link.RULES -> StaticPages.CONDITIONS_URL
            Link.DATA -> StaticPages.DATA_POLICY_URL
        }
        sideEffect(SideEffect.ShowWebpage(url))
    }

    enum class Link { POLICY, RULES, DATA }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
        data class ShowWebpage(val url: String) : SideEffect
        data class ShowSuccess(val email: String) : SideEffect
        data class ShowError(val message: Text) : SideEffect
    }

    enum class Operation : OperationKey { SUBSCRIBE }

    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
    }

}
