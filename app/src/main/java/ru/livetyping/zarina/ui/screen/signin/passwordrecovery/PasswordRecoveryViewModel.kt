package ru.livetyping.zarina.ui.screen.signin.passwordrecovery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.user.exception.EmailException
import ru.livetyping.zarina.domain.user.exception.EmptyEmailException
import ru.livetyping.zarina.domain.user.exception.UserNotFoundException
import ru.livetyping.zarina.ui.base.text.Text
import ru.livetyping.zarina.ui.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.ui.screen.signin.passwordrecovery.PasswordRecoveryViewModel.SideEffect
import ru.livetyping.zarina.usecase.user.RequestPasswordResetUseCase
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: PasswordRecoveryInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val operationTracker = OperationTracker()

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var sendJob: Job? = null

    private val emailValueHolder = savedStateHandle.createValueHolder(
        key = KEY_EMAIL,
        initialValue = "",
    )

    val email: StateFlow<String> = emailValueHolder.stateFlow

    private val _isEmailInvalid = MutableStateFlow(false)
    val isEmailInvalid: StateFlow<Boolean> = _isEmailInvalid.asStateFlow()

    val isSendButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.SEND)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PasswordRecoveryScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onEmailChanged(email: String) {
        emailValueHolder.set(email)
        _isEmailInvalid.value = false
    }

    fun onSendClicked() {
        if (sendJob?.isActive == true) return
        sendJob = viewModelScope.launch {
            operationTracker.track(Operation.SEND) {
                val email = Email.create(email.value)
                val params = RequestPasswordResetUseCase.Params(email)
                interactor.requestPasswordReset(params)
                    .onSuccess {
                        val text = Text.Resource(R.string.password_recovery_password_reset_requested)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(SideEffect.ShowZarinaToast(message))

                        val action = PasswordRecoveryScreenAction.PasswordResetRequested
                        emitSideEffect(SideEffect.Navigate(action))
                    }
                    .onFailure(::onRequestPasswordResetFailure)
            }
        }
    }

    private fun onRequestPasswordResetFailure(e: Throwable) {
        when (e) {
            is EmptyEmailException -> {
                val text = Text.Resource(R.string.password_recovery_empty_email_error)
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }

            is EmailException -> {
                val text = Text.Resource(R.string.incorrect_data_entered)
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }

            is UserNotFoundException -> {
                val text = Text.Resource(R.string.user_with_this_email_not_found)
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }

            else -> {
                val text = Text.Resource(R.string.something_went_wrong)
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
        }

        if (e is EmailException) {
            _isEmailInvalid.value = true
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: PasswordRecoveryScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    private enum class Operation : OperationKey { SEND }

    companion object {
        private const val KEY_EMAIL = "email"
    }
}
