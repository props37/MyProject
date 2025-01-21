package ru.livetyping.zarina.presentation.common.component.policy

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.text.rememberStringWithLinks

@Composable
fun YandexCaptchaPolicy(
    modifier: Modifier = Modifier,
) {
    val currentContext by rememberUpdatedState(LocalContext.current)

    val terms = stringResource(R.string.yandex_captcha_terms_policy)
    val termsUrl = stringResource(R.string.yandex_captcha_terms_policy_url)
    val substringToUrl = remember(terms, termsUrl) {
        mapOf(terms to termsUrl)
    }
    val linkStyle = UiKitTheme.typography.footnote.regular.copy(
        color = UiKitTheme.colors.text.general.regular.default,
    )

    val textWithLinks = rememberStringWithLinks(
        baseString = stringResource(R.string.yandex_captcha_policies),
        substringToUrl = substringToUrl,
        urlStyle = linkStyle.toSpanStyle(),
        onUrlClicked = { url ->
            val intent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()
            intent.launchUrl(currentContext, url.toUri())
        },
    )

    Text(
        text = textWithLinks,
        style = UiKitTheme.typography.footnote.light,
        color = UiKitTheme.colors.text.general.regular.muted,
        modifier = modifier,
    )
}
