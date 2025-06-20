package ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.byValue
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.giftcert.GiftCertificate
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert.model.GiftCertificateState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun GiftCertificateInput(
    state: GiftCertificateState,
    onApplyClicked: () -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.cart_gift_certificate_description).uppercase(),
            style = UiKitTheme2.typography.body2,
            color = UiKitTheme2.colors.mainBlack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))

        ZarinaTextField(
            state = state.giftCertificateNumberTextFieldState,
            isError = state.isGiftCertificateNumberInvalid,
            label = {
                ZarinaTextFieldDefaults.AppearingLabel(
                    textFieldValue = state.giftCertificateNumberTextFieldState.text.toString(),
                    label = stringResource(R.string.cart_certificate_number),
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.cart_certificate_number).uppercase(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            innerTrailingContent = {
                ZarinaTextFieldDefaults.ClearButton(
                    isVisible = state.giftCertificateNumberTextFieldState.text.isNotEmpty(),
                    onClick = state.giftCertificateNumberTextFieldState::clearText,
                )
            },
            inputTransformation = InputTransformation
                .byValue { _, proposed ->
                    proposed.filter { it.isDigit() }
                }
                .maxLength(GiftCertificate.NUMBER_MAX_LENGTH),
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
            state = state.giftCertificateVerificationCodeTextFieldState,
            isError = state.isGiftCertificateVerificationCodeInvalid,
            label = {
                ZarinaTextFieldDefaults.AppearingLabel(
                    textFieldValue = state.giftCertificateVerificationCodeTextFieldState.text.toString(),
                    label = stringResource(R.string.cart_verification_code),
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.cart_verification_code).uppercase(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            innerTrailingContent = {
                ZarinaTextFieldDefaults.ClearButton(
                    isVisible = state.giftCertificateVerificationCodeTextFieldState.text.isNotEmpty(),
                    onClick = state.giftCertificateVerificationCodeTextFieldState::clearText,
                )
            },
            inputTransformation = InputTransformation
                .byValue { _, proposed ->
                    proposed.filter { it.isDigit() }
                }
                .maxLength(GiftCertificate.VERIFICATION_CODE_MAX_LENGTH),
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
            isLoading = state.isApplyButtonLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(text = stringResource(RCommon.string.res_apply).uppercase())
        }

        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        Spacer(modifier = Modifier.windowInsetsBottomHeight(windowInsetsProvider()))
    }
}
