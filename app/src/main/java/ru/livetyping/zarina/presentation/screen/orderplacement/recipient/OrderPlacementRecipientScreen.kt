package ru.livetyping.zarina.presentation.screen.orderplacement.recipient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaPhoneNumberTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.orderplacement.common.OrderPlacementComponents
import ru.livetyping.zarina.presentation.screen.orderplacement.recipient.OrderPlacementRecipientViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun OrderPlacementRecipientScreen(
    navigate: (OrderPlacementRecipientScreenAction) -> Unit,
    viewModel: OrderPlacementRecipientViewModel = hiltViewModel(),
) {
    val phone by viewModel.phone.collectAsStateWithLifecycle()

    ScreenContent(
        firstNameTextFieldState = viewModel.firstNameTextFieldState,
        lastNameTextFieldState = viewModel.lastNameTextFieldState,
        phone = phone,
        onPhoneChanged = viewModel::onPhoneChanged,
        emailTextFieldState = viewModel.emailTextFieldState,
        onContinueClicked = viewModel::onContinueClicked,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    firstNameTextFieldState: TextFieldState,
    lastNameTextFieldState: TextFieldState,
    phone: String,
    onPhoneChanged: (String) -> Unit,
    emailTextFieldState: TextFieldState,
    onContinueClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (OrderPlacementRecipientScreenAction) -> Unit,
) {
    OrderPlacementRecipientScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .imePadding(),
    ) {
        OrderPlacementComponents.TopBar(
            title = stringResource(R.string.recipient),
            step = 1, // TODO: [High] Implement
            stepCount = 4, // TODO: [High] Implement
            isBackButtonVisible = false,
            onCloseClicked = onCloseClicked,
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.personal_data),
                style = UiKitTheme.typography.secondary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaTextField(
                state = lastNameTextFieldState,
                label = {
                    Text(text = stringResource(R.string.last_name))
                },
                placeholder = {
                    Text(text = stringResource(R.string.last_name_text_field_placeholder))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = lastNameTextFieldState.text.isNotEmpty(),
                        onClick = { lastNameTextFieldState.clearText() },
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaTextField(
                state = firstNameTextFieldState,
                label = {
                    Text(text = stringResource(R.string.first_name))
                },
                placeholder = {
                    Text(text = stringResource(R.string.first_name_text_field_placeholder))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = firstNameTextFieldState.text.isNotEmpty(),
                        onClick = { firstNameTextFieldState.clearText() },
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.contacts),
                style = UiKitTheme.typography.secondary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaPhoneNumberTextField(
                phoneNumber = phone,
                onPhoneNumberChanged = onPhoneChanged,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaTextField(
                state = emailTextFieldState,
                label = {
                    Text(text = stringResource(R.string.email))
                },
                placeholder = {
                    Text(text = stringResource(R.string.email_text_field_placeholder))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = emailTextFieldState.text.isNotEmpty(),
                        onClick = { firstNameTextFieldState.clearText() },
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(32.dp))

            Spacer(modifier = Modifier.weight(1f))
            ZarinaButton(
                onClick = onContinueClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.continue_).uppercase())
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            firstNameTextFieldState = rememberTextFieldState(),
            lastNameTextFieldState = rememberTextFieldState(),
            phone = "",
            onPhoneChanged = {},
            emailTextFieldState = rememberTextFieldState(),
            onContinueClicked = {},
            onCloseClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
