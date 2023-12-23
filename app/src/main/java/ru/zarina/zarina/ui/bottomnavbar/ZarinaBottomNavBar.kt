package ru.zarina.zarina.ui.bottomnavbar

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.rework.ZarinaTheme
import ru.zarina.zarina.util.compose.HorizontalAndBottom

@Composable
fun ZarinaBottomNavBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.HorizontalAndBottom),
) {
    val backgroundColor = UiKitTheme.colorsReworked.background.general.regular.background
    val topBorderColor = UiKitTheme.colorsReworked.border.general.default

    // TODO: [High] Do not use restricted API
    val backStack by navController.currentBackStack.collectAsStateWithLifecycle()

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = 56.dp)
            .drawBehind {
                drawRect(backgroundColor)
                drawLine(
                    color = topBorderColor,
                    start = Offset.Zero,
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx(),
                )
            }
            .selectableGroup()
            .windowInsetsPadding(windowInsets)
            .clipToBounds()
            .padding(top = 6.dp, bottom = 4.dp),
    ) {
        BottomNavBarItem.ITEMS.forEach { item ->
            Item(
                title = stringResource(item.titleResId),
                iconResId = item.iconResId,
                isSelected = isItemSelected(item, backStack),
                onClick = { navController.navigateToBottomNavBarItem(item) },
            )
        }
    }
}

@Composable
private fun RowScope.Item(
    title: String,
    @DrawableRes
    iconResId: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val selectedColor = UiKitTheme.colorsReworked.text.general.regular.default

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = onClick,
                enabled = isEnabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, color = selectedColor),
            )
            .weight(1f),
    ) {
        val color by animateColorAsState(
            targetValue = if (isSelected) {
                UiKitTheme.colorsReworked.text.general.regular.default
            } else {
                UiKitTheme.colorsReworked.text.general.regular.disabled
            },
            label = "ZarinaBottomNavBar item color",
        )

        Icon(
            painter = painterResource(iconResId),
            contentDescription = title,
            tint = color,
            modifier = Modifier.size(20.dp),
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            style = UiKitTheme.typographyReworked.caption2.regular,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun isItemSelected(
    bottomNavItem: BottomNavBarItem,
    backStack: List<NavBackStackEntry>,
): Boolean {
    val itemRoutes = BottomNavBarItem.ITEMS.map { it.baseRoute.route }
    val lastBottomNavItemEntry = backStack.lastOrNull { backStackEntry ->
        val route = backStackEntry.destination.route
        itemRoutes.contains(route)
    }
    return lastBottomNavItemEntry?.destination?.route == bottomNavItem.baseRoute.route
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaTheme {
        ZarinaBottomNavBar(
            navController = rememberNavController(),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
