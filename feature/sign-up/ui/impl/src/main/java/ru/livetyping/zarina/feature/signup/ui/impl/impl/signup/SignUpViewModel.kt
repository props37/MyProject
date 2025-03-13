package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup

import androidx.compose.foundation.text.input.TextFieldState
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.combineMore
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.sms.ZarinaSms
import ru.livetyping.zarina.core.domain.model.user.exception.BirthDateException
import ru.livetyping.zarina.core.domain.model.user.exception.EmailAlreadyUsedException
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyBirthDateException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyEmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyFirstNameException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyPasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyPhoneNumberException
import ru.livetyping.zarina.core.domain.model.user.exception.FirstNameException
import ru.livetyping.zarina.core.domain.model.user.exception.OtpTimeoutException
import ru.livetyping.zarina.core.domain.model.user.exception.PasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneNumberException
import ru.livetyping.zarina.core.domain.usecase.user.SignUpUseCase
import ru.livetyping.zarina.core.domain.validation.SignUpValidator
import ru.livetyping.zarina.core.kotlinutil.LocalDateUtil
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaEvent
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaState
import ru.livetyping.zarina.feature.signup.ui.impl.R
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.model.SignUpEvent
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.model.SignUpState
import timber.log.Timber
import java.time.ZoneOffset
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: SignUpDependencies,
) : ViewModel(), SideEffectSource<SignUpSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var signUpJob: Job? = null

    @OptIn(SavedStateHandleSaveableApi::class)
    private val nameTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isNameInvalid = MutableStateFlow(false)

    private val birthDateEpochMillisValueHolder = savedStateHandle.createValueHolder<Long?>(
        key = Keys.BIRTH_DATE_EPOCH_MILLIS.key,
        initialValue = null,
    )

    private val isBirthDateInvalid = MutableStateFlow(false)

    @OptIn(SavedStateHandleSaveableApi::class)
    private val emailTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isEmailInvalid = MutableStateFlow(false)

    @OptIn(SavedStateHandleSaveableApi::class)
    private val phoneTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState(PHONE_INITIAL_TEXT) },
    )

    private val isPhoneInvalid = MutableStateFlow(false)

    @OptIn(SavedStateHandleSaveableApi::class)
    private val passwordTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isPasswordInvalid = MutableStateFlow(false)

    private val arePoliciesAcceptedValueHolder = savedStateHandle.createValueHolder(
        key = Keys.IS_POLICIES_ACCEPTED.key,
        initialValue = false,
    )
    private val arePoliciesInvalid = MutableStateFlow(false)

    private val receiveEmailsValueHolder = savedStateHandle.createValueHolder(
        key = Keys.RECEIVE_EMAILS.key,
        initialValue = false,
    )

    private val receiveSmsValueHolder = savedStateHandle.createValueHolder(
        key = Keys.RECEIVE_SMS.key,
        initialValue = false,
    )

    private val _yandexCaptchaState = MutableStateFlow<YandexCaptchaState>(YandexCaptchaState.None)
    val yandexCaptchaState: StateFlow<YandexCaptchaState> = _yandexCaptchaState.asStateFlow()

    val signUpState: StateFlow<SignUpState> = combineMore(
        isNameInvalid,
        birthDateEpochMillisValueHolder.stateFlow,
        isBirthDateInvalid,
        isEmailInvalid,
        isPhoneInvalid,
        isPasswordInvalid,
        arePoliciesAcceptedValueHolder.stateFlow,
        arePoliciesInvalid,
        receiveEmailsValueHolder.stateFlow,
        receiveSmsValueHolder.stateFlow,
        yandexCaptchaState,
        operationTracker.ongoingOperationKeys,
    ) { isNameInvalid, birthDateEpochMillis, isBirthDateInvalid, isEmailInvalid,
        isPhoneInvalid, isPasswordInvalid, arePoliciesAccepted, arePoliciesInvalid,
        receiveEmails, receiveSms, yandexCaptchaState, ongoingOperations ->

        val isSignUpButtonLoading = SignUpOperation in ongoingOperations
                || yandexCaptchaState is YandexCaptchaState.Started

        SignUpState(
            nameTextFieldState = nameTextFieldState,
            isNameInvalid = isNameInvalid,
            birthDateEpochMillis = birthDateEpochMillis,
            isBirthDateInvalid = isBirthDateInvalid,
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid,
            phoneTextFieldState = phoneTextFieldState,
            isPhoneInvalid = isPhoneInvalid,
            passwordTextFieldState = passwordTextFieldState,
            isPasswordInvalid = isPasswordInvalid,
            receiveEmails = receiveEmails,
            receiveSms = receiveSms,
            arePoliciesAccepted = arePoliciesAccepted,
            arePoliciesInvalid = arePoliciesInvalid,
            isSignUpButtonLoading = isSignUpButtonLoading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = SignUpState(
            nameTextFieldState = nameTextFieldState,
            isNameInvalid = isNameInvalid.value,
            birthDateEpochMillis = birthDateEpochMillisValueHolder.get(),
            isBirthDateInvalid = isBirthDateInvalid.value,
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid.value,
            phoneTextFieldState = phoneTextFieldState,
            isPhoneInvalid = isPhoneInvalid.value,
            passwordTextFieldState = passwordTextFieldState,
            isPasswordInvalid = isPasswordInvalid.value,
            receiveEmails = receiveEmailsValueHolder.get(),
            receiveSms = receiveSmsValueHolder.get(),
            arePoliciesAccepted = false,
            arePoliciesInvalid = false,
            isSignUpButtonLoading = false,
        ),
    )

    init {
        makeFieldsValidOnChange()
    }

    override fun onCleared() {
        deps.smsCodeRetriever.stop()
    }

    fun onSignUpEvent(event: SignUpEvent) {
        when (event) {
            SignUpEvent.BackClicked -> onBackClicked()
            is SignUpEvent.BirthDateEpochMillisChanged -> {
                birthDateEpochMillisValueHolder.set(event.millis)
                isBirthDateInvalid.value = false
            }

            is SignUpEvent.ReceiveEmailsChanged -> receiveEmailsValueHolder.set(event.receive)
            is SignUpEvent.ReceiveSmsChanged -> receiveSmsValueHolder.set(event.receive)
            is SignUpEvent.PoliciesAcceptedChanged -> {
                arePoliciesAcceptedValueHolder.set(event.isAccepted)
            }

            SignUpEvent.SignUpClicked -> startSignUp()
        }
    }

    fun onYandexCaptchaEvent(event: YandexCaptchaEvent) {
        when (event) {
            is YandexCaptchaEvent.DismissRequested -> {
                _yandexCaptchaState.value = YandexCaptchaState.None
            }

            is YandexCaptchaEvent.TokenReceived -> {
                _yandexCaptchaState.value = YandexCaptchaState.None
                signUp(event.token)
            }
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SignUpScreenAction.BackClicked
            emitSideEffect(SignUpSideEffect.Navigate(action))
        }
    }

    private fun startSignUp() {
        if (signUpJob?.isActive == true) return

        if (!arePoliciesAcceptedValueHolder.get()) {
            showPoliciesNotAcceptedError()
            return
        }

        try {
            val signUpParams = SignUpValidator.Params(
                firstName = nameTextFieldState.text.toString(),
                birthDate = birthDateEpochMillisValueHolder.get()?.let { millis ->
                    LocalDateUtil.fromEpochMillis(millis, ZoneOffset.UTC)
                },
                email = Email.create(emailTextFieldState.text.toString()),
                phone = PhoneNumber.create(phoneTextFieldState.text.toString()),
                password = passwordTextFieldState.text.toString(),
            )
            SignUpValidator().validate(signUpParams)

            viewModelScope.launch {
                showYandexCaptcha()
            }
        } catch (e: Exception) {
            handleSignUpException(e)
        }
    }

    private fun signUp(yandexCaptchaToken: YandexCaptchaToken) {
        if (signUpJob?.isActive == true) return

        if (!arePoliciesAcceptedValueHolder.get()) {
            showPoliciesNotAcceptedError()
            return
        }

        deps.smsCodeRetriever.start(
            sender = ZarinaSms.SENDER,
            codeRegexPattern = ZarinaSms.CODE_REGEX_PATTERN,
        )

        signUpJob = viewModelScope.launch {
            operationTracker.track(SignUpOperation) {
                val birthDate = birthDateEpochMillisValueHolder.get()?.let { millis ->
                    LocalDateUtil.fromEpochMillis(millis, ZoneOffset.UTC)
                }
                val email = Email.create(emailTextFieldState.text.toString())
                val phone = PhoneNumber.create(phoneTextFieldState.text.toString())
                val password = passwordTextFieldState.text.toString()
                val params = SignUpUseCase.Params(
                    name = nameTextFieldState.text.toString(),
                    birthDate = birthDate,
                    email = email,
                    phone = phone,
                    password = password,
                    receiveEmails = receiveEmailsValueHolder.get(),
                    receiveSms = receiveSmsValueHolder.get(),
                    yandexCaptchaToken = yandexCaptchaToken,
                )
                deps.signUp(params)
                    .onSuccess {
                        deps.credentialManager.createCredential(
                            username = email.value,
                            password = password,
                        )

                        val action = SignUpScreenAction.UserCreated(phone)
                        emitSideEffect(SignUpSideEffect.Navigate(action))
                    }
                    .onFailure(::handleSignUpException)
            }
        }
    }

    private fun handleSignUpException(t: Throwable) {
        Timber.tag(TAG).e(t)
        when (t) {
            is CombinedValidationException -> handleSignUpCombinedValidationException(t)
            is FirstNameException -> {
                isNameInvalid.value = true
                val textResId = when (t) {
                    is EmptyFirstNameException -> R.string.sign_up_empty_fields_error
                    else -> RCommon.string.res_incorrect_data_entered
                }
                showZarinaErrorToast(Text.Resource(textResId))
            }

            is BirthDateException -> {
                isBirthDateInvalid.value = true
                val textResId = when (t) {
                    is EmptyBirthDateException -> R.string.sign_up_empty_fields_error
                    else -> RCommon.string.res_incorrect_data_entered
                }
                showZarinaErrorToast(Text.Resource(textResId))
            }

            is EmailException -> {
                isEmailInvalid.value = true
                val textResId = when (t) {
                    is EmptyEmailException -> R.string.sign_up_empty_fields_error
                    is EmailAlreadyUsedException -> R.string.sign_up_email_already_in_use_error
                    else -> RCommon.string.res_incorrect_data_entered
                }
                showZarinaErrorToast(Text.Resource(textResId))
            }

            is PhoneNumberException -> {
                isPhoneInvalid.value = true
                val textResId = when (t) {
                    is EmptyPhoneNumberException -> R.string.sign_up_empty_fields_error
                    else -> RCommon.string.res_incorrect_data_entered
                }
                showZarinaErrorToast(Text.Resource(textResId))
            }

            is PasswordException -> {
                isPasswordInvalid.value = true
                val textResId = when (t) {
                    is EmptyPasswordException -> R.string.sign_up_empty_fields_error
                    else -> RCommon.string.res_incorrect_data_entered
                }
                showZarinaErrorToast(Text.Resource(textResId))
            }

            is OtpTimeoutException -> {
                val text = Text.Resource(R.string.sign_up_otp_timeout_error)
                showZarinaErrorToast(text)
            }

            else -> {
                val text = Text.Resource(RCommon.string.res_something_went_wrong)
                showZarinaErrorToast(text)
            }
        }
    }

    private fun handleSignUpCombinedValidationException(e: CombinedValidationException) {
        val causes = e.causes
        causes.forEach { cause ->
            when (cause) {
                is FirstNameException -> isNameInvalid.value = true
                is BirthDateException -> isBirthDateInvalid.value = true
                is EmailException -> isEmailInvalid.value = true
                is PhoneNumberException -> isPhoneInvalid.value = true
                is PasswordException -> isPasswordInvalid.value = true
            }
        }

        val isNameEmpty = causes.any { it is EmptyFirstNameException }
        val isBirthDateEmpty = causes.any { it is EmptyBirthDateException }
        val isEmailEmpty = causes.any { it is EmptyEmailException }
        val isPhoneEmpty = causes.any { it is EmptyPhoneNumberException }
        val isPasswordEmpty = causes.any { it is EmptyPasswordException }

        val textResId = if (isNameEmpty || isBirthDateEmpty || isEmailEmpty || isPhoneEmpty || isPasswordEmpty) {
            R.string.sign_up_empty_fields_error
        } else {
            RCommon.string.res_incorrect_data_entered
        }
        showZarinaErrorToast(Text.Resource(textResId))
    }

    private fun showPoliciesNotAcceptedError() {
        arePoliciesInvalid.value = true
        val text = Text.Resource(R.string.sign_up_policies_error)
        showZarinaErrorToast(text)
    }

    private suspend fun showYandexCaptcha() {
        val captcha = deps.getYandexCaptcha().getOrNull()
        if (captcha != null) {
            _yandexCaptchaState.value = YandexCaptchaState.Started(captcha)
        } else {
            val text = Text.Resource(RCommon.string.res_something_went_wrong)
            showZarinaErrorToast(text)
        }
    }

    private fun makeFieldsValidOnChange() {
        nameTextFieldState.textAsFlow()
            .onEach { isNameInvalid.value = false }
            .launchIn(viewModelScope)
        emailTextFieldState.textAsFlow()
            .onEach { isEmailInvalid.value = false }
            .launchIn(viewModelScope)
        phoneTextFieldState.textAsFlow()
            .onEach { isPhoneInvalid.value = false }
            .launchIn(viewModelScope)
        passwordTextFieldState.textAsFlow()
            .onEach { isPasswordInvalid.value = false }
            .launchIn(viewModelScope)
    }

    private fun showZarinaErrorToast(text: Text) {
        val toastMessage = ZarinaToastMessage.error(text)
        emitSideEffect(SignUpSideEffect.ShowZarinaToast(toastMessage))
    }

    private data object SignUpOperation : OperationKey

    private enum class Keys {
        BIRTH_DATE_EPOCH_MILLIS,
        RECEIVE_EMAILS,
        RECEIVE_SMS,
        IS_POLICIES_ACCEPTED;

        val key: String get() = name
    }

    private companion object {
        private const val PHONE_INITIAL_TEXT = "+7"

        private const val TAG = "SignUpViewModel"
    }
}
