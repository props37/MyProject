package ru.livetyping.zarina.presentation.common.component.policy

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.text.rememberStringWithLinks

@Composable
fun RecaptchaPolicy(
    onUrlClicked: (Url) -> Unit,
    modifier: Modifier = Modifier,
) {
    val privacy = stringResource(R.string.sign_up_recaptcha_policies_privacy)
    val terms = stringResource(R.string.sign_up_recaptcha_policies_terms)

    val privacyUrl = stringResource(R.string.recaptcha_policies_privacy_url)
    val termsUrl = stringResource(R.string.recaptcha_policies_terms_url)

    val substringToUrl = remember(privacy, terms, privacyUrl, termsUrl) {
        mapOf(
            privacy to privacyUrl,
            terms to termsUrl,
        )
    }
    val linkStyle = UiKitTheme.typography.footnote.regular.copy(
        color = UiKitTheme.colors.text.general.regular.default,
    )
    val stringWithLinks = rememberStringWithLinks(
        baseString = stringResource(R.string.sign_up_recaptcha_policies),
        substringToUrl = substringToUrl,
        urlStyle = linkStyle.toSpanStyle(),
        onUrlClicked = { onUrlClicked(Url(it)) },
    )

    Text(
        text = stringWithLinks,
        style = UiKitTheme.typography.footnote.light,
        color = UiKitTheme.colors.text.general.regular.muted,
        modifier = modifier,
    )
}
