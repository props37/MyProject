package ru.livetyping.zarina.ui.screen.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.ui.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.ui.common.component.text.ZarinaClickableText
import ru.livetyping.zarina.ui.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.navigationBarsOrIme

object SignInScreenComponents {

    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            modifier = modifier,
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = stringResource(R.string.sign_in_to_account),
                    style = UiKitTheme.typography.primary.regular,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
        )
    }

    @Composable
    fun SignInBlock(
        onSignInClicked: () -> Unit,
        onSignUpClicked: () -> Unit,
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaButton(
                onClick = onSignInClicked,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.sign_in).uppercase())
            }

            Spacer(modifier = Modifier.height(16.dp))

            Policies(onUrlClicked = onUrlClicked)
            
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = stringResource(R.string.do_not_have_account_yet_question),
                style = UiKitTheme.typography.tertiary.regular,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            ZarinaButton(
                onClick = onSignUpClicked,
                colors = ZarinaButtonDefaults.outlineColors(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.sign_up).uppercase())
            }

            Spacer(modifier = Modifier.height(20.dp))
            val navigationBarsOrImeBottomPadding =
                WindowInsets.navigationBarsOrIme.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(navigationBarsOrImeBottomPadding))
        }
    }

    @Composable
    private fun Policies(
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val privacyPolicy = stringResource(R.string.sign_in_policies_privacy_policy)
        val recaptchaPrivacy = stringResource(R.string.sign_up_recaptcha_policies_privacy)
        val recaptchaTerms = stringResource(R.string.sign_up_recaptcha_policies_terms)

        val privacyPolicyUrl = stringResource(R.string.privacy_policy_url)
        val recaptchaPrivacyUrl = stringResource(R.string.recaptcha_policies_privacy_url)
        val recaptchaTermsUrl = stringResource(R.string.recaptcha_policies_terms_url)

        val clickableTextToUrl = remember(
            privacyPolicy,
            recaptchaPrivacy,
            recaptchaTerms,
            privacyPolicyUrl,
            recaptchaPrivacyUrl,
            recaptchaTermsUrl,
        ) {
            mapOf(
                privacyPolicy to privacyPolicyUrl,
                recaptchaPrivacy to recaptchaPrivacyUrl,
                recaptchaTerms to recaptchaTermsUrl,
            )
        }

        ZarinaClickableText(
            baseText = stringResource(R.string.sign_in_policies),
            clickableTextToUrl = clickableTextToUrl,
            onUrlClicked = onUrlClicked,
            modifier = modifier,
        )
    }

    val TopPadding: Dp get() = 32.dp
}
