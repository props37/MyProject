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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
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
import ru.livetyping.zarina.feature.cart.ui.impl.impl.model.CheckoutTopBarState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.recipient.model.RecipientState
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
    private val checkoutStep = navEntry.checkoutStep

    val topBarState: StateFlow<CheckoutTopBarState> = ReadOnlyStateFlow(
        CheckoutTopBarState(
            checkoutStep = checkoutStep,
            checkoutStepCount = cartType.checkoutStepCount,
        )
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    val firstNameTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isFirstNameInvalid = MutableStateFlow(false)

    @OptIn(SavedStateHandleSaveableApi::class)
    val lastNameTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isLastNameInvalid = MutableStateFlow(false)

    @OptIn(SavedStateHandleSaveableApi::class)
    val phoneTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState(PHONE_INITIAL_VALUE) },
    )

    private val isPhoneInvalid = MutableStateFlow(false)

    @OptIn(SavedStateHandleSaveableApi::class)
    val emailTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isEmailInvalid = MutableStateFlow(false)

    private val initialRecipientState = RecipientState(
        firstNameTextFieldState = firstNameTextFieldState,
        isFirstNameInvalid = false,
        lastNameTextFieldState = lastNameTextFieldState,
        isLastNameInvalid = false,
        phoneTextFieldState = phoneTextFieldState,
        isPhoneInvalid = false,
        emailTextFieldState = emailTextFieldState,
        isEmailInvalid = false,
    )

    val recipientState: StateFlow<RecipientState> = combine(
        isFirstNameInvalid,
        isLastNameInvalid,
        isPhoneInvalid,
        isEmailInvalid,
    ) { isFirstNameInvalid, isLastNameInvalid, isPhoneInvalid, isEmailInvalid ->
        RecipientState(
            firstNameTextFieldState = firstNameTextFieldState,
            isFirstNameInvalid = isFirstNameInvalid,
            lastNameTextFieldState = lastNameTextFieldState,
            isLastNameInvalid = isLastNameInvalid,
            phoneTextFieldState = phoneTextFieldState,
            isPhoneInvalid = isPhoneInvalid,
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = initialRecipientState,
    )

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
                currentCheckoutStep = checkoutStep,
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
                isFirstNameInvalid.value = true
                val messageResId = when (e) {
                    is EmptyFirstNameException -> R.string.cart_recipient_empty_fields_error
                    else -> R.string.cart_recipient_validation_error
                }
                showZarinaErrorToast(Text.Resource(messageResId))
            }

            is LastNameException -> {
                isLastNameInvalid.value = true
                val messageResId = when (e) {
                    is EmptyLastNameException -> R.string.cart_recipient_empty_fields_error
                    else -> R.string.cart_recipient_validation_error
                }
                showZarinaErrorToast(Text.Resource(messageResId))
            }

            is PhoneNumberException -> {
                isPhoneInvalid.value = true
                val messageResId = when (e) {
                    is EmptyPhoneNumberException -> R.string.cart_recipient_empty_fields_error
                    else -> R.string.cart_recipient_validation_error
                }
                showZarinaErrorToast(Text.Resource(messageResId))
            }

            is EmailException -> {
                isEmailInvalid.value = true
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
                is FirstNameException -> isFirstNameInvalid.value = true
                is LastNameException -> isLastNameInvalid.value = true
                is PhoneNumberException -> isPhoneInvalid.value = true
                is EmailException -> isEmailInvalid.value = true
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
            .onEach { isFirstNameInvalid.value = false }
            .launchIn(viewModelScope)
        lastNameTextFieldState.textAsFlow()
            .onEach { isLastNameInvalid.value = false }
            .launchIn(viewModelScope)
        phoneTextFieldState.textAsFlow()
            .onEach { isPhoneInvalid.value = false }
            .launchIn(viewModelScope)
        emailTextFieldState.textAsFlow()
            .onEach { isEmailInvalid.value = false }
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
