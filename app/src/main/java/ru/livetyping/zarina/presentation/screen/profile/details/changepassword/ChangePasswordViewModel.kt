package ru.livetyping.zarina.presentation.screen.profile.details.changepassword

import androidx.lifecycle.SavedStateHandle
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
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.user.USER_BIRTH_DATE_DEFAULT
import ru.livetyping.zarina.domain.user.exception.OldPasswordException
import ru.livetyping.zarina.domain.user.exception.PasswordException
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.screen.profile.details.changepassword.ChangePasswordViewModel.SideEffect
import ru.livetyping.zarina.usecase.user.UpdateUserInfoUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ChangePasswordInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var changePasswordJob: Job? = null

    private val oldPasswordValueHolder = savedStateHandle.createValueHolder(
        key = KEY_OLD_PASSWORD,
        initialValue = "",
    )

    private val newPasswordValueHolder = savedStateHandle.createValueHolder(
        key = KEY_NEW_PASSWORD,
        initialValue = "",
    )

    val oldPassword: StateFlow<String> = oldPasswordValueHolder.stateFlow

    private val _isOldPasswordInvalid = MutableStateFlow(false)
    val isOldPasswordInvalid: StateFlow<Boolean> = _isOldPasswordInvalid.asStateFlow()

    val newPassword: StateFlow<String> = newPasswordValueHolder.stateFlow

    private val _isNewPasswordInvalid = MutableStateFlow(false)
    val isNewPasswordInvalid: StateFlow<Boolean> = _isNewPasswordInvalid.asStateFlow()

    val isChangePasswordButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.CHANGE_PASSWORD)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ChangePasswordScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onOldPasswordChanged(password: String) {
        oldPasswordValueHolder.set(password)
        _isOldPasswordInvalid.value = false
    }

    fun onNewPasswordChanged(password: String) {
        newPasswordValueHolder.set(password)
        _isNewPasswordInvalid.value = false
    }

    fun onNewPasswordEntered() {
        onChangePasswordClicked()
    }

    fun onChangePasswordClicked() {
        if (changePasswordJob?.isActive == true) return

        changePasswordJob = viewModelScope.launch {
            operationTracker.track(Operation.CHANGE_PASSWORD) {
                val user = interactor.getUserFlow().firstOrNull()?.getOrNull()
                if (user != null) {
                    val params = UpdateUserInfoUseCase.Params(
                        oldPassword = oldPassword.value,
                        newPassword = newPassword.value,
                        firstName = user.firstName.orEmpty(),
                        lastName = user.lastName.orEmpty(),
                        birthDate = user.birthDate ?: USER_BIRTH_DATE_DEFAULT,
                        email = user.email,
                        phone = user.phone ?: PhoneNumber.create(""),
                        gender = user.gender,
                    )
                    interactor.updateUserInfo(params)
                        .onSuccess {
                            // TODO: [High] Navigate back with result
                        }
                        .onFailure(::onChangePasswordFailure)
                } else {
                    Timber.e("Failed to change password because the user is null")
                    val text = Text.Resource(R.string.password_changing_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
            }
        }
    }

    private fun onChangePasswordFailure(throwable: Throwable) {
        when (throwable) {
            is ValidationException -> {
                val exceptions = listOf(throwable) + throwable.suppressedExceptions
                val text = when {
                    exceptions.any { it is OldPasswordException } -> {
                        Text.Resource(R.string.invalid_old_password)
                    }

                    exceptions.any { it is PasswordException } -> {
                        Text.Resource(R.string.password_does_not_meet_requirements)
                    }

                    else -> Text.Resource(R.string.password_changing_error)
                }
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(SideEffect.ShowZarinaToast(message))

                if (exceptions.any { it is OldPasswordException }) {
                    _isOldPasswordInvalid.value = true
                }
                if (exceptions.any { it is PasswordException }) {
                    _isNewPasswordInvalid.value = true
                }
            }

            else -> {
                val text = Text.Resource(R.string.password_changing_error)
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ChangePasswordScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    private enum class Operation : OperationKey { CHANGE_PASSWORD }

    companion object {
        private const val KEY_OLD_PASSWORD = "old_password"
        private const val KEY_NEW_PASSWORD = "new_password"
    }
}
