package ru.zarina.zarina.ui.common.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.behavior.base.DefaultBehaviorController
import ru.zarina.zarina.ui.common.behavior.navigationbar.LocalNavigationBarController
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarBehavior
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.Graph
import ru.zarina.zarina.ui.navigation.destinations.Catalog
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.utils.compose.topLineShape

private val BottomNavigationHeight = 56.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ZarinaBottomNavigation(
    navController: NavController,
    navigationBarController: DefaultBehaviorController<NavigationBarBehavior>,
    modifier: Modifier = Modifier,
) {
    val behavior by navigationBarController.currentBehavior.collectAsStateWithLifecycle()
    val isVisible = behavior is NavigationBarBehavior.Visible
    val isAnimated = behavior.isAnimated
    AnimatedVisibility(
        visible = isVisible,
        label = "is navigation bar visible",
        enter = if (isAnimated) expandVertically() else EnterTransition.None,
        exit = if (isAnimated) shrinkVertically() else ExitTransition.None,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(
                    with(LocalDensity.current) {
                        BottomNavigationHeight + WindowInsets.navigationBars
                            .getBottom(this)
                            .toDp()
                    }
                )
                .animateEnterExit(
                    enter = if (isAnimated) slideInVertically { it } else EnterTransition.None,
                    exit = if (isAnimated) slideOutVertically { it } else ExitTransition.None,
                )
                .border(
                    width = 1.dp,
                    color = UiKitTheme.colors.listDivider,
                    shape = topLineShape(1.dp),
                ),
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            BottomNavigationTab.items.forEach { item ->
                val isSelected =
                    currentDestination?.hierarchy?.any { it.route == item.destination.routeSchema } == true
                BottomNavigationItem(
                    item = item,
                    isSelected = isSelected,
                    onClick = {
                        navController.navigate(item)
                    },
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    }
}

fun NavController.navigate(tab: BottomNavigationTab) {
    val isCurrentTab =
        currentDestination?.hierarchy?.any { it.route == tab.destination.routeSchema } == true
    val isCurrentScreen =
        currentDestination?.route == (tab.destination as? Graph<*>)?.startDestination?.routeSchema
    when {
        isCurrentTab && !isCurrentScreen -> {
            navigate(tab.destination.routeSchema) {
                popBackStack(tab.destination.routeSchema, true)
            }
        }

        !isCurrentTab -> {
            navigate(tab.destination.routeSchema) {
                popUpTo(graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        else -> Unit
    }
}


@Composable
fun RowScope.BottomNavigationItem(
    item: BottomNavigationTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val foregroundColor = animateColorAsState(
        targetValue = if (isSelected) UiKitTheme.colors.primaryContentColor else UiKitTheme.colors.disabledPale,
        label = "$item foreground color",
    )
    Box(
        modifier = modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
            ),
    ) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = null,
            tint = if (isSelected) UiKitTheme.colors.primaryContentColor else UiKitTheme.colors.disabledPale,
            modifier = Modifier.align(Alignment.TopCenter),
        )
        Text(
            text = stringResource(id = item.title),
            style = UiKitTheme.typography.circle1012,
            color = foregroundColor.value,
            maxLines = 1,
            modifier = Modifier
                .padding(2.dp)
                .align(Alignment.BottomCenter),
        )
    }
}

sealed class BottomNavigationTab(
    @DrawableRes
    val icon: Int,
    @StringRes
    val title: Int,
    val destination: Destination<*>,
) {

    object Catalogue : BottomNavigationTab(
        icon = R.drawable.ic_magnifying_glass_lines_36,
        title = R.string.catalogue,
        destination = Catalog,
    )

    object Favourites : BottomNavigationTab(
        icon = R.drawable.ic_heart_36,
        title = R.string.favorites,
        destination = ru.zarina.zarina.ui.navigation.destinations.Favorites,
    )

    object Home : BottomNavigationTab(
        icon = R.drawable.ic_home_36,
        title = R.string.main_page,
        destination = ru.zarina.zarina.ui.navigation.destinations.Home,
    )

    object Profile : BottomNavigationTab(
        icon = R.drawable.ic_person_36,
        title = R.string.profile,
        destination = ru.zarina.zarina.ui.navigation.destinations.Profile,
    )

    object Cart : BottomNavigationTab(
        icon = R.drawable.ic_shopping_bag_36,
        title = R.string.cart,
        destination = ru.zarina.zarina.ui.navigation.destinations.Cart,
    )


    companion object {
        val items = listOf(Catalogue, Favourites, Home, Profile, Cart)
    }
}

fun Modifier.bottomNavigationPadding(): Modifier = composed {
    this.padding(bottomNavigationPaddingValues())
}

@Composable
fun bottomNavigationPaddingValues(): PaddingValues {
    val behavior = LocalNavigationBarController.current.currentBehavior.collectAsStateWithLifecycle()
    val isVisible = remember { derivedStateOf { behavior.value is NavigationBarBehavior.Visible } }
    return PaddingValues(bottom = if (isVisible.value) BottomNavigationHeight else 0.dp)
}
