package ru.livetyping.zarina.presentation.screen.profile.details.changeemail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.user.USER_BIRTH_DATE_DEFAULT
import ru.livetyping.zarina.domain.user.exception.EmailException
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.screen.profile.details.changeemail.ChangeEmailViewModel.SideEffect
import ru.livetyping.zarina.usecase.user.UpdateUserInfoUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val interactor: ChangeEmailInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var changeEmailJob: Job? = null

    private var email = ""

    private val _isEmailInvalid = MutableStateFlow(false)
    val isEmailInvalid: StateFlow<Boolean> = _isEmailInvalid.asStateFlow()

    val isChangeEmailButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.CHANGE_EMAIL)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ChangeEmailScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onEmailChanged(email: String) {
        this.email = email
        _isEmailInvalid.value = false
    }

    fun onEmailEntered() {
        onChangeEmailClicked()
    }

    fun onChangeEmailClicked() {
        if (changeEmailJob?.isActive == true) return

        changeEmailJob = viewModelScope.launch {
            operationTracker.track(Operation.CHANGE_EMAIL) {
                val user = interactor.getUserFlow().firstOrNull()?.getOrNull()
                if (user != null) {
                    val params = UpdateUserInfoUseCase.Params(
                        email = Email.create(email),
                        firstName = user.firstName.orEmpty(),
                        lastName = user.lastName.orEmpty(),
                        birthDate = user.birthDate ?: USER_BIRTH_DATE_DEFAULT,
                        phone = user.phone ?: PhoneNumber.create(""),
                        gender = user.gender,
                    )
                    interactor.updateUserInfo(params)
                        .onSuccess { onChangeEmailSuccess() }
                        .onFailure(::onChangeEmailFailure)
                } else {
                    Timber.e("Failed to change email because the user is null")
                    val message = ZarinaToastMessage.error(EMAIL_CHANGE_ERROR_DEFAULT_TEXT)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
            }
        }
    }

    private fun onChangeEmailSuccess() {
        val text = Text.Resource(R.string.email_changed)
        val message = ZarinaToastMessage(text)
        emitSideEffect(SideEffect.ShowZarinaToast(message))

        val action = ChangeEmailScreenAction.EmailChanged
        emitSideEffect(SideEffect.Navigate(action))
    }

    private fun onChangeEmailFailure(throwable: Throwable) {
        when (throwable) {
            is ValidationException -> {
                val exceptions = listOf(this) + throwable.suppressedExceptions
                val text = when {
                    exceptions.any { it is EmailException } -> {
                        Text.Resource(R.string.invalid_email_try_again)
                    }

                    else -> EMAIL_CHANGE_ERROR_DEFAULT_TEXT
                }
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(SideEffect.ShowZarinaToast(message))

                if (exceptions.any { it is EmailException }) {
                    _isEmailInvalid.value = true
                }
            }

            else -> {
                val message = ZarinaToastMessage.error(EMAIL_CHANGE_ERROR_DEFAULT_TEXT)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ChangeEmailScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    private enum class Operation : OperationKey { CHANGE_EMAIL }

    companion object {
        private val EMAIL_CHANGE_ERROR_DEFAULT_TEXT: Text
            get() = Text.Resource(R.string.email_changing_error)
    }
}
