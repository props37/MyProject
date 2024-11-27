package ru.livetyping.zarina.core.uikit.captcha

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.uicommon.openUrlInCustomTabs
import ru.livetyping.zarina.core.uicompose.rememberAnnotatedStringWithLinks
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
public fun YandexCaptchaPolicies(
    modifier: Modifier = Modifier,
) {
    val currentContext by rememberUpdatedState(LocalContext.current)
    
    val terms = stringResource(RCommon.string.res_yandex_captcha_terms_policy)
    val termsUrl = stringResource(RCommon.string.res_yandex_captcha_terms_policy_url)
    val substringToUrl = remember(terms, termsUrl) {
        mapOf(terms to termsUrl)
    }
    val linkStyle = UiKitTheme.typography.footnote.regular.copy(
        color = UiKitTheme.colors.text.general.regular.default,
    )

    val textWithLinks = rememberAnnotatedStringWithLinks(
        baseString = stringResource(RCommon.string.res_yandex_captcha_policies),
        substringToUrl = substringToUrl,
        urlStyle = linkStyle.toSpanStyle(),
        onUrlClicked = currentContext::openUrlInCustomTabs,
    )

    Text(
        text = textWithLinks,
        style = UiKitTheme.typography.footnote.light,
        color = UiKitTheme.colors.text.general.regular.muted,
        modifier = modifier,
    )
}
