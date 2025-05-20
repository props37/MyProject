package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicommon.openUrlInCustomTabs
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uicompose.text.rememberAnnotatedStringWithLinks
import ru.livetyping.zarina.core.uikit.checkbox.ZarinaCheckbox
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.signup.ui.impl.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Policies(
    areAccepted: Boolean,
    onAcceptedChanged: (Boolean) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        PoliciesText(modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.width(16.dp))

        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            ZarinaCheckbox(
                isChecked = areAccepted,
                onCheckedChanged = onAcceptedChanged,
                isError = isError,
            )
        }
    }
}

@Suppress("NAME_SHADOWING")
@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SubscriptionPolicy(
    isVisible: Boolean,
    isAccepted: Boolean,
    onAcceptedChanged: (Boolean) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    AnimatedContent(
        targetState = isVisible,
        transitionSpec = {
            AnimatedContentDefaultTransitionSpec.using(SizeTransform(clip = false))
        },
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) { isVisible ->
        if (isVisible) {
            Row(modifier = Modifier.padding(contentPadding)) {
                SubscriptionPolicyText(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.width(16.dp))

                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    ZarinaCheckbox(
                        isChecked = isAccepted,
                        onCheckedChanged = onAcceptedChanged,
                        isError = isError,
                    )
                }
            }
        }
    }
}

@Composable
private fun PoliciesText(
    modifier: Modifier = Modifier,
) {
    val currentContext by rememberUpdatedState(LocalContext.current)

    val privacy = stringResource(R.string.sign_up_policies_privacy)
    val onlineStore = stringResource(R.string.sign_up_policies_online_store)
    val loyalty = stringResource(R.string.sign_up_policies_loyalty)

    val privacyUrl = stringResource(ru.livetyping.zarina.core.resource.R.string.res_zarina_privacy_policy_url)
    val onlineStoreUrl = stringResource(ru.livetyping.zarina.core.resource.R.string.res_zarina_online_store_policy_url)
    val loyaltyUrl = stringResource(ru.livetyping.zarina.core.resource.R.string.res_zarina_loyalty_policy_url)

    val substringToUrl = remember(
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
    val stringWithLinks = rememberAnnotatedStringWithLinks(
        baseString = stringResource(R.string.sign_up_policies),
        substringToUrl = substringToUrl,
        linkStyle = UiKitTheme.typography.footnote.regular.toSpanStyle()
            .copy(textDecoration = TextDecoration.Underline),
        onUrlClicked = currentContext::openUrlInCustomTabs,
    )

    Text(
        text = stringWithLinks,
        style = UiKitTheme.typography.footnote.light,
        color = UiKitTheme.colors.text.general.regular.default,
        modifier = modifier,
    )
}

@Composable
private fun SubscriptionPolicyText(
    modifier: Modifier = Modifier,
) {
    val currentContext by rememberUpdatedState(LocalContext.current)

    val personalData = stringResource(R.string.sign_up_subscription_policy_agreement)
    val personalDataUrl = stringResource(ru.livetyping.zarina.core.resource.R.string.res_zarina_personal_data_policy_url)

    val substringToUrl = remember(personalData, personalDataUrl) {
        mapOf(personalData to personalDataUrl)
    }
    val stringWithLinks = rememberAnnotatedStringWithLinks(
        baseString = stringResource(R.string.sign_up_subscription_policy),
        substringToUrl = substringToUrl,
        linkStyle = UiKitTheme.typography.footnote.regular.toSpanStyle()
            .copy(textDecoration = TextDecoration.Underline),
        onUrlClicked = currentContext::openUrlInCustomTabs,
    )

    Text(
        text = stringWithLinks,
        style = UiKitTheme.typography.footnote.light,
        color = UiKitTheme.colors.text.general.regular.default,
        modifier = modifier,
    )
}
