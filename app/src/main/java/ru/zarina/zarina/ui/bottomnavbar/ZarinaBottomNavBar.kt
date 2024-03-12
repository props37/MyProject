package ru.zarina.zarina.ui.bottomnavbar

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.BottomNavBarBehavior
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.LocalBottomNavBarBehaviorController
import ru.zarina.zarina.ui.common.component.ZarinaCounter
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultTransitionSpec
import ru.zarina.zarina.util.compose.HorizontalAndBottom
import ru.zarina.zarina.util.compose.unscalable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ZarinaBottomNavBar(
    navController: NavHostController,
    cartProductCount: Int,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = DefaultWindowInsets,
) {
    val behaviorController = LocalBottomNavBarBehaviorController.current
    val behavior by behaviorController.currentBehavior.collectAsStateWithLifecycle()
    val isBottomNavBarVisible = behavior is BottomNavBarBehavior.Visible
    val isBottomNavBarAnimated = behavior.isAnimated

    val sizeTracker = LocalBottomNavBarSizeTracker.current

    val visibleState = remember { MutableTransitionState(isBottomNavBarVisible) }
    DisposableEffect(isBottomNavBarVisible) {
        visibleState.targetState = isBottomNavBarVisible
        onDispose {}
    }

    LaunchedEffect(visibleState, sizeTracker) {
        snapshotFlow { visibleState.currentState }.collect { isVisible ->
            if (!isVisible) {
                sizeTracker.onSizeChanged(IntSize.Zero)
            }
        }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = remember(isBottomNavBarAnimated) {
            if (isBottomNavBarAnimated) {
                expandVertically(BottomNavBarAnimationSpec)
            } else {
                EnterTransition.None
            }
        },
        exit = remember(isBottomNavBarAnimated) {
            if (isBottomNavBarAnimated) {
                shrinkVertically(BottomNavBarAnimationSpec)
            } else {
                ExitTransition.None
            }
        },
        modifier = modifier.onSizeChanged { size ->
            sizeTracker.onSizeChanged(size)
        },
    ) {
        val backgroundColor = UiKitTheme.colors.background.general.regular.default
        val topBorderColor = UiKitTheme.colors.border.general.default

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .animateEnterExit(
                    enter = remember(isBottomNavBarAnimated) {
                        if (isBottomNavBarAnimated) {
                            slideInVertically(BottomNavBarContentAnimationSpec) { it }
                        } else {
                            EnterTransition.None
                        }
                    },
                    exit = remember(isBottomNavBarAnimated) {
                        if (isBottomNavBarAnimated) {
                            slideOutVertically(BottomNavBarContentAnimationSpec) { it }
                        } else {
                            ExitTransition.None
                        }
                    },
                )
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
            // TODO: [Medium] Do not use restricted API
            val backStack by navController.currentBackStack.collectAsStateWithLifecycle()

            BottomNavBarItem.ITEMS.forEach { item ->
                Item(
                    title = stringResource(item.titleResId),
                    iconResId = item.iconResId,
                    isSelected = isItemSelected(item, backStack),
                    onClick = { navController.navigateToBottomNavBarItem(item) },
                    counterValue = if (item is BottomNavBarItem.Cart) cartProductCount else null,
                )
            }
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
    counterValue: Int? = null,
    isEnabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val selectedColor = UiKitTheme.colors.text.general.regular.default

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
                UiKitTheme.colors.text.general.regular.default
            } else {
                UiKitTheme.colors.text.general.regular.disabled
            },
            label = "ZarinaBottomNavBar item color",
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.widthIn(min = 40.dp),
        ) {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(20.dp),
            )

            ItemCounter(
                count = counterValue,
                modifier = Modifier.align(Alignment.TopEnd),
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            style = UiKitTheme.typography.caption2.regular,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ItemCounter(
    count: Int?,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = count,
        transitionSpec = {
            AnimatedContentDefaultTransitionSpec().using(sizeTransform = null)
        },
        contentAlignment = Alignment.TopEnd,
        label = "ItemCounter",
        modifier = modifier,
    ) { count ->
        if (count != null && count != 0) {
            ZarinaCounter(
                value = count.toString(),
                textStyle = UiKitTheme.typography.caption2.bold.unscalable(LocalDensity.current),
            )
        }
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

private val DefaultWindowInsets: WindowInsets
    @Composable
    get() = WindowInsets.navigationBars
        .union(WindowInsets.displayCutout)
        .only(WindowInsetsSides.HorizontalAndBottom)

private const val BottomNavBarAnimationSpringStiffness = Spring.StiffnessMedium

private val BottomNavBarAnimationSpec: SpringSpec<IntSize>
    get() = spring(
        stiffness = BottomNavBarAnimationSpringStiffness,
        visibilityThreshold = IntSize.VisibilityThreshold,
    )

private val BottomNavBarContentAnimationSpec: SpringSpec<IntOffset>
    get() = spring(
        stiffness = BottomNavBarAnimationSpringStiffness,
        visibilityThreshold = IntOffset.VisibilityThreshold,
    )

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        ZarinaBottomNavBar(
            navController = rememberNavController(),
            cartProductCount = 5,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
