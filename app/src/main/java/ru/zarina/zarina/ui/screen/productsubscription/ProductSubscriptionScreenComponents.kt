package ru.zarina.zarina.ui.screen.productsubscription

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.ui.common.component.ZarinaCheckbox
import ru.zarina.zarina.ui.common.component.button.BackIconButton
import ru.zarina.zarina.ui.common.component.topbar.TopBarDefaults
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.addStyles
import ru.zarina.zarina.util.compose.addUrlAnnotations

object ProductSubscriptionScreenComponents {

    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                BackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = stringResource(R.string.product_subscription),
                    style = UiKitTheme.typographyReworked.primary.regular,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
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
            )
        }
    }

    @OptIn(ExperimentalTextApi::class)
    @Composable
    fun PoliciesText(
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val baseTextStyle = UiKitTheme.typographyReworked.footnote.light.copy(
            color = UiKitTheme.colorsReworked.text.general.regular.default,
        )
        val boldTextStyle = UiKitTheme.typographyReworked.footnote.bold.copy(
            color = UiKitTheme.colorsReworked.text.general.regular.default,
        )

        val baseText = stringResource(R.string.product_subscription_policies)
        val privacyPolicyText = stringResource(R.string.product_subscription_policies_privacy)
        val onlineStorePolicyText =
            stringResource(R.string.product_subscription_policies_online_store)
        val personalDataPolicyText =
            stringResource(R.string.product_subscription_policies_personal_data)

        val privacyPolicyUrl = stringResource(R.string.product_subscription_policies_privacy_url)
        val onlineStorePolicyUrl =
            stringResource(R.string.product_subscription_policies_online_store_url)
        val personalDataPolicyUrl =
            stringResource(R.string.product_subscription_policies_personal_data_url)

        val text = remember(
            baseTextStyle,
            boldTextStyle,
            baseText,
            privacyPolicyText,
            onlineStorePolicyText,
            personalDataPolicyText,
            privacyPolicyUrl,
            onlineStorePolicyUrl,
            personalDataPolicyUrl,
        ) {
            val clickableTextStyle = boldTextStyle.toSpanStyle()
            val substringToStyles = mapOf(
                privacyPolicyText to listOf(clickableTextStyle),
                onlineStorePolicyText to listOf(clickableTextStyle),
                personalDataPolicyText to listOf(clickableTextStyle),
            )
            val substringToUrlAnnotations = mapOf(
                privacyPolicyText to listOf(UrlAnnotation(privacyPolicyUrl)),
                onlineStorePolicyText to listOf(UrlAnnotation(onlineStorePolicyUrl)),
                personalDataPolicyText to listOf(UrlAnnotation(personalDataPolicyUrl)),
            )

            buildAnnotatedString {
                withStyle(baseTextStyle.toSpanStyle()) {
                    append(baseText)
                }
                addStyles(substringToStyles)
                addUrlAnnotations(substringToUrlAnnotations)
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
}
