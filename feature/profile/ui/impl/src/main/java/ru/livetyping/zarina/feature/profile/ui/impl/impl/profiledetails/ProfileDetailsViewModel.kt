package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.kotlinutil.LocalDateUtil
import ru.livetyping.zarina.core.kotlinutil.toEpochMillis
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsTopBarEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsTopBarState
import javax.inject.Inject

@HiltViewModel
internal class ProfileDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserFlow: GetUserFlowUseCase,
) : ViewModel(), SideEffectSource<ProfileDetailsSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val userParams = GetUserFlowUseCase.Params(CachePolicy.Remote())
    private val userRequester = FlowRequester<Result<User?>, UserRequest> {
        getUserFlow(userParams)
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

    private val userResultFlow = userRequester.flow
        .onEach { result ->
            val user = result.getOrNull()
            if (user != null) {
                updateFieldsWithUser(user)
            }
        }

    val profileDetailsState: StateFlow<ProfileDetailsState> = combine(
        userResultFlow,
        userRequester.loadingState,
        birthDateEpochMillisValueHolder.stateFlow,
        phone,
        email,
    ) { userResult, loadingState, birthDateEpochMillis, phone, email ->
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
                        receiveEmails = receiveEmails.value,
                        receiveSms = receiveSms.value,
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

    val topBarState: StateFlow<ProfileDetailsTopBarState> = TODO()

    fun onTopBarEvent(event: ProfileDetailsTopBarEvent) {
        when (event) {
            ProfileDetailsTopBarEvent.BackClicked -> onBackClicked()
            ProfileDetailsTopBarEvent.SaveClicked -> TODO()
        }
    }

    fun onProfileDetailsEvent(event: ProfileDetailsEvent) {
        when (event) {
            is ProfileDetailsEvent.BirthDateEpochMillisChanged -> TODO()
            ProfileDetailsEvent.EmailClicked -> TODO()
            ProfileDetailsEvent.PhoneClicked -> TODO()
            ProfileDetailsEvent.ChangePasswordClicked -> TODO()
            is ProfileDetailsEvent.ReceiveEmailsChanged -> TODO()
            is ProfileDetailsEvent.ReceiveSmsChanged -> TODO()
            ProfileDetailsEvent.DeleteAccountClicked -> TODO()
            ProfileDetailsEvent.SignOutClicked -> TODO()
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

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProfileDetailsScreenAction.BackClicked
            emitSideEffect(ProfileDetailsSideEffect.Navigate(action))
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

    private enum class UserRequest : FlowRequest { LOADING, REFRESHING }

    private enum class Keys {
        BIRTH_DATE_EPOCH_MILLIS;

        val key: String = name
    }
}
