package ru.livetyping.zarina.presentation.bottomnavbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.uikit.bottombar.ZarinaBottomBar
import ru.livetyping.zarina.core.uikit.bottombar.ZarinaBottomBarDefaults
import ru.livetyping.zarina.core.uikit.bottombar.ZarinaBottomBarItem
import ru.livetyping.zarina.core.uikit.bottombar.navigation.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.bottombar.navigation.behavior.LocalBottomNavBarBehaviorController
import ru.livetyping.zarina.core.uikit.bottombar.navigation.sizetracker.LocalBottomNavBarSizeTracker

@Composable
fun ZarinaBottomNavBar(
    navController: NavHostController,
    wishlistProductCountProvider: () -> Int,
    cartProductCountProvider: () -> Int,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = ZarinaBottomBarDefaults.WindowInsets,
) {
    AnimatedZarinaBottomBar(
        windowInsets = windowInsets,
        modifier = modifier,
    ) {
        // TODO: [Medium] Do not use restricted API
        val backStack by navController.currentBackStack.collectAsStateWithLifecycle()

        for (i in BottomNavBarItems.indices) {
            val item = BottomNavBarItems[i]
            key(item) {
                val isSelected = isItemSelected(item, backStack)

                val counterValueProvider = when (item) {
                    BottomNavBarItem.Wishlist -> wishlistProductCountProvider
                    BottomNavBarItem.Cart -> cartProductCountProvider
                    else -> null
                }

                ZarinaBottomBarItem(
                    title = stringResource(item.titleResId),
                    iconResId = item.iconResId,
                    isSelected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            navController.navigateToBottomNavBarItem(item)
                        } else {
                            navController.popBackStackToBottomNavBarItem(item)
                        }
                    },
                    counterValueProvider = counterValueProvider,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun AnimatedZarinaBottomBar(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = ZarinaBottomBarDefaults.WindowInsets,
    content: @Composable RowScope.() -> Unit,
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

    val enterTransition = remember { expandVertically(BottomBarAnimationSpec) }
    val exitTransition = remember { shrinkVertically(BottomBarAnimationSpec) }

    val contentEnterTransition = remember {
        slideInVertically(BottomBarContentAnimationSpec) { it }
    }
    val contentExitTransition = remember {
        slideOutVertically(BottomBarContentAnimationSpec) { it }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = if (isBottomNavBarAnimated) enterTransition else EnterTransition.None,
        exit = if (isBottomNavBarAnimated) exitTransition else ExitTransition.None,
        modifier = modifier
            .onSizeChanged { size ->
                sizeTracker.onSizeChanged(size)
            },
    ) {
        ZarinaBottomBar(
            windowInsets = windowInsets,
            content = content,
            modifier = Modifier
                .animateEnterExit(
                    enter = if (isBottomNavBarAnimated) {
                        contentEnterTransition
                    } else {
                        EnterTransition.None
                    },
                    exit = if (isBottomNavBarAnimated) {
                        contentExitTransition
                    } else {
                        ExitTransition.None
                    },
                ),
        )
    }
}

private fun isItemSelected(
    bottomNavItem: BottomNavBarItem,
    backStack: List<NavBackStackEntry>,
): Boolean {
    val bottomNavBarItemFeatureEntry = bottomNavItem.toFeatureNavEntry()
    val bottomNavBarItemNavEntries = BottomNavBarItems.map { it.toFeatureNavEntry() }
    val lastBottomNavBarItemBackStackEntry = backStack.lastOrNull { backStackEntry ->
        bottomNavBarItemNavEntries.any { navEntry ->
            backStackEntry.destination.hasRoute(navEntry::class)
        }
    }
    return lastBottomNavBarItemBackStackEntry?.destination
        ?.hasRoute(bottomNavBarItemFeatureEntry::class) ?: false
}

private const val BottomBarAnimationSpringStiffness = Spring.StiffnessMedium

@Stable
internal val BottomBarAnimationSpec: SpringSpec<IntSize> = spring(
    stiffness = BottomBarAnimationSpringStiffness,
    visibilityThreshold = IntSize.VisibilityThreshold,
)

@Stable
internal val BottomBarContentAnimationSpec: SpringSpec<IntOffset> = spring(
    stiffness = BottomBarAnimationSpringStiffness,
    visibilityThreshold = IntOffset.VisibilityThreshold,
)
