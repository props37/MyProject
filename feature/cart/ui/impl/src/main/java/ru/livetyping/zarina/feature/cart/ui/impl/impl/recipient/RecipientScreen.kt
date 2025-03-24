package ru.livetyping.zarina.feature.cart.ui.impl.impl.recipient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.text.ZarinaPhoneTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.recipient.model.RecipientState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun RecipientScreen(
    navActions: RecipientNavActions,
    viewModel: RecipientViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val recipientState by viewModel.recipientState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        recipientState = recipientState,
        onContinueClicked = viewModel::onContinueClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    topBarState: CheckoutTopBarState,
    onTopBarEvent: (CheckoutTopBarEvent) -> Unit,
    recipientState: RecipientState,
    onContinueClicked: () -> Unit,
    sideEffects: Flow<RecipientSideEffect>,
    navActions: RecipientNavActions,
) {
    RecipientScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
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
        CheckoutTopBar(
            state = topBarState,
            title = stringResource(R.string.cart_recipient),
            onEvent = onTopBarEvent,
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.cart_personal_data),
                style = UiKitTheme.typography.secondary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            val lastNameTextFieldState = recipientState.lastNameTextFieldState
            ZarinaTextField(
                state = lastNameTextFieldState,
                isError = recipientState.isLastNameInvalid,
                label = {
                    val label = if (lastNameTextFieldState.text.isNotEmpty()) {
                        stringResource(RCommon.string.res_last_name)
                    } else ""
                    Text(label)
                },
                placeholder = {
                    Text(text = stringResource(RCommon.string.res_last_name))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = lastNameTextFieldState.text.isNotEmpty(),
                        onClick = { lastNameTextFieldState.clearText() },
                    )
                },
                keyboardOptions = remember {
                    KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next,
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .semantics { contentType = ContentType.PersonLastName },
            )
            Spacer(modifier = Modifier.height(16.dp))

            val firstNameTextFieldState = recipientState.firstNameTextFieldState
            ZarinaTextField(
                state = firstNameTextFieldState,
                isError = recipientState.isFirstNameInvalid,
                label = {
                    val label = if (firstNameTextFieldState.text.isNotEmpty()) {
                        stringResource(RCommon.string.res_first_name)
                    } else ""
                    Text(label)
                },
                placeholder = {
                    Text(text = stringResource(RCommon.string.res_first_name))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = firstNameTextFieldState.text.isNotEmpty(),
                        onClick = { firstNameTextFieldState.clearText() },
                    )
                },
                keyboardOptions = remember {
                    KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next,
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .semantics { contentType = ContentType.PersonFirstName },
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.cart_contacts),
                style = UiKitTheme.typography.secondary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            val phoneTextFieldState = recipientState.phoneTextFieldState
            ZarinaPhoneTextField(
                state = phoneTextFieldState,
                isError = recipientState.isPhoneInvalid,
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next,
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .semantics { contentType = ContentType.PhoneNumber },
            )
            Spacer(modifier = Modifier.height(16.dp))

            val emailTextFieldState = recipientState.emailTextFieldState
            ZarinaTextField(
                state = emailTextFieldState,
                isError = recipientState.isEmailInvalid,
                label = {
                    val label = if (emailTextFieldState.text.isNotEmpty()) {
                        stringResource(RCommon.string.res_email)
                    } else ""
                    Text(label)
                },
                placeholder = {
                    Text(text = stringResource(RCommon.string.res_email))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = emailTextFieldState.text.isNotEmpty(),
                        onClick = { emailTextFieldState.clearText() },
                    )
                },
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done,
                    )
                },
                onKeyboardAction = { onContinueClicked() },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .semantics { contentType = ContentType.EmailAddress },
            )
            Spacer(modifier = Modifier.height(32.dp))

            Spacer(modifier = Modifier.weight(1f))
            ZarinaButton(
                onClick = onContinueClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(RCommon.string.res_continue).uppercase())
            }

            val navigationBarHeight =
                WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(navigationBarHeight))
            Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        }
    }
}
