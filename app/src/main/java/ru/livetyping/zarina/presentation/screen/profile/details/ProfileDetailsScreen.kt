package ru.livetyping.zarina.presentation.screen.profile.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.component.datepicker.ZarinaDatePicker
import ru.livetyping.zarina.presentation.common.component.datepicker.ZarinaDatePickerDefaults
import ru.livetyping.zarina.presentation.common.component.datepicker.ZarinaDatePickerDialog
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsScreenComponents.DatePickerMinYear
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsScreenComponents.ProfileDetails
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsViewModel.SideEffect
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsViewModel.State
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import java.time.LocalDate

@Composable
fun ProfileDetailsScreen(
    navigate: (ProfileDetailsScreenAction) -> Unit,
    viewModel: ProfileDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val birthDateMillis by viewModel.birthDateMillis.collectAsStateWithLifecycle()
    val isBrithDateChangeable by viewModel.isBirthDateChangeable.collectAsStateWithLifecycle()
    val phoneNumber by viewModel.phoneNumber.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val receiveEmails by viewModel.receiveEmails.collectAsStateWithLifecycle()
    val receiveSms by viewModel.receiveSms.collectAsStateWithLifecycle()
    val isSaveUserInfoButtonVisible by viewModel.isSaveUserInfoButtonVisible.collectAsStateWithLifecycle()

    ScreenContent(
        isSaveUserInfoButtonVisible = isSaveUserInfoButtonVisible,
        onSaveUserInfoClicked = viewModel::onSaveUserInfoClicked,
        state = state,
        firstNameTextFieldState = viewModel.firstNameTextFieldState,
        lastNameTextFieldState = viewModel.lastNameTextFieldState,
        birthDateMillis = birthDateMillis,
        isBrithDateChangeable = isBrithDateChangeable,
        onBirthDateMillisChanged = viewModel::onBirthDateMillisChanged,
        phoneNumber = phoneNumber,
        onPhoneNumberClicked = viewModel::onPhoneNumberClicked,
        email = email,
        onEmailClicked = viewModel::onEmailClicked,
        receiveEmails = receiveEmails,
        onReceiveEmailsChanged = viewModel::onReceiveEmailsChanged,
        receiveSms = receiveSms,
        onReceiveSmsChanged = viewModel::onReceiveSmsChanged,
        onChangePasswordClicked = viewModel::onChangePasswordClicked,
        onRemoteUserErrorRefreshClicked = viewModel::onUserErrorRefreshClicked,
        onSignOutClicked = viewModel::onSignOutClicked,
        onDeleteAccountClicked = viewModel::onDeleteAccountClicked,
        onBackClicked = viewModel::onBackClicked,
        onUrlClicked = viewModel::onUrlClicked,
        onScreenOpened = viewModel::onScreenOpened,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    isSaveUserInfoButtonVisible: Boolean,
    onSaveUserInfoClicked: () -> Unit,
    state: State,
    firstNameTextFieldState: TextFieldState,
    lastNameTextFieldState: TextFieldState,
    birthDateMillis: Long?,
    isBrithDateChangeable: Boolean,
    onBirthDateMillisChanged: (Long?) -> Unit,
    phoneNumber: String?,
    onPhoneNumberClicked: () -> Unit,
    email: String,
    onEmailClicked: () -> Unit,
    receiveEmails: Boolean,
    onReceiveEmailsChanged: (Boolean) -> Unit,
    receiveSms: Boolean,
    onReceiveSmsChanged: (Boolean) -> Unit,
    onChangePasswordClicked: () -> Unit,
    onRemoteUserErrorRefreshClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onUrlClicked: (String) -> Unit,
    onScreenOpened: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProfileDetailsScreenAction) -> Unit,
) {
    ProfileDetailsScreenBehavior(
        onScreenOpened = onScreenOpened,
        sideEffects = sideEffects,
        navigate = navigate,
    )

    var isDatePickerVisible by remember { mutableStateOf(false) }
    if (isDatePickerVisible) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = birthDateMillis,
            yearRange = remember { DatePickerMinYear..LocalDate.now().year },
        )

        ZarinaDatePickerDialog(
            onDismissRequest = { isDatePickerVisible = false },
            confirmButton = {
                ZarinaDatePickerDefaults.ConfirmButton(
                    onClick = {
                        onBirthDateMillisChanged(datePickerState.selectedDateMillis)
                        isDatePickerVisible = false
                    },
                )
            },
        ) {
            ZarinaDatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TopBar(
            isSaveUserInfoButtonVisible = isSaveUserInfoButtonVisible,
            onSaveUserInfoClicked = onSaveUserInfoClicked,
            onBackClicked = onBackClicked,
        )

        ProfileDetails(
            state = state,
            firstNameTextFieldState = firstNameTextFieldState,
            lastNameTextFieldState = lastNameTextFieldState,
            birthDateMillis = birthDateMillis,
            isBrithDateChangeable = isBrithDateChangeable,
            onBirthDateMillisClicked = { isDatePickerVisible = true },
            phoneNumber = phoneNumber,
            onPhoneNumberClicked = onPhoneNumberClicked,
            email = email,
            onEmailClicked = onEmailClicked,
            receiveEmails = receiveEmails,
            onReceiveEmailsChanged = onReceiveEmailsChanged,
            receiveSms = receiveSms,
            onReceiveSmsChanged = onReceiveSmsChanged,
            onChangePasswordClicked = onChangePasswordClicked,
            onRemoteUserErrorRefreshClicked = onRemoteUserErrorRefreshClicked,
            onSignOutClicked = onSignOutClicked,
            onDeleteAccountClicked = onDeleteAccountClicked,
            onUrlClicked = onUrlClicked,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
