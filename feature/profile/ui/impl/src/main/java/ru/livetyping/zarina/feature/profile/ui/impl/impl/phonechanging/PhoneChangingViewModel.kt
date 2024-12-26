package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.exception.InvalidPhoneNumberException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneNumberAlreadyUsedException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneNumberException
import ru.livetyping.zarina.core.domain.usecase.user.ChangePhoneNumberUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetYandexCaptchaUseCase
import ru.livetyping.zarina.core.domain.validation.PhoneValidator
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.YandexCaptchaEvent
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.model.PhoneChangingEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.model.PhoneChangingState
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class PhoneChangingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val changePhoneNumber: ChangePhoneNumberUseCase,
    private val getYandexCaptcha: GetYandexCaptchaUseCase,
) : ViewModel(), SideEffectSource<PhoneChangingSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var requestPhoneChangeJob: Job? = null

    @OptIn(SavedStateHandleSaveableApi::class)
    private val phoneTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState(PHONE_INITIAL_VALUE) },
    )

    private val isPhoneInvalid = MutableStateFlow(false)

    private val visibleYandexCaptcha = MutableStateFlow<YandexCaptcha?>(null)

    val phoneChangingState: StateFlow<PhoneChangingState> = combine(
        isPhoneInvalid,
        visibleYandexCaptcha,
        operationTracker.ongoingOperationKeys,
    ) { isPhoneInvalid, visibleYandexCaptcha, ongoingOperations ->
        val isRequestPhoneChangeButtonLoading = RequestPhoneChangeOperation in ongoingOperations
                || visibleYandexCaptcha != null

        PhoneChangingState(
            phoneTextFieldState = phoneTextFieldState,
            isPhoneInvalid = isPhoneInvalid,
            isRequestPhoneChangeButtonLoading = isRequestPhoneChangeButtonLoading,
            visibleYandexCaptcha = visibleYandexCaptcha,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = PhoneChangingState(
            phoneTextFieldState = phoneTextFieldState,
            isPhoneInvalid = false,
            isRequestPhoneChangeButtonLoading = false,
            visibleYandexCaptcha = null,
        ),
    )

    init {
        makeFieldsValidOnChange()
    }

    fun onPhoneChangingEvent(event: PhoneChangingEvent) {
        when (event) {
            PhoneChangingEvent.BackClicked -> onBackClicked()
            PhoneChangingEvent.RequestPhoneChangeClicked -> startPhoneNumberChange()
        }
    }

    fun onYandexCaptchaEvent(event: YandexCaptchaEvent) {
        when (event) {
            YandexCaptchaEvent.DismissRequested -> visibleYandexCaptcha.value = null
            is YandexCaptchaEvent.TokenReceived -> {
                visibleYandexCaptcha.value = null
                changePhoneNumber(event.token)
            }
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PhoneChangingScreenAction.BackClicked
            emitSideEffect(PhoneChangingSideEffect.Navigate(action))
        }
    }

    private fun startPhoneNumberChange() {
        if (requestPhoneChangeJob?.isActive == true) return

        try {
            val phone = PhoneNumber.create(phoneTextFieldState.text.toString())
            PhoneValidator().validate(phone)

            viewModelScope.launch {
                val yandexCaptcha = getYandexCaptcha().getOrNull()
                if (yandexCaptcha != null) {
                    visibleYandexCaptcha.value = yandexCaptcha
                } else {
                    val text = Text.Resource(RCommon.string.res_something_went_wrong)
                    showZarinaErrorToast(text)
                }
            }
        } catch (e: PhoneNumberException) {
            handlePhoneException(e)
        }
    }

    private fun changePhoneNumber(yandexCaptchaToken: YandexCaptchaToken) {
        if (requestPhoneChangeJob?.isActive == true) return

        requestPhoneChangeJob = viewModelScope.launch {
            operationTracker.track(RequestPhoneChangeOperation) {
                val phone = PhoneNumber.create(phoneTextFieldState.text.toString())
                val params = ChangePhoneNumberUseCase.Params(phone, yandexCaptchaToken)
                changePhoneNumber(params)
                    .onSuccess {
                        val action = PhoneChangingScreenAction.PhoneChangeRequested(phone)
                        emitSideEffect(PhoneChangingSideEffect.Navigate(action))
                    }
                    .onFailure(::onPhoneNumberChangeFailure)
            }
        }
    }

    private fun onPhoneNumberChangeFailure(t: Throwable) {
        when (t) {
            is CombinedValidationException -> {
                val causes = t.causes
                causes.forEach { cause ->
                    when (cause) {
                        is PhoneNumberException -> handlePhoneException(cause)
                        else -> {
                            val text = Text.Resource(RCommon.string.res_something_went_wrong)
                            showZarinaErrorToast(text)
                        }
                    }
                }
            }

            is PhoneNumberException -> handlePhoneException(t)
            else -> {
                val text = Text.Resource(RCommon.string.res_something_went_wrong)
                showZarinaErrorToast(text)
            }
        }
    }

    private fun handlePhoneException(e: PhoneNumberException) {
        isPhoneInvalid.value = true

        val textResId = when (e) {
            is PhoneNumberAlreadyUsedException -> {
                R.string.profile_phone_number_already_in_use_error
            }

            is InvalidPhoneNumberException -> R.string.profile_enter_valid_phone_number
            else -> RCommon.string.res_something_went_wrong
        }
        showZarinaErrorToast(Text.Resource(textResId))
    }

    private fun makeFieldsValidOnChange() {
        phoneTextFieldState.textAsFlow()
            .onEach { isPhoneInvalid.value = false }
            .launchIn(viewModelScope)
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(PhoneChangingSideEffect.ShowZarinaToast(message))
    }

    private data object RequestPhoneChangeOperation : OperationKey

    private companion object {
        private const val PHONE_INITIAL_VALUE = "+7"
    }
}
