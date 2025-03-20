package ru.livetyping.zarina.feature.cart.ui.impl.impl.recipient

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyEmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyFirstNameException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyLastNameException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyPhoneNumberException
import ru.livetyping.zarina.core.domain.model.user.exception.FirstNameException
import ru.livetyping.zarina.core.domain.model.user.exception.LastNameException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneNumberException
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.domain.validation.RecipientValidator
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.util.checkoutStepCount
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class RecipientViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: RecipientDependencies,
) : ViewModel(), SideEffectSource<RecipientSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<RecipientNavEntry>(
        typeMap = RecipientNavEntry.typeMap(),
    )
    private val cartType = navEntry.cartType.toCartType()

    val checkoutStep = ReadOnlyStateFlow(navEntry.checkoutStep)

    val checkoutStepCount = ReadOnlyStateFlow(cartType.checkoutStepCount)

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

    @OptIn(SavedStateHandleSaveableApi::class)
    val phoneTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState(PHONE_INITIAL_VALUE) },
    )

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
        resetTextFieldErrorsOnChange()
    }

    fun onClosedClicked() {
        navigationThrottler.throttle {
            val action = RecipientScreenAction.CloseClicked
            emitSideEffect(RecipientSideEffect.Navigate(action))
        }
    }

    fun onContinueClicked() {
        try {
            val recipient = Recipient(
                firstName = firstNameTextFieldState.text.toString().trim(),
                lastName = lastNameTextFieldState.text.toString().trim(),
                phone = PhoneNumber.create(phoneTextFieldState.text.toString()),
                email = Email.create(emailTextFieldState.text.toString()),
            )
            val recipientValidator = RecipientValidator()
            recipientValidator.validate(recipient)

            val action = RecipientScreenAction.ContinueClicked(
                cartType = cartType,
                currentCheckoutStep = checkoutStep.value + 1,
                recipient = recipient,
            )
            emitSideEffect(RecipientSideEffect.Navigate(action))
        } catch (e: Exception) {
            handleCustomerValidationException(e)
        }
    }

    private fun handleCustomerValidationException(e: Exception) {
        when (e) {
            is CombinedValidationException -> handleCustomerCombinedValidationException(e)
            is FirstNameException -> {
                _isFirstNameInvalid.value = true
                val messageResId = when (e) {
                    is EmptyFirstNameException -> R.string.cart_recipient_empty_fields_error
                    else -> R.string.cart_recipient_validation_error
                }
                showZarinaErrorToast(Text.Resource(messageResId))
            }

            is LastNameException -> {
                _isLastNameInvalid.value = true
                val messageResId = when (e) {
                    is EmptyLastNameException -> R.string.cart_recipient_empty_fields_error
                    else -> R.string.cart_recipient_validation_error
                }
                showZarinaErrorToast(Text.Resource(messageResId))
            }

            is PhoneNumberException -> {
                _isPhoneInvalid.value = true
                val messageResId = when (e) {
                    is EmptyPhoneNumberException -> R.string.cart_recipient_empty_fields_error
                    else -> R.string.cart_recipient_validation_error
                }
                showZarinaErrorToast(Text.Resource(messageResId))
            }

            is EmailException -> {
                _isEmailInvalid.value = true
                val messageResId = when (e) {
                    is EmptyEmailException -> R.string.cart_recipient_empty_fields_error
                    else -> R.string.cart_recipient_validation_error
                }
                showZarinaErrorToast(Text.Resource(messageResId))
            }

            else -> {
                val messageText = Text.Resource(RCommon.string.res_something_went_wrong)
                showZarinaErrorToast(messageText)
            }
        }
    }

    private fun handleCustomerCombinedValidationException(e: CombinedValidationException) {
        val causes = e.causes
        causes.forEach { cause ->
            when (cause) {
                is FirstNameException -> _isFirstNameInvalid.value = true
                is LastNameException -> _isLastNameInvalid.value = true
                is PhoneNumberException -> _isPhoneInvalid.value = true
                is EmailException -> _isEmailInvalid.value = true
            }
        }

        val isFirstNameEmpty = causes.any { it is EmptyFirstNameException }
        val isLastNameEmpty = causes.any { it is EmptyLastNameException }
        val isPhoneEmpty = causes.any { it is EmptyPhoneNumberException }
        val isEmailEmpty = causes.any { it is EmptyEmailException }

        val messageResId = when {
            isFirstNameEmpty || isLastNameEmpty || isPhoneEmpty || isEmailEmpty -> {
                R.string.cart_recipient_empty_fields_error
            }

            else -> R.string.cart_recipient_validation_error
        }
        val messageText = Text.Resource(messageResId)
        showZarinaErrorToast(messageText)
    }

    private fun fetchUser() {
        viewModelScope.launch {
            val params = GetUserFlowUseCase.Params(CachePolicy.LocalOnly)
            val user = deps.getUserFlow(params).firstOrNull()?.getOrNull()
            if (user != null) {
                firstNameTextFieldState.setTextAndPlaceCursorAtEnd(user.firstName.orEmpty())
                lastNameTextFieldState.setTextAndPlaceCursorAtEnd(user.lastName.orEmpty())
                phoneTextFieldState.setTextAndPlaceCursorAtEnd(user.phone?.value.orEmpty())
                emailTextFieldState.setTextAndPlaceCursorAtEnd(user.email.value)
            }
        }
    }

    private fun resetTextFieldErrorsOnChange() {
        firstNameTextFieldState.textAsFlow()
            .onEach { _isFirstNameInvalid.value = false }
            .launchIn(viewModelScope)
        lastNameTextFieldState.textAsFlow()
            .onEach { _isLastNameInvalid.value = false }
            .launchIn(viewModelScope)
        phoneTextFieldState.textAsFlow()
            .onEach { _isPhoneInvalid.value = false }
            .launchIn(viewModelScope)
        emailTextFieldState.textAsFlow()
            .onEach { _isEmailInvalid.value = false }
            .launchIn(viewModelScope)
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(RecipientSideEffect.ShowZarinaToast(message))
    }

    companion object {
        private const val PHONE_INITIAL_VALUE = "+7"
    }
}
