package ru.livetyping.zarina.ui.screen.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.user.LoyaltyCard
import ru.livetyping.zarina.ui.common.component.LoyaltyCardSide
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.ui.common.component.button.ZarinaIconButton
import ru.livetyping.zarina.ui.common.component.item.ZarinaItem
import ru.livetyping.zarina.ui.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.ui.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.ui.screen.profile.ProfileViewModel.InfoItem
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentCrossfadeTransitionSpec
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.ui.common.component.LoyaltyCard as LoyaltyCardImpl

object ProfileScreenComponents {

    @Composable
    fun TopBar(
        userFirstName: String?,
        isEditProfileButtonVisible: Boolean,
        onProfileDetailsClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            centerContent = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.profile),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    AnimatedContent(
                        targetState = userFirstName,
                        transitionSpec = {
                            AnimatedContentDefaultTransitionSpec()
                                .using(SizeTransform(clip = false))
                        },
                        contentAlignment = Alignment.Center,
                        label = "User first name",
                    ) { firstName ->
                        if (firstName != null) {
                            Text(
                                text = firstName,
                                style = UiKitTheme.typography.tertiary.regular,
                                color = UiKitTheme.colors.text.general.regular.muted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            },
            endContent = {
                AnimatedVisibility(
                    visible = isEditProfileButtonVisible,
                    enter = AnimatedContentDefaultEnterTransition,
                    exit = AnimatedContentDefaultExitTransition,
                ) {
                    ZarinaIconButton(
                        onClick = onProfileDetailsClicked,
                        indication = rememberRipple(bounded = false, radius = 20.dp),
                        modifier = Modifier.padding(end = 2.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_pencil_24),
                            contentDescription = stringResource(R.string.edit_profile),
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
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
    fun LoyaltyCard(
        loyaltyCard: LoyaltyCard?,
        onLevelInfoClicked: () -> Unit,
        onSideChanged: (LoyaltyCardSide?) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AnimatedContent(
            targetState = loyaltyCard,
            transitionSpec = {
                AnimatedContentDefaultTransitionSpec().using(SizeTransform(clip = false))
            },
            contentKey = { it != null },
            label = "LoyaltyCard",
            modifier = modifier,
        ) { card ->
            if (card != null) {
                LoyaltyCardImpl(
                    card = card,
                    onLevelInfoClicked = onLevelInfoClicked,
                    onSideChanged = onSideChanged,
                )
            } else {
                SideEffect { onSideChanged(null) }
                // TODO: [High] Implement
                Text(
                    text = "Здесь будет плейсхолдер карты",
                    style = UiKitTheme.typography.primary.bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
                )
            }
        }
    }

    @Composable
    fun Info(
        infoItems: ImmutableList<InfoItem>,
        city: City?,
        onInfoItemClicked: (InfoItem) -> Unit,
        appVersion: String,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            infoItems.forEachIndexed { index, item ->
                key(item) {
                    InfoItem(
                        item = item,
                        city = city,
                        onInfoItemClicked = onInfoItemClicked,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    if (index < infoItems.lastIndex) {
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
        onInfoItemClicked: (InfoItem) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val itemNameResId = when (item) {
            InfoItem.MyOrders -> R.string.my_orders
            InfoItem.City -> R.string.city
            InfoItem.Shops -> R.string.shops
            InfoItem.Help -> R.string.help
            InfoItem.AboutCompany -> R.string.about_company
        }

        ZarinaItem(
            onClick = { onInfoItemClicked(item) },
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
                            ZarinaTextSkeleton(
                                textStyle = textStyle,
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
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
}
