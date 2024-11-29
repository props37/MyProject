package ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery

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
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyEmailException
import ru.livetyping.zarina.core.domain.model.user.exception.UserNotFoundException
import ru.livetyping.zarina.core.domain.usecase.user.RequestPasswordResetUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.feature.signin.ui.impl.R
import ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery.model.PasswordRecoveryState
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class PasswordRecoveryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val requestPasswordReset: RequestPasswordResetUseCase,
) : ViewModel(), SideEffectSource<PasswordRecoverySideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var passwordRecoveryRequestJob: Job? = null

    @OptIn(SavedStateHandleSaveableApi::class)
    private val emailTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isEmailInvalid = MutableStateFlow(false)

    val passwordRecoveryState: StateFlow<PasswordRecoveryState> = combine(
        isEmailInvalid,
        operationTracker.ongoingOperationKeys,
    ) { isEmailInvalid, ongoingOperations ->
        PasswordRecoveryState(
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid,
            isSendButtonLoading = PasswordRecoveryRequestOperation in ongoingOperations,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = PasswordRecoveryState(
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid.value,
            isSendButtonLoading = false,
        )
    )

    init {
        makeFieldsValidOnChange()
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PasswordRecoveryScreenAction.BackClicked
            emitSideEffect(PasswordRecoverySideEffect.Navigate(action))
        }
    }

    fun onRequestPasswordRecoveryClicked() {
        if (passwordRecoveryRequestJob?.isActive == true) return

        passwordRecoveryRequestJob = viewModelScope.launch {
            operationTracker.track(PasswordRecoveryRequestOperation) {
                val email = Email.create(emailTextFieldState.text.toString())
                val params = RequestPasswordResetUseCase.Params(email)
                requestPasswordReset(params)
                    .onSuccess {
                        val text = Text.Resource(R.string.sign_in_password_recovery_password_reset_requested)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(PasswordRecoverySideEffect.ShowZarinaToast(message))

                        // TODO: [Top] Implement
                        TODO()
                    }
                    .onFailure(::handlePasswordRecoveryRequestException)
            }
        }
    }

    private fun handlePasswordRecoveryRequestException(t: Throwable) {
        when (t) {
            is EmailException -> {
                isEmailInvalid.value = true
                val textResId = when (t) {
                    is EmptyEmailException -> R.string.sign_in_password_recovery_empty_email_error
                    else -> RCommon.string.res_incorrect_data_entered
                }
                showZarinaErrorToast(Text.Resource(textResId))
            }

            is UserNotFoundException -> {
                val text = Text.Resource(R.string.sign_in_user_with_this_email_not_found)
                showZarinaErrorToast(text)
            }

            else -> {
                val text = Text.Resource(RCommon.string.res_something_went_wrong)
                showZarinaErrorToast(text)
            }
        }
    }

    private fun makeFieldsValidOnChange() {
        emailTextFieldState.textAsFlow()
            .onEach { isEmailInvalid.value = false }
            .launchIn(viewModelScope)
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(PasswordRecoverySideEffect.ShowZarinaToast(message))
    }

    private data object PasswordRecoveryRequestOperation : OperationKey
}
