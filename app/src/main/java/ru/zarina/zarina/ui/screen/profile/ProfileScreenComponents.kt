package ru.zarina.zarina.ui.screen.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.item.ZarinaItem
import ru.zarina.zarina.ui.common.component.skeleton.ZarinaSkeleton
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.animation.AnimatedContentCrossfadeTransitionSpec

object ProfileScreenComponents {

    @Composable
    fun TopBar(
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            centerContent = {
                Text(
                    text = stringResource(R.string.profile),
                    style = UiKitTheme.typography.primary.regular,
                    color = UiKitTheme.colors.text.general.regular.default,
                )
            },
            modifier = modifier,
        )
    }

    @Composable
    fun AuthorizationSuggestion(
        onSignInClicked: () -> Unit,
        onSignUpClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            Text(
                text = stringResource(R.string.sign_in_or_sign_up),
                style = UiKitTheme.typography.secondary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.profile_authorization_description),
                style = UiKitTheme.typography.tertiary.regular,
                color = UiKitTheme.colors.text.general.regular.default,
            )

            Spacer(modifier = Modifier.height(28.dp))

            ZarinaButton(
                onClick = onSignInClicked,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.sign_in).uppercase())
            }
            Spacer(modifier = Modifier.height(8.dp))
            ZarinaButton(
                onClick = onSignUpClicked,
                colors = ZarinaButtonDefaults.outlineColors(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.sign_up).uppercase())
            }
        }
    }

    @Composable
    fun Info(
        isMyOrdersItemVisible: Boolean,
        city: City?,
        onMyOrdersClicked: () -> Unit,
        onCityClicked: () -> Unit,
        onShopsClicked: () -> Unit,
        onHelpClicked: () -> Unit,
        onAboutCompanyClicked: () -> Unit,
        appVersion: String,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            InfoItem.entries.forEachIndexed { index, item ->
                if (item != InfoItem.MyOrders || isMyOrdersItemVisible) {
                    InfoItem(
                        item = item,
                        city = city,
                        onMyOrdersClicked = onMyOrdersClicked,
                        onCityClicked = onCityClicked,
                        onShopsClicked = onShopsClicked,
                        onHelpClicked = onHelpClicked,
                        onAboutCompanyClicked = onAboutCompanyClicked,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    if (index < InfoItem.entries.lastIndex) {
                        Divider(
                            color = UiKitTheme.colors.border.general.default,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppVersion(
                version = appVersion,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    @Composable
    private fun InfoItem(
        item: InfoItem,
        city: City?,
        onMyOrdersClicked: () -> Unit,
        onCityClicked: () -> Unit,
        onShopsClicked: () -> Unit,
        onHelpClicked: () -> Unit,
        onAboutCompanyClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val onClick: () -> Unit
        val itemNameResId: Int
        when (item) {
            InfoItem.MyOrders -> {
                onClick = onMyOrdersClicked
                itemNameResId = R.string.my_orders
            }
            InfoItem.City -> {
                onClick = onCityClicked
                itemNameResId = R.string.city
            }
            InfoItem.Shops -> {
                onClick = onShopsClicked
                itemNameResId = R.string.shops
            }
            InfoItem.Help -> {
                onClick = onHelpClicked
                itemNameResId = R.string.help
            }
            InfoItem.AboutCompany -> {
                onClick = onAboutCompanyClicked
                itemNameResId = R.string.about_company
            }
        }

        ZarinaItem(
            onClick = onClick,
            startContent = {
                val textStyle = UiKitTheme.typography.secondary.light
                Text(
                    text = stringResource(itemNameResId),
                    style = textStyle,
                    color = UiKitTheme.colors.text.general.regular.default,
                )

                if (item == InfoItem.City) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AnimatedContent(
                        targetState = city,
                        transitionSpec = {
                            AnimatedContentCrossfadeTransitionSpec().using(sizeTransform = null)
                        },
                        contentAlignment = Alignment.CenterStart,
                        label = "City",
                    ) { city ->
                        if (city != null) {
                            Text(
                                text = city.name,
                                style = textStyle,
                                color = UiKitTheme.colors.text.general.regular.muted,
                            )
                        } else {
                            ZarinaSkeleton(
                                modifier = Modifier
                                    .height(16.dp)
                                    .fillMaxWidth(fraction = 0.35f),
                            )
                        }
                    }
                }
            },
            endContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_small_arrow_up_24),
                    contentDescription = null,
                    tint = UiKitTheme.colors.icon.regular.default,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(degrees = 90f),
                )
            },
            modifier = modifier,
        )
    }

    @Composable
    private fun AppVersion(
        version: String,
        modifier: Modifier = Modifier,
    ) {
        Text(
            text = stringResource(R.string.app_version_s, version),
            style = UiKitTheme.typography.footnote.regular,
            color = UiKitTheme.colors.text.general.regular.muted,
            modifier = modifier,
        )
    }

    private enum class InfoItem { MyOrders, City, Shops, Help, AboutCompany }
}
