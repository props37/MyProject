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
import ru.livetyping.zarina.core.domain.usecase.user.GetYandexCaptchaUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.YandexCaptchaEvent
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.model.PhoneChangingEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.model.PhoneChangingState
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class PhoneChangingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getYandexCaptcha: GetYandexCaptchaUseCase,
) : ViewModel(), SideEffectSource<PhoneChangingSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var requestPhoneChangeJob: Job? = null

    @OptIn(SavedStateHandleSaveableApi::class)
    private val phoneTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
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
            PhoneChangingEvent.RequestPhoneChangeClicked -> onRequestPhoneChangeClicked()
        }
    }

    fun onYandexCaptchaEvent(event: YandexCaptchaEvent) {
        when (event) {
            YandexCaptchaEvent.DismissRequested -> visibleYandexCaptcha.value = null
            is YandexCaptchaEvent.TokenReceived -> {
                visibleYandexCaptcha.value = null
                // TODO: [Top] Implement
                TODO()
            }
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PhoneChangingScreenAction.BackClicked
            emitSideEffect(PhoneChangingSideEffect.Navigate(action))
        }
    }

    private fun onRequestPhoneChangeClicked() {
        if (requestPhoneChangeJob?.isActive == true) return

        viewModelScope.launch {
            val yandexCaptcha = getYandexCaptcha().getOrNull()
            if (yandexCaptcha != null) {
                visibleYandexCaptcha.value = yandexCaptcha
            } else {
                val text = Text.Resource(RCommon.string.res_something_went_wrong)
                showZarinaErrorToast(text)
            }
        }
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
}
