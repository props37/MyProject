package ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging

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
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.exception.OldPasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.PasswordException
import ru.livetyping.zarina.core.domain.usecase.user.UpdateUserInfoUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging.model.PasswordChangingEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging.model.PasswordChangingState
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class PasswordChangingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateUserInfo: UpdateUserInfoUseCase,
) : ViewModel(), SideEffectSource<PasswordChangingSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var changePasswordJob: Job? = null

    @OptIn(SavedStateHandleSaveableApi::class)
    private val oldPasswordTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    private val newPasswordTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isOldPasswordInvalid = MutableStateFlow(false)
    private val isNewPasswordInvalid = MutableStateFlow(false)

    val passwordChangingState: StateFlow<PasswordChangingState> = combine(
        isOldPasswordInvalid,
        isNewPasswordInvalid,
        operationTracker.ongoingOperationKeys,
    ) { isOldPasswordInvalid, isNewPasswordInvalid, ongoingOperations ->
        val isChangePasswordButtonLoading = ChangePasswordOperation in ongoingOperations

        PasswordChangingState(
            oldPasswordTextFieldState = oldPasswordTextFieldState,
            newPasswordTextFieldState = newPasswordTextFieldState,
            isOldPasswordInvalid = isOldPasswordInvalid,
            isNewPasswordInvalid = isNewPasswordInvalid,
            isChangePasswordButtonLoading = isChangePasswordButtonLoading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = PasswordChangingState(
            oldPasswordTextFieldState = oldPasswordTextFieldState,
            newPasswordTextFieldState = newPasswordTextFieldState,
            isOldPasswordInvalid = false,
            isNewPasswordInvalid = false,
            isChangePasswordButtonLoading = false,
        ),
    )

    init {
        makeFieldsValidOnChange()
    }

    fun onPasswordChangingEvent(event: PasswordChangingEvent) {
        when (event) {
            PasswordChangingEvent.BackClicked -> onBackClicked()
            PasswordChangingEvent.ChangePasswordClicked -> onChangePasswordClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PasswordChangingScreenAction.BackClicked
            emitSideEffect(PasswordChangingSideEffect.Navigate(action))
        }
    }

    private fun onChangePasswordClicked() {
        if (changePasswordJob?.isActive == true) return

        changePasswordJob = viewModelScope.launch {
            operationTracker.track(ChangePasswordOperation) {
                val params = UpdateUserInfoUseCase.Params(
                    oldPassword = oldPasswordTextFieldState.text.toString(),
                    newPassword = newPasswordTextFieldState.text.toString(),
                )
                updateUserInfo(params)
                    .onSuccess {
                        val text = Text.Resource(R.string.profile_password_changed)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(PasswordChangingSideEffect.ShowZarinaToast(message))

                        val action = PasswordChangingScreenAction.PasswordChanged
                        emitSideEffect(PasswordChangingSideEffect.Navigate(action))
                    }
                    .onFailure(::onPasswordChangingFailure)
            }
        }
    }

    private fun onPasswordChangingFailure(t: Throwable) {
        when (t) {
            is CombinedValidationException -> {
                val causes = t.causes
                causes.forEach { cause ->
                    when (cause) {
                        is OldPasswordException -> handleOldPasswordException()
                        is PasswordException -> handleNewPasswordException()
                    }
                }
            }

            is OldPasswordException -> handleOldPasswordException()
            is PasswordException -> handleNewPasswordException()
            else -> {
                val text = Text.Resource(RCommon.string.res_something_went_wrong)
                showZarinaErrorToast(text)
            }
        }
    }

    private fun handleOldPasswordException() {
        isOldPasswordInvalid.value = true
        val text = Text.Resource(R.string.profile_invalid_old_password)
        showZarinaErrorToast(text)
    }

    private fun handleNewPasswordException() {
        isNewPasswordInvalid.value = true
        val text = Text.Resource(R.string.profile_password_does_not_meet_requirements)
        showZarinaErrorToast(text)
    }

    private fun makeFieldsValidOnChange() {
        oldPasswordTextFieldState.textAsFlow()
            .onEach { isOldPasswordInvalid.value = false }
            .launchIn(viewModelScope)
        newPasswordTextFieldState.textAsFlow()
            .onEach { isNewPasswordInvalid.value = false }
            .launchIn(viewModelScope)
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(PasswordChangingSideEffect.ShowZarinaToast(message))
    }

    private data object ChangePasswordOperation : OperationKey
}
