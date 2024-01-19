package ru.zarina.zarina.ui.screen.products

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.base.TopBarDefaults
import ru.zarina.zarina.ui.common.component.base.button.BackIconButton
import ru.zarina.zarina.ui.common.component.base.button.ZarinaIconButton
import ru.zarina.zarina.ui.common.component.base.skeleton.Skeleton
import ru.zarina.zarina.ui.theme.UiKitTheme

object ProductsScreenComponents {

    @Composable
    fun TopBar(
        title: String?,
        actions: TopBarActions,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .heightIn(min = TopBarDefaults.MinHeight)
                .padding(TopBarDefaults.VerticalPadding),
        ) {
            BackIconButton(
                onClick = actions.onBackClicked,
                iconSize = TopBarIconSize,
            )

            Spacer(modifier = Modifier.width(4.dp))

            AnimatedContent(
                targetState = title,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                contentAlignment = Alignment.CenterStart,
                label = "TopBar title",
                modifier = Modifier.weight(1f),
            ) { title ->
                if (title != null) {
                    Text(
                        text = title,
                        style = UiKitTheme.typographyReworked.primary.regular,
                        color = UiKitTheme.colorsReworked.text.general.regular.default,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Skeleton(
                        modifier = Modifier
                            .wrapContentWidth(align = Alignment.Start)
                            .fillMaxWidth(fraction = 0.5f)
                            .height(20.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            ZarinaIconButton(
                onClick = actions.onSearchClicked,
                indication = rememberRipple(bounded = false, radius = TopBarIconSize),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search_24),
                    contentDescription = stringResource(R.string.search),
                    tint = UiKitTheme.colorsReworked.icon.regular.default,
                    modifier = Modifier.size(TopBarIconSize),
                )
            }

            ZarinaIconButton(
                onClick = actions.onFiltersClicked,
                indication = rememberRipple(bounded = false, radius = TopBarIconSize),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_filters_24),
                    contentDescription = stringResource(R.string.filters),
                    tint = UiKitTheme.colorsReworked.icon.regular.default,
                    modifier = Modifier.size(TopBarIconSize),
                )
            }
        }
    }

    @Stable
    class TopBarActions(
        val onBackClicked: () -> Unit,
        val onSearchClicked: () -> Unit,
        val onFiltersClicked: () -> Unit,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TopBarActions

            if (onBackClicked != other.onBackClicked) return false
            if (onSearchClicked != other.onSearchClicked) return false
            return onFiltersClicked == other.onFiltersClicked
        }

        override fun hashCode(): Int {
            var result = onBackClicked.hashCode()
            result = 31 * result + onSearchClicked.hashCode()
            result = 31 * result + onFiltersClicked.hashCode()
            return result
        }
    }

    private val TopBarIconSize: Dp get() = 20.dp
}
