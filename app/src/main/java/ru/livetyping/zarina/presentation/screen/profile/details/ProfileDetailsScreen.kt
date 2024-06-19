package ru.livetyping.zarina.presentation.screen.profile.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsScreenComponents.ContactsBlock
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsScreenComponents.PersonalDataBlock
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsScreenComponents.SettingsBlock
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsViewModel.SideEffect
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsViewModel.State
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade

@Composable
fun ProfileDetailsScreen(
    navigate: (ProfileDetailsScreenAction) -> Unit,
    viewModel: ProfileDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val birthDateMillis by viewModel.birthDateMillis.collectAsStateWithLifecycle()
    val phoneNumber by viewModel.phoneNumber.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val receiveNewsByEmail by viewModel.receiveNewsByEmail.collectAsStateWithLifecycle()
    val receiveSmsNotifications by viewModel.receiveSmsNotifications.collectAsStateWithLifecycle()

    ScreenContent(
        state = state,
        lastNameTextFieldState = viewModel.lastNameTextFieldState,
        firstNameTextFieldState = viewModel.firstNameTextFieldState,
        birthDateMillis = birthDateMillis,
        phoneNumber = phoneNumber,
        onPhoneNumberClicked = viewModel::onPhoneNumberClicked,
        email = email,
        onEmailClicked = viewModel::onEmailClicked,
        receiveNewsByEmail = receiveNewsByEmail,
        onReceiveNewsByEmailChanged = viewModel::onReceiveNewsByEmailChanged,
        receiveSmsNotifications = receiveSmsNotifications,
        onReceiveSmsNotificationsChanged = viewModel::onReceiveSmsNotificationsChanged,
        onChangePasswordClicked = viewModel::onChangePasswordClicked,
        onRemoteUserErrorRefreshClicked = viewModel::onRemoteUserErrorRefreshClicked,
        onSignOutClicked = viewModel::onSignOutClicked,
        onDeleteAccountClicked = viewModel::onDeleteAccountClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    state: State,
    lastNameTextFieldState: TextFieldState,
    firstNameTextFieldState: TextFieldState,
    birthDateMillis: Long?,
    phoneNumber: String?,
    onPhoneNumberClicked: () -> Unit,
    email: String,
    onEmailClicked: () -> Unit,
    receiveNewsByEmail: Boolean,
    onReceiveNewsByEmailChanged: (Boolean) -> Unit,
    receiveSmsNotifications: Boolean,
    onReceiveSmsNotificationsChanged: (Boolean) -> Unit,
    onChangePasswordClicked: () -> Unit,
    onRemoteUserErrorRefreshClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProfileDetailsScreenAction) -> Unit,
) {
    ProfileDetailsScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

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
        TopBar(onBackClicked = onBackClicked)

        Crossfade(
            targetState = state,
            contentKey = {
                // TODO: [High] Extract
                when (it) {
                    is State.Success -> "Success"
                    State.Loading, is State.Error -> it
                }
            },
            modifier = Modifier.fillMaxSize(),
        ) { state ->
            when (state) {
                is State.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        PersonalDataBlock(
                            lastNameTextFieldState = lastNameTextFieldState,
                            firstNameTextFieldState = firstNameTextFieldState,
                            birthDateMillis = birthDateMillis,
                            onBirthDateClicked = {}, // TODO: [High] Implement
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ContactsBlock(
                            phoneNumber = phoneNumber,
                            onPhoneNumberClicked = onPhoneNumberClicked,
                            email = email,
                            onEmailClicked = onEmailClicked,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SettingsBlock(
                            onChangePasswordClicked = onChangePasswordClicked,
                            receiveNewsByEmail = receiveNewsByEmail,
                            onReceiveNewsByEmailChanged = onReceiveNewsByEmailChanged,
                            receiveSmsNotifications = receiveSmsNotifications,
                            onReceiveSmsNotificationsChanged = onReceiveSmsNotificationsChanged,
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        ZarinaButton(
                            onClick = onSignOutClicked,
                            colors = ZarinaButtonDefaults.outlineColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        ) {
                            Text(text = stringResource(R.string.sign_out).uppercase())
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        ZarinaButton(
                            onClick = onDeleteAccountClicked,
                            colors = ZarinaButtonDefaults.backlessErrorColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        ) {
                            Text(text = stringResource(R.string.delete_account).uppercase())
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                State.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        ZarinaCircularLoader(modifier = Modifier.size(40.dp))
                    }
                }

                is State.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onRemoteUserErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
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
