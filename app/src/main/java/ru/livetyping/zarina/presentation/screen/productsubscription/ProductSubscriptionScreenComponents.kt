package ru.livetyping.zarina.presentation.screen.productsubscription

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.checkbox.ZarinaCheckbox
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.text.rememberStringWithLinks

object ProductSubscriptionScreenComponents {

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
                    text = stringResource(R.string.product_subscription),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @OptIn(ExperimentalMaterialApi::class)
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

            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                ZarinaCheckbox(
                    isChecked = areAccepted,
                    onCheckedChanged = onAcceptedChanged,
                    isError = isError,
                )
            }
        }
    }

    @Composable
    private fun PoliciesText(
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val privacy = stringResource(R.string.product_subscription_policies_privacy)
        val onlineStore = stringResource(R.string.product_subscription_policies_online_store)
        val personalData = stringResource(R.string.product_subscription_policies_personal_data)

        val privacyUrl = stringResource(R.string.privacy_policy_url)
        val onlineStoreUrl = stringResource(R.string.online_store_policy_url)
        val personalDataUrl = stringResource(R.string.personal_data_policy_url)

        val substringToUrl = remember(
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
        val stringWithLinks = rememberStringWithLinks(
            baseString = stringResource(R.string.product_subscription_policies),
            substringToUrl = substringToUrl,
            urlStyle = UiKitTheme.typography.footnote.regular.toSpanStyle(),
            onUrlClicked = { onUrlClicked(Url(it)) },
        )

        Text(
            text = stringWithLinks,
            style = UiKitTheme.typography.footnote.light,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = modifier,
        )
    }
}
