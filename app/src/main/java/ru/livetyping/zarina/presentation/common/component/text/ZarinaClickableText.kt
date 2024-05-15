package ru.livetyping.zarina.presentation.common.component.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.text.addStyles
import ru.livetyping.zarina.util.compose.text.addUrlAnnotations

@OptIn(ExperimentalTextApi::class)
@Composable
fun ZarinaClickableText(
    baseText: String,
    clickableTextToUrl: Map<String, String>,
    onUrlClicked: (Url) -> Unit,
    modifier: Modifier = Modifier,
    baseTextStyle: TextStyle = BaseTextStyle,
    clickableTextStyle: TextStyle = ClickableTextStyle,
) {
    val text = remember(baseText, clickableTextToUrl, baseTextStyle, clickableTextStyle) {
        val clickableTextSpanStyle = clickableTextStyle.toSpanStyle()
        val clickableTextSpanStyles = listOf(clickableTextSpanStyle)
        val clickableTextToSpanStyles = clickableTextToUrl.mapValues { (_, _) ->
            clickableTextSpanStyles
        }
        val clickableTextToUrlAnnotations = clickableTextToUrl.mapValues { (_, url) ->
            listOf(UrlAnnotation(url))
        }

        buildAnnotatedString {
            withStyle(baseTextStyle.toSpanStyle()) {
                append(baseText)
            }
            addStyles(clickableTextToSpanStyles)
            addUrlAnnotations(clickableTextToUrlAnnotations)
        }
    }

    ClickableText(
        text = text,
        onClick = { offset ->
            val annotation = text.getUrlAnnotations(offset, offset).firstOrNull()
            if (annotation != null) {
                val url = Url(annotation.item.url)
                onUrlClicked(url)
            }
        },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        val privacy = stringResource(R.string.product_subscription_policies_privacy)
        val onlineStore = stringResource(R.string.product_subscription_policies_online_store)
        val personalData = stringResource(R.string.product_subscription_policies_personal_data)

        val privacyUrl = stringResource(R.string.privacy_policy_url)
        val onlineStoreUrl = stringResource(R.string.online_store_policy_url)
        val personalDataUrl = stringResource(R.string.personal_data_policy_url)

        val clickableTextToUrl = remember(
            privacy,
            onlineStore,
            personalData,
            privacyUrl,
            onlineStoreUrl,
            personalDataUrl,
        ) {
            mapOf(
                privacy to privacyUrl,
                onlineStore to onlineStoreUrl,
                personalData to personalDataUrl,
            )
        }

        ZarinaClickableText(
            baseText = stringResource(R.string.product_subscription_policies),
            clickableTextToUrl = clickableTextToUrl,
            onUrlClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}

private val TextColor: Color
    @Composable
    get() = UiKitTheme.colors.text.general.regular.default

private val BaseTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.footnote.light.copy(color = TextColor)

private val ClickableTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.footnote.regular.copy(color = TextColor)
