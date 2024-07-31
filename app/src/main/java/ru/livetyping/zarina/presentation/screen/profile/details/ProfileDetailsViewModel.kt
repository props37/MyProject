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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.user.User
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsViewModel.SideEffect
import ru.livetyping.zarina.usecase.user.UpdateUserInfoUseCase
import ru.livetyping.zarina.usecase.user.UpdateUserNotificationSettingsUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.compose.text.clear
import ru.livetyping.zarina.util.compose.text.textAsFlow
import ru.livetyping.zarina.util.kotlin.date.LocalDateUtil
import ru.livetyping.zarina.util.kotlin.date.toMillis
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ProfileDetailsInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val userRequester = FlowRequester<Result<User>, UserRequest>(initialRequest = null) {
        interactor.getUpdatedUserFlow()
    }

    private var saveUserInfoJob: Job? = null

    private var currentUser = MutableStateFlow<User?>(null)

    private val userResult: StateFlow<Result<User>?> = userRequester.flow
        .onEach {
            val user = it.getOrNull()
            if (user != null) {
                currentUser.value = user
                updateUserInfo(user)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    @OptIn(SavedStateHandleSaveableApi::class)
    val firstNameTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    val lastNameTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

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
        userResult,
        userRequester.loadingState,
    ) { result, loadingState ->
        val isLoading = loadingState is FlowRequester.LoadingState.Loading
                && loadingState.request == UserRequest.LOADING
        if (result == null || isLoading) {
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

    val isSaveUserInfoButtonVisible: StateFlow<Boolean> = combine(
        currentUser,
        firstNameTextFieldState.textAsFlow(),
        lastNameTextFieldState.textAsFlow(),
        birthDateMillis,
    ) { currentUser, firstName, lastName, birthDateMillis ->
        if (currentUser != null) {
            val firstNameChanged = firstName.toString().trim() != currentUser.firstName
            val lastNameChanged = lastName.toString().trim() != currentUser.lastName
            val birthDate = birthDateMillis?.let { LocalDateUtil.fromMillis(it) }
            val birthDateChanged = birthDate != currentUser.birthDate
            (firstName.isNotBlank() && lastName.isNotBlank() && birthDate != null)
                    && (firstNameChanged || lastNameChanged || birthDateChanged)
        } else {
            false
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = false,
    )

    val isBirthDateChangeable: StateFlow<Boolean> = combine(
        birthDateMillis,
        userResult,
    ) { birthDateMillis, userResult ->
        if (birthDateMillis != null) {
            val currentBirthDate = LocalDateUtil.fromMillis(birthDateMillis)
            val user = userResult?.getOrNull()
            currentBirthDate == User.BIRTH_DATE_DEFAULT || currentBirthDate != user?.birthDate
        } else {
            false
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = false,
    )

    fun onScreenOpened() {
        userRequester.request(UserRequest.LOADING)
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProfileDetailsScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onSaveUserInfoClicked() {
        if (saveUserInfoJob?.isActive == true) return

        val currentUser = currentUser.value
        if (currentUser == null) {
            Timber.e("Current user is null")
            return
        }

        emitSideEffect(SideEffect.HideKeyboard)
        saveUserInfoJob = viewModelScope.launch {
            val birthDate = birthDateMillis.value
                ?.let { LocalDateUtil.fromMillis(it) } ?: User.BIRTH_DATE_DEFAULT
            val params = UpdateUserInfoUseCase.Params(
                firstName = firstNameTextFieldState.text.toString().trim(),
                lastName = lastNameTextFieldState.text.toString().trim(),
                birthDate = birthDate,
                email = Email.create(email.value),
                phone = PhoneNumber.create(phoneNumber.value ?: ""),
                gender = currentUser.gender,
            )
            interactor.updateUserInfo(params)
                .onSuccess {
                    val text = Text.Resource(R.string.personal_data_changed)
                    val message = ZarinaToastMessage(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))

                    userRequester.request(UserRequest.REFRESHING)
                }
                .onFailure {
                    val text = Text.Resource(R.string.user_info_updating_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    fun onUserErrorRefreshClicked() {
        userRequester.request(UserRequest.LOADING)
    }

    fun onBirthDateMillisChanged(millis: Long?) {
        birthDateMillisValueHolder.set(millis)
    }

    fun onPhoneNumberClicked() {
        navigationThrottler.throttle {
            val action = ProfileDetailsScreenAction.ChangePhoneNumberClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onEmailClicked() {
        navigationThrottler.throttle {
            val action = ProfileDetailsScreenAction.ChangeEmailClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onReceiveEmailsChanged(value: Boolean) {
        updateUserNotificationSettings(
            receiveSms = receiveSms.value,
            receiveEmails = value,
            onFailure = {
                delay(50.milliseconds)
                _receiveEmails.value = !value
            },
        )
    }

    fun onReceiveSmsChanged(value: Boolean) {
        updateUserNotificationSettings(
            receiveSms = value,
            receiveEmails = receiveEmails.value,
            onFailure = {
                delay(50.milliseconds)
                _receiveSms.value = !value
            },
        )
    }

    fun onChangePasswordClicked() {
        navigationThrottler.throttle {
            val action = ProfileDetailsScreenAction.ChangePasswordClicked
            emitSideEffect(SideEffect.Navigate(action))
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

    private fun updateUserNotificationSettings(
        receiveSms: Boolean,
        receiveEmails: Boolean,
        onFailure: suspend () -> Unit,
    ) {
        if (receiveSms == this.receiveSms.value && receiveEmails == this.receiveEmails.value) return

        _receiveSms.value = receiveSms
        _receiveEmails.value = receiveEmails
        viewModelScope.launch {
            val params = UpdateUserNotificationSettingsUseCase.Params(
                receiveSms = receiveSms,
                receiveEmails = receiveEmails,
            )
            interactor.updateUserNotificationSettings(params)
                .onFailure {
                    val text = Text.Resource(R.string.notification_settings_updating_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                    onFailure()
                }
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
        val birthDateMillis = user.birthDate?.toMillis()
        birthDateMillisValueHolder.set(birthDateMillis)
        _phoneNumber.value = user.phone?.value
        _email.value = user.email.value
        _receiveSms.value = user.notificationSettings.receiveSms
        _receiveEmails.value = user.notificationSettings.receiveEmails
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProfileDetailsScreenAction) : SideEffect

        data class OpenUrl(val url: String) : SideEffect

        data object HideKeyboard : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    @Stable
    sealed class State {
        data object Loading : State()

        @Immutable
        data class Success(val user: User) : State()

        @Immutable
        data class Error(val state: ErrorState) : State()
    }

    private enum class UserRequest : FlowRequester.Request { LOADING, REFRESHING }

    companion object {
        private const val KEY_BIRTH_DATE_MILLIS = "birth_date_millis"
    }
}
