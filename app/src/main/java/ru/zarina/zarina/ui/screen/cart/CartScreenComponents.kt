package ru.zarina.zarina.ui.screen.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.skeleton.Skeleton
import ru.zarina.zarina.ui.common.component.skeleton.rememberSkeletonShimmer
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultExitTransition
import ru.zarina.zarina.util.compose.Crossfade

object CartScreenComponents {

    @Composable
    fun TopBar(
        isClearButtonVisible: Boolean,
        onClearClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            centerContent = {
                Text(
                    text = stringResource(R.string.cart),
                    style = UiKitTheme.typographyReworked.primary.regular,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                )
            },
            endContent = {
                AnimatedVisibility(
                    visible = isClearButtonVisible,
                    enter = AnimatedContentDefaultEnterTransition,
                    exit = AnimatedContentDefaultExitTransition,
                ) {
                    ZarinaButton(
                        onClick = onClearClicked,
                        size = ZarinaButtonSize.Medium,
                        colors = ZarinaButtonDefaults.backlessColors(),
                    ) {
                        Text(text = stringResource(R.string.clear).uppercase())
                    }
                }
            },
            modifier = modifier,
        )
    }

    @Composable
    fun City(
        city: City?,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = city,
            contentKey = { it != null },
            modifier = modifier
                .clickable(
                    enabled = city != null,
                    onClick = onClick,
                ),
        ) { city ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                if (city != null) {
                    Text(
                        text = city.name,
                        style = UiKitTheme.typographyReworked.secondary.light,
                        color = UiKitTheme.colorsReworked.text.general.regular.default,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.ic_small_arrow_up_24),
                        contentDescription = stringResource(R.string.change_city),
                        tint = UiKitTheme.colorsReworked.icon.regular.default,
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(90f),
                    )
                } else {
                    val shimmer = rememberSkeletonShimmer(ShimmerBounds.Window)
                    Skeleton(
                        shimmer = shimmer,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.55f)
                            .height(16.dp),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Skeleton(
                        shimmer = shimmer,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    fun EmptyCartPlaceholder(
        onGoToCatalogClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(R.drawable.ic_cart_outline_64),
                contentDescription = null,
                tint = UiKitTheme.colorsReworked.icon.regular.disabled,
                modifier = Modifier.size(64.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.cart_screen_empty_cart_placeholder_title),
                style = UiKitTheme.typographyReworked.primary.bold,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.cart_screen_empty_cart_placeholder_description),
                style = UiKitTheme.typographyReworked.secondary.regular,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaButton(
                onClick = onGoToCatalogClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.go_to_catalog).uppercase())
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
