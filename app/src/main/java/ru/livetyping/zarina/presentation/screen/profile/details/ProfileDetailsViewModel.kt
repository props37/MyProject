package ru.livetyping.zarina.presentation.screen.profile.details

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.user.User
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsViewModel.SideEffect
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.compose.text.clear
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ProfileDetailsInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val remoteUserRequester = FlowRequester(RemoteUserRequest.GENERAL) {
        interactor.getRemoteUserFlow()
    }

    private val remoteUserResult: StateFlow<Result<User>?> = remoteUserRequester.flow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    @OptIn(SavedStateHandleSaveableApi::class)
    val lastNameTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
    ) {
        TextFieldState()
    }

    @OptIn(SavedStateHandleSaveableApi::class)
    val firstNameTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
    ) {
        TextFieldState()
    }

    private val birthDateMillisValueHolder = savedStateHandle.createValueHolder<Long?>(
        key = KEY_BIRTH_DATE_MILLIS,
        initialValue = null,
    )

    val birthDateMillis: StateFlow<Long?> = birthDateMillisValueHolder.stateFlow

    private val _phoneNumber = MutableStateFlow<String?>(null)
    val phoneNumber: StateFlow<String?> = _phoneNumber.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _receiveEmails = MutableStateFlow(false)
    val receiveEmails: StateFlow<Boolean> = _receiveEmails.asStateFlow()

    private val _receiveSms = MutableStateFlow(false)
    val receiveSms: StateFlow<Boolean> = _receiveSms.asStateFlow()

    val state: StateFlow<State> = combine(
        remoteUserResult.onEach {
            val user = it?.getOrNull()
            if (user != null) updateUserInfo(user)
        },
        remoteUserRequester.loadingState,
    ) { result, loadingState ->
        if (result == null || loadingState.isLoading) {
            State.Loading
        } else {
            result.fold(
                onSuccess = { user -> State.Success(user) },
                onFailure = {
                    val errorState = ErrorState.from(it)
                    State.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = State.Loading,
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProfileDetailsScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onRemoteUserErrorRefreshClicked() {
        remoteUserRequester.request(RemoteUserRequest.GENERAL)
    }

    fun onBirthDateMillisChanged(millis: Long?) {
        birthDateMillisValueHolder.set(millis)
    }

    fun onPhoneNumberClicked() {
        navigationThrottler.throttle {
            // TODO: [High] Implement
        }
    }

    fun onEmailClicked() {
        navigationThrottler.throttle {
            // TODO: [High] Implement
        }
    }

    fun onReceiveEmailsChanged(value: Boolean) {
        _receiveEmails.value = value
        // TODO: [High] Implement
    }

    fun onReceiveSmsChanged(value: Boolean) {
        _receiveSms.value = value
        // TODO: [High] Implement
    }

    fun onChangePasswordClicked() {
        navigationThrottler.throttle {
            // TODO: [High] Implement
        }
    }

    fun onSignOutClicked() {
        navigationThrottler.throttle {
            val action = ProfileDetailsScreenAction.SignOutClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onDeleteAccountClicked() {
        navigationThrottler.throttle {
            val action = ProfileDetailsScreenAction.DeleteAccountClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onUrlClicked(url: String) {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.OpenUrl(url))
        }
    }

    private fun updateUserInfo(user: User) {
        lastNameTextFieldState.edit {
            clear()
            append(user.lastName?.trim())
            placeCursorAtEnd()
        }
        firstNameTextFieldState.edit {
            clear()
            append(user.firstName?.trim())
            placeCursorAtEnd()
        }
        val birthDateMillis = user.birthDate
            ?.atStartOfDay(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
        birthDateMillisValueHolder.set(birthDateMillis)
        _phoneNumber.value = user.phone?.value
        _email.value = user.email.value
        // TODO: [High] Set up notifications
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProfileDetailsScreenAction) : SideEffect

        data class OpenUrl(val url: String) : SideEffect
    }

    @Stable
    sealed class State {
        data object Loading : State()

        @Immutable
        data class Success(val user: User) : State()

        @Immutable
        data class Error(val state: ErrorState) : State()
    }

    private enum class RemoteUserRequest : FlowRequester.Request { GENERAL }

    companion object {
        private const val KEY_BIRTH_DATE_MILLIS = "birth_date_millis"
    }
}
