package ru.livetyping.zarina.core.uikit.captcha

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import ru.livetyping.zarina.core.uicommon.openUrlInCustomTabs
import ru.livetyping.zarina.core.uicompose.text.rememberAnnotatedStringWithLinks
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
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
    val linkStyle = UiKitTheme2.typography.body2

    val textWithLinks = rememberAnnotatedStringWithLinks(
        baseString = stringResource(RCommon.string.res_yandex_captcha_policies),
        substringToUrl = substringToUrl,
        linkStyle = linkStyle.toSpanStyle().copy(textDecoration = TextDecoration.Underline),
        onUrlClicked = currentContext::openUrlInCustomTabs,
    )

    Text(
        text = textWithLinks.toUpperCase(),
        style = UiKitTheme2.typography.body2,
        color = UiKitTheme2.colors.mainBlack,
        modifier = modifier,
    )
}
