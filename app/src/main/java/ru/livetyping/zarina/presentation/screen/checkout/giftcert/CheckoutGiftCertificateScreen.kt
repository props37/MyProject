package ru.livetyping.zarina.presentation.screen.checkout.giftcert

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.byValue
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.giftcert.GiftCertificate
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.checkout.giftcert.CheckoutGiftCertificateScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.checkout.giftcert.CheckoutGiftCertificateViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun CheckoutGiftCertificateScreen(
    navigate: (CheckoutGiftCertificateScreenAction) -> Unit,
    viewModel: CheckoutGiftCertificateViewModel = hiltViewModel(),
) {
    val isApplyButtonLoading by viewModel.isApplyButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        isApplyButtonLoading = isApplyButtonLoading,
        giftCertificateNumberTextFieldState = viewModel.giftCertificateNumberTextFieldState,
        giftCertificateVerificationCodeTextFieldState = viewModel.giftCertificateVerificationCodeTextFieldState,
        onApplyClicked = viewModel::onApplyClicked,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    isApplyButtonLoading: Boolean,
    giftCertificateNumberTextFieldState: TextFieldState,
    giftCertificateVerificationCodeTextFieldState: TextFieldState,
    onApplyClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutGiftCertificateScreenAction) -> Unit,
) {
    CheckoutGiftCertificateScreenBehavior(
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
            ),
    ) {
        TopBar(onCloseClicked = onCloseClicked)

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.gift_certificate_description),
                style = UiKitTheme.typography.tertiary.light,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaTextField(
                state = giftCertificateNumberTextFieldState,
                label = {
                    ZarinaTextFieldDefaults.AppearingLabel(
                        textFieldValue = giftCertificateNumberTextFieldState.text,
                        label = stringResource(R.string.certificate_number),
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.certificate_number),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = giftCertificateNumberTextFieldState.text.isNotEmpty(),
                        onClick = giftCertificateNumberTextFieldState::clearText,
                    )
                },
                inputTransformation = InputTransformation
                    .byValue { _, proposed ->
                        proposed.filter { it.isDigit() }
                    }
                    .maxLength(GiftCertificate.GIFT_CERTIFICATE_MAX_LENGTH),
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            ZarinaTextField(
                state = giftCertificateVerificationCodeTextFieldState,
                label = {
                    ZarinaTextFieldDefaults.AppearingLabel(
                        textFieldValue = giftCertificateVerificationCodeTextFieldState.text,
                        label = stringResource(R.string.verification_code),
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.verification_code),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = giftCertificateVerificationCodeTextFieldState.text.isNotEmpty(),
                        onClick = giftCertificateVerificationCodeTextFieldState::clearText,
                    )
                },
                inputTransformation = InputTransformation
                    .byValue { _, proposed ->
                        proposed.filter { it.isDigit() }
                    }
                    .maxLength(GiftCertificate.GIFT_CERTIFICATE_VERIFICATION_CODE_MAX_LENGTH),
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    )
                },
                onKeyboardAction = { default ->
                    onApplyClicked()
                    default()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(32.dp))

            ZarinaButton(
                onClick = onApplyClicked,
                isLoading = isApplyButtonLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.apply).uppercase())
            }

            val navigationBarHeight =
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(navigationBarHeight + 20.dp))
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // Add preview
    }
}
