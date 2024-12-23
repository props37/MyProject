package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.combineMore
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.UpdateUserInfoUseCase
import ru.livetyping.zarina.core.domain.usecase.user.UpdateUserNotificationsSettingsUseCase
import ru.livetyping.zarina.core.kotlinutil.LocalDateUtil
import ru.livetyping.zarina.core.kotlinutil.toEpochMillis
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.AccountDeletionDialogEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.AccountDeletionDialogState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsTopBarEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsTopBarState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.SignOutDialogEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.SignOutDialogState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ProfileDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: ProfileDetailsDeps,
) : ViewModel(), SideEffectSource<ProfileDetailsSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var saveChangesJob: Job? = null
    private var signOutJob: Job? = null
    private var deleteAccountJob: Job? = null

    private val userParams = GetUserFlowUseCase.Params(CachePolicy.Remote())
    private val userRequester = FlowRequester<Result<User?>, UserRequest> {
        deps.getUserFlow(userParams)
    }

    @OptIn(SavedStateHandleSaveableApi::class)
    private val firstNameTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    private val lastNameTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val birthDateEpochMillisValueHolder = savedStateHandle.createValueHolder<Long?>(
        key = Keys.BIRTH_DATE_EPOCH_MILLIS.key,
        initialValue = null,
    )

    private val phone = MutableStateFlow<PhoneNumber?>(null)

    private val email = MutableStateFlow<Email?>(null)

    private val receiveEmails = MutableStateFlow(false)

    private val receiveSms = MutableStateFlow(false)

    private val currentUser = MutableStateFlow<User?>(null)

    private val userResultFlow = userRequester.flow
        .onEach { result ->
            val user = result.getOrNull()
            if (user != null) {
                currentUser.value = user
                updateFieldsWithUser(user)
            }
        }

    val topBarState: StateFlow<ProfileDetailsTopBarState> = combine(
        currentUser,
        firstNameTextFieldState.textAsFlow(),
        lastNameTextFieldState.textAsFlow(),
        birthDateEpochMillisValueHolder.stateFlow,
    ) { currentUser, firstName, lastName, birthDateEpochMillis ->
        val isSaveButtonVisible = if (currentUser != null) {
            val firstNameChanged = firstName.toString().trim() != currentUser.firstName
            val lastNameChanged = lastName.toString().trim() != currentUser.lastName
            val birthDate = birthDateEpochMillis?.let { LocalDateUtil.fromMillis(it) }
            val birthDateChanged = birthDate != currentUser.birthDate
            (firstName.isNotBlank() && lastName.isNotBlank() && birthDate != null)
                    && (firstNameChanged || lastNameChanged || birthDateChanged)
        } else {
            false
        }
        ProfileDetailsTopBarState(
            isSaveButtonVisible = isSaveButtonVisible,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = ProfileDetailsTopBarState(isSaveButtonVisible = false),
    )

    val profileDetailsState: StateFlow<ProfileDetailsState> = combineMore(
        userResultFlow,
        userRequester.loadingState,
        birthDateEpochMillisValueHolder.stateFlow,
        phone,
        email,
        receiveEmails,
        receiveSms,
    ) { userResult, loadingState, birthDateEpochMillis, phone, email, receiveEmails, receiveSms ->
        val isLoading = loadingState is FlowRequester.LoadingState.Loading
                && loadingState.request == UserRequest.LOADING
        if (isLoading) {
            ProfileDetailsState.Loading
        } else {
            userResult.fold(
                onSuccess = { user ->
                    val isBirthDateChangeable = if (birthDateEpochMillis != null) {
                        val currentBirthDate = LocalDateUtil.fromMillis(birthDateEpochMillis)
                        currentBirthDate == User.BIRTH_DATE_MIN_VALUE || currentBirthDate != user?.birthDate
                    } else {
                        false
                    }

                    ProfileDetailsState.Success(
                        firstNameTextFieldState = firstNameTextFieldState,
                        lastNameTextFieldState = lastNameTextFieldState,
                        birthDateEpochMillis = birthDateEpochMillis,
                        isBirthDateChangeable = isBirthDateChangeable,
                        phone = phone,
                        email = email ?: Email.create(""),
                        receiveEmails = receiveEmails,
                        receiveSms = receiveSms,
                    )
                },
                onFailure = {
                    val errorState = ZarinaErrorScreenState.from(it)
                    ProfileDetailsState.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = ProfileDetailsState.Loading,
    )

    private val isSignOutDialogVisible = MutableStateFlow(false)
    val signOutDialogState: StateFlow<SignOutDialogState> = combine(
        isSignOutDialogVisible,
        operationTracker.ongoingOperationKeys,
    ) { isVisible, ongoingOperations ->
        if (isVisible) {
            val isSignOutButtonLoading = Operation.SIGN_OUT in ongoingOperations
            SignOutDialogState.Visible(isSignOutButtonLoading = isSignOutButtonLoading)
        } else {
            SignOutDialogState.Hidden
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = SignOutDialogState.Hidden,
    )

    private val isAccountDeletionDialogVisible = MutableStateFlow(false)
    val accountDeletionDialogState: StateFlow<AccountDeletionDialogState> = combine(
        isAccountDeletionDialogVisible,
        operationTracker.ongoingOperationKeys,
    ) { isVisible, ongoingOperations ->
        if (isVisible) {
            val isDeleteButtonLoading = Operation.DELETE_ACCOUNT in ongoingOperations
            AccountDeletionDialogState.Visible(isDeleteButtonLoading = isDeleteButtonLoading)
        } else {
            AccountDeletionDialogState.Hidden
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = AccountDeletionDialogState.Hidden,
    )

    fun onTopBarEvent(event: ProfileDetailsTopBarEvent) {
        when (event) {
            ProfileDetailsTopBarEvent.BackClicked -> onBackClicked()
            ProfileDetailsTopBarEvent.SaveChangesClicked -> onSaveChangesClicked()
        }
    }

    fun onProfileDetailsEvent(event: ProfileDetailsEvent) {
        when (event) {
            is ProfileDetailsEvent.BirthDateEpochMillisChanged -> {
                birthDateEpochMillisValueHolder.set(event.millis)
            }

            ProfileDetailsEvent.EmailClicked -> onEmailClicked()
            ProfileDetailsEvent.PhoneClicked -> onPhoneClicked()
            ProfileDetailsEvent.ChangePasswordClicked -> onChangePasswordClicked()
            is ProfileDetailsEvent.ReceiveEmailsChanged -> onReceiveEmailsChanged(event)
            is ProfileDetailsEvent.ReceiveSmsChanged -> onReceiveSmsChanged(event)
            ProfileDetailsEvent.SignOutClicked -> isSignOutDialogVisible.value = true
            ProfileDetailsEvent.DeleteAccountClicked -> isAccountDeletionDialogVisible.value = true
            ProfileDetailsEvent.ErrorRefreshClicked -> userRequester.request(UserRequest.LOADING)
        }
    }

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> Unit
            LifecycleEvent.ON_START -> userRequester.request(UserRequest.LOADING)
            LifecycleEvent.ON_RESUME -> Unit
        }
    }

    fun onSignOutDialogEvent(event: SignOutDialogEvent) {
        when (event) {
            SignOutDialogEvent.DismissRequested -> {
                if (signOutJob?.isActive == true) return
                isSignOutDialogVisible.value = false
            }

            SignOutDialogEvent.SignOutClicked -> signOut()
        }
    }

    fun onAccountDeletionDialogEvent(event: AccountDeletionDialogEvent) {
        when (event) {
            AccountDeletionDialogEvent.DismissRequested -> {
                if (deleteAccountJob?.isActive == true) return
                isAccountDeletionDialogVisible.value = false
            }

            AccountDeletionDialogEvent.DeleteAccountClicked -> deleteAccount()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProfileDetailsScreenAction.BackClicked
            emitSideEffect(ProfileDetailsSideEffect.Navigate(action))
        }
    }

    private fun onSaveChangesClicked() {
        if (saveChangesJob?.isActive == true) return

        val currentUser = currentUser.value
        if (currentUser == null) {
            Timber.tag(TAG).e("Current user is null")
            return
        }

        emitSideEffect(ProfileDetailsSideEffect.HideKeyboard)

        saveChangesJob = viewModelScope.launch {
            val birthDate = birthDateEpochMillisValueHolder.get()?.let { millis ->
                LocalDateUtil.fromMillis(millis)
            } ?: User.BIRTH_DATE_MIN_VALUE
            val params = UpdateUserInfoUseCase.Params(
                firstName = firstNameTextFieldState.text.toString(),
                lastName = lastNameTextFieldState.text.toString(),
                birthDate = birthDate,
                email = email.value ?: currentUser.email,
                phone = phone.value ?: currentUser.phone ?: PhoneNumber.create(""),
                gender = currentUser.gender,
            )
            deps.updateUserInfo(params)
                .onSuccess {
                    val text = Text.Resource(R.string.profile_personal_data_changed)
                    val message = ZarinaToastMessage(text)
                    emitSideEffect(ProfileDetailsSideEffect.ShowZarinaToast(message))

                    userRequester.request(UserRequest.REFRESHING)
                }
                .onFailure {
                    val text = Text.Resource(R.string.profile_user_info_updating_error)
                    showZarinaErrorToast(text)
                }
        }
    }

    private fun onEmailClicked() {
        navigationThrottler.throttle {
            val action = ProfileDetailsScreenAction.ChangeEmailClicked
            emitSideEffect(ProfileDetailsSideEffect.Navigate(action))
        }
    }

    private fun onPhoneClicked() {
        navigationThrottler.throttle {
            // TODO: [Top] Implement
            TODO()
        }
    }

    private fun onChangePasswordClicked() {
        navigationThrottler.throttle {
            // TODO: [Top] Implement
            TODO()
        }
    }

    private fun onReceiveEmailsChanged(event: ProfileDetailsEvent.ReceiveEmailsChanged) {
        updateNotificationsSettings(
            receiveEmails = event.receiveEmails,
            receiveSms = receiveSms.value,
            onFailure = {
                delay(NOTIFICATIONS_SETTINGS_RESET_DELAY_MILLIS)
                receiveEmails.value = !event.receiveEmails
            },
        )
    }

    private fun onReceiveSmsChanged(event: ProfileDetailsEvent.ReceiveSmsChanged) {
        updateNotificationsSettings(
            receiveEmails = receiveEmails.value,
            receiveSms = event.receiveSms,
            onFailure = {
                delay(NOTIFICATIONS_SETTINGS_RESET_DELAY_MILLIS)
                receiveSms.value = !event.receiveSms
            },
        )
    }

    private fun signOut() {
        if (signOutJob?.isActive == true) return

        signOutJob = viewModelScope.launch {
            operationTracker.track(Operation.SIGN_OUT) {
                deps.signOut()
                    .onSuccess {
                        val action = ProfileDetailsScreenAction.UserSignedOut
                        emitSideEffect(ProfileDetailsSideEffect.Navigate(action))
                    }
                    .onFailure {
                        val text = Text.Resource(R.string.profile_sign_out_error)
                        showZarinaErrorToast(text)
                    }
            }
        }
    }

    private fun deleteAccount() {
        if (deleteAccountJob?.isActive == true) return

        deleteAccountJob = viewModelScope.launch {
            operationTracker.track(Operation.DELETE_ACCOUNT) {
                deps.deleteAccount()
                    .onSuccess {
                        val action = ProfileDetailsScreenAction.AccountDeleted
                        emitSideEffect(ProfileDetailsSideEffect.Navigate(action))
                    }
                    .onFailure {
                        val text = Text.Resource(R.string.profile_account_deletion_error)
                        showZarinaErrorToast(text)
                    }
            }
        }
    }

    private fun updateNotificationsSettings(
        receiveSms: Boolean,
        receiveEmails: Boolean,
        onFailure: suspend () -> Unit,
    ) {
        if (receiveSms == this.receiveSms.value && receiveEmails == this.receiveEmails.value) return

        this.receiveSms.value = receiveSms
        this.receiveEmails.value = receiveEmails

        viewModelScope.launch {
            val params = UpdateUserNotificationsSettingsUseCase.Params(
                receiveSms = receiveSms,
                receiveEmails = receiveEmails,
            )
            deps.updateUserNotificationsSettings(params)
                .onFailure {
                    val text = Text.Resource(R.string.profile_notification_settings_updating_error)
                    showZarinaErrorToast(text)
                    onFailure()
                }
        }
    }

    private fun updateFieldsWithUser(user: User) {
        firstNameTextFieldState.setTextAndPlaceCursorAtEnd(user.firstName.orEmpty())
        lastNameTextFieldState.setTextAndPlaceCursorAtEnd(user.lastName.orEmpty())
        val birthDateEpochMillis = user.birthDate?.toEpochMillis()
        birthDateEpochMillisValueHolder.set(birthDateEpochMillis)
        phone.value = user.phone
        email.value = user.email
        receiveEmails.value = user.notificationSettings.receiveEmails
        receiveSms.value = user.notificationSettings.receiveSms
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(ProfileDetailsSideEffect.ShowZarinaToast(message))
    }

    private enum class UserRequest : FlowRequest { LOADING, REFRESHING }

    private enum class Operation : OperationKey { SIGN_OUT, DELETE_ACCOUNT }

    private enum class Keys {
        BIRTH_DATE_EPOCH_MILLIS;

        val key: String = name
    }

    private companion object {
        private const val NOTIFICATIONS_SETTINGS_RESET_DELAY_MILLIS = 50L
        private const val TAG = "ProfileDetailsViewModel"
    }
}
