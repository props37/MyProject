package ru.livetyping.zarina.ui.screen.signup

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.ui.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.ui.common.component.checkbox.ZarinaCheckbox
import ru.livetyping.zarina.ui.common.component.text.ZarinaClickableText
import ru.livetyping.zarina.ui.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.ui.theme.UiKitTheme

object SignUpScreenComponents {

    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = stringResource(R.string.registration),
                    style = UiKitTheme.typography.primary.regular,
                )
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @Composable
    fun Policies(
        areAccepted: Boolean,
        onAcceptedChanged: (Boolean) -> Unit,
        isError: Boolean,
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(modifier = modifier) {
            PoliciesText(
                onUrlClicked = onUrlClicked,
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(16.dp))

            ZarinaCheckbox(
                isChecked = areAccepted,
                onCheckedChanged = onAcceptedChanged,
                isError = isError,
            )
        }
    }

    @Composable
    fun RecaptchaPolicies(
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val privacy = stringResource(R.string.sign_up_recaptcha_policies_privacy)
        val terms = stringResource(R.string.sign_up_recaptcha_policies_terms)

        val privacyUrl = stringResource(R.string.sign_up_recaptcha_policies_privacy_url)
        val termsUrl = stringResource(R.string.sign_up_recaptcha_policies_terms_url)

        val clickableTextToUrl = remember(privacy, terms, privacyUrl, termsUrl) {
            mapOf(
                privacy to privacyUrl,
                terms to termsUrl,
            )
        }

        ZarinaClickableText(
            baseText = stringResource(R.string.sign_up_recaptcha_policies),
            clickableTextToUrl = clickableTextToUrl,
            onUrlClicked = onUrlClicked,
            baseTextStyle = UiKitTheme.typography.footnote.light.copy(
                color = UiKitTheme.colors.text.general.regular.muted,
            ),
            modifier = modifier,
        )
    }

    @Composable
    private fun PoliciesText(
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val privacy = stringResource(R.string.sign_up_policies_privacy)
        val onlineStore = stringResource(R.string.sign_up_policies_online_store)
        val loyalty = stringResource(R.string.sign_up_policies_loyalty)

        val privacyUrl = stringResource(R.string.privacy_policy_url)
        val onlineStoreUrl = stringResource(R.string.online_store_policy_url)
        val loyaltyUrl = stringResource(R.string.loyalty_policy_url)

        val clickableTextToUrl = remember(
            privacy,
            onlineStore,
            loyalty,
            privacyUrl,
            onlineStoreUrl,
            loyaltyUrl,
        ) {
            mapOf(
                privacy to privacyUrl,
                onlineStore to onlineStoreUrl,
                loyalty to loyaltyUrl,
            )
        }

        ZarinaClickableText(
            baseText = stringResource(R.string.sign_up_policies),
            clickableTextToUrl = clickableTextToUrl,
            onUrlClicked = onUrlClicked,
            modifier = modifier,
        )
    }
}
