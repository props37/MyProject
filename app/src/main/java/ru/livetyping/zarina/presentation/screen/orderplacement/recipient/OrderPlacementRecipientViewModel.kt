package ru.livetyping.zarina.presentation.screen.orderplacement.recipient

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.user.exception.EmailException
import ru.livetyping.zarina.domain.user.exception.EmptyEmailException
import ru.livetyping.zarina.domain.user.exception.EmptyFirstNameException
import ru.livetyping.zarina.domain.user.exception.EmptyLastNameException
import ru.livetyping.zarina.domain.user.exception.EmptyPhoneNumberException
import ru.livetyping.zarina.domain.user.exception.FirstNameException
import ru.livetyping.zarina.domain.user.exception.LastNameException
import ru.livetyping.zarina.domain.user.exception.PhoneNumberException
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.OrderPlacementGraph
import ru.livetyping.zarina.presentation.screen.orderplacement.common.orderPlacementStepCount
import ru.livetyping.zarina.presentation.screen.orderplacement.recipient.OrderPlacementRecipientViewModel.SideEffect
import ru.livetyping.zarina.usecase.orderplacement.ValidateRecipientUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.compose.text.textAsFlow
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class OrderPlacementRecipientViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: OrderPlacementRecipientInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var validateRecipientJob: Job? = null

    private val cartType = savedStateHandle
        .getStateFlow<CartTypeParcelable?>(
            key = OrderPlacementGraph.Recipient.ARG_KEY_CART_TYPE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            checkNotNull(parcelable) { "cartType is null" }
            parcelable.toCartType()
        }

    val step: StateFlow<Int> = savedStateHandle
        .getStateFlow<Int?>(
            key = OrderPlacementGraph.Recipient.ARG_KEY_STEP,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "step is null" }
        }

    val stepCount: StateFlow<Int> =
        MutableStateFlow(cartType.value.orderPlacementStepCount).asStateFlow()

    @OptIn(SavedStateHandleSaveableApi::class)
    val firstNameTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val _isFirstNameInvalid = MutableStateFlow(false)
    val isFirstNameInvalid: StateFlow<Boolean> = _isFirstNameInvalid.asStateFlow()

    @OptIn(SavedStateHandleSaveableApi::class)
    val lastNameTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val _isLastNameInvalid = MutableStateFlow(false)
    val isLastNameInvalid: StateFlow<Boolean> = _isLastNameInvalid.asStateFlow()

    private val phoneValueHolder = savedStateHandle.createValueHolder(
        key = KEY_PHONE,
        initialValue = PHONE_INITIAL_VALUE,
    )
    val phone: StateFlow<String> = phoneValueHolder.stateFlow

    private val _isPhoneInvalid = MutableStateFlow(false)
    val isPhoneInvalid: StateFlow<Boolean> = _isPhoneInvalid.asStateFlow()

    @OptIn(SavedStateHandleSaveableApi::class)
    val emailTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val _isEmailInvalid = MutableStateFlow(false)
    val isEmailInvalid: StateFlow<Boolean> = _isEmailInvalid.asStateFlow()

    init {
        fetchUser()
        resetFirstNameErrorsOnChange()
        resetLastNameErrorsOnChange()
        resetEmailErrorsOnChange()
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = OrderPlacementRecipientScreenAction.OrderPlacementClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onPhoneChanged(phone: String) {
        phoneValueHolder.set(phone)
        _isPhoneInvalid.value = false
    }

    fun onContinueClicked() {
        if (validateRecipientJob?.isActive == true) return

        validateRecipientJob = viewModelScope.launch {
            val params = ValidateRecipientUseCase.Params(
                firstName = firstNameTextFieldState.text.toString().trim(),
                lastName = lastNameTextFieldState.text.toString().trim(),
                phone = PhoneNumber.create(phone.value),
                email = Email.create(emailTextFieldState.text.toString()),
            )
            interactor.validateRecipient(params)
                .onSuccess { onValidateRecipientSuccess() }
                .onFailure(::onValidateRecipientFailure)
        }
    }

    private fun onValidateRecipientSuccess() {
        when (cartType.value) {
            CartType.DELIVERY -> {
                // TODO: [High] Implement
            }

            CartType.PICKUP -> {
                val action = OrderPlacementRecipientScreenAction.RecipientValidated(
                    cartType = cartType.value,
                    step = step.value + 1,
                )
                emitSideEffect(SideEffect.Navigate(action))
            }
        }
    }

    private fun onValidateRecipientFailure(t: Throwable) {
        when (t) {
            is ValidationException -> {
                val exceptions = listOf(t) + t.suppressedExceptions
                if (exceptions.any { it is FirstNameException }) {
                    _isFirstNameInvalid.value = true
                }
                if (exceptions.any { it is LastNameException }) {
                    _isLastNameInvalid.value = true
                }
                if (exceptions.any { it is PhoneNumberException }) {
                    _isPhoneInvalid.value = true
                }
                if (exceptions.any { it is EmailException }) {
                    _isEmailInvalid.value = true
                }

                val isFirstNameEmpty = exceptions.any { it is EmptyFirstNameException }
                val isLastNameEmpty = exceptions.any { it is EmptyLastNameException }
                val isPhoneEmpty = exceptions.any { it is EmptyPhoneNumberException }
                val isEmailEmpty = exceptions.any { it is EmptyEmailException }

                val messageResId = when {
                    isFirstNameEmpty || isLastNameEmpty || isPhoneEmpty || isEmailEmpty -> {
                        R.string.order_placement_recipient_empty_fields_error
                    }

                    else -> {
                        R.string.order_placement_recipient_validation_error
                    }
                }
                val messageText = Text.Resource(messageResId)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }

            else -> {
                val messageText = Text.Resource(R.string.something_went_wrong)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
        }
    }

    private fun fetchUser() {
        viewModelScope.launch {
            val user = interactor.getUserFlow().firstOrNull()?.getOrNull()
            if (user != null) {
                firstNameTextFieldState.setTextAndPlaceCursorAtEnd(user.firstName.orEmpty())
                lastNameTextFieldState.setTextAndPlaceCursorAtEnd(user.lastName.orEmpty())
                phoneValueHolder.set(user.phone?.value.orEmpty())
                emailTextFieldState.setTextAndPlaceCursorAtEnd(user.email.value)
            }
        }
    }

    private fun resetFirstNameErrorsOnChange() {
        firstNameTextFieldState.textAsFlow()
            .onEach { _isFirstNameInvalid.value = false }
            .launchIn(viewModelScope)
    }

    private fun resetLastNameErrorsOnChange() {
        lastNameTextFieldState.textAsFlow()
            .onEach { _isLastNameInvalid.value = false }
            .launchIn(viewModelScope)
    }

    private fun resetEmailErrorsOnChange() {
        emailTextFieldState.textAsFlow()
            .onEach { _isEmailInvalid.value = false }
            .launchIn(viewModelScope)
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: OrderPlacementRecipientScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    companion object {
        private const val PHONE_INITIAL_VALUE = "+7"

        private const val KEY_PHONE = "phone"
    }
}
