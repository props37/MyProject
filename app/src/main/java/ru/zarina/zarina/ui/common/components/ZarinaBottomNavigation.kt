package ru.zarina.zarina.ui.common.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ZarinaBottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(52.dp),
    ) {
        BottomNavigationRoot.items.forEach { item ->
            val isSelected = false
            BottomNavigationItem(
                item = item,
                isSelected = isSelected,
                onClick = { /*TODO*/ },
            )
        }
    }
}

@Composable
fun RowScope.BottomNavigationItem(
    item: BottomNavigationRoot,
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

sealed class BottomNavigationRoot(
    @DrawableRes
    val icon: Int,
    @StringRes
    val title: Int,
) {

    object Catalogue : BottomNavigationRoot(
        icon = R.drawable.ic_magnifying_glass_lines_36,
        title = R.string.catalogue
    )

    object Favourites : BottomNavigationRoot(
        icon = R.drawable.ic_heart_36,
        title = R.string.favourites
    )

    object Home : BottomNavigationRoot(
        icon = R.drawable.ic_home_36,
        title = R.string.main_page
    )

    object Profile : BottomNavigationRoot(
        icon = R.drawable.ic_person_36,
        title = R.string.profile
    )

    object Cart : BottomNavigationRoot(
        icon = R.drawable.ic_shopping_bag_36,
        title = R.string.cart
    )


    companion object {
        val items = listOf(Catalogue, Favourites, Home, Profile, Cart)
    }
}
