package ru.zarina.zarina.ui.common.components.toolbar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ru.zarina.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenToolbar(
    title: String,
    modifier: Modifier = Modifier,
    startIcon: @Composable (() -> Unit)? = null,
    endIcon: @Composable (() -> Unit)? = null,
    colors: TopAppBarColors = ScreenToolbarDefaults.colors(),
    isElevated: Boolean = false,
) {
    ScreenToolbar(
        title = {
            Text(
                text = title,
                style = UiKitTheme.typography.circle1718,
                color = UiKitTheme.colors.primaryContentColor,
                maxLines = 1,
            )
        },
        modifier = modifier,
        startIcon = startIcon,
        endIcon = endIcon,
        colors = colors,
        isElevated = isElevated,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenToolbar(
    title: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    startIcon: @Composable (() -> Unit)? = null,
    endIcon: @Composable (() -> Unit)? = null,
    colors: TopAppBarColors = ScreenToolbarDefaults.colors(),
    isElevated: Boolean = false,
) {
    val elevationDp by animateDpAsState(
        if (isElevated) 6.dp else 0.dp,
        label = "toolbar elevation"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevationDp)
            .zIndex(1000f),
    ) {
        CenterAlignedTopAppBar(
            title = title,
            navigationIcon = { if (startIcon != null) startIcon() },
            actions = { if (endIcon != null) endIcon() },
            colors = colors,
        )
    }
}

object ScreenToolbarDefaults {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun colors(
        containerColor: Color = UiKitTheme.colors.screenBackground,
        actionIconContentColor: Color = UiKitTheme.colors.primaryContentColor,
        navigationIconContentColor: Color = UiKitTheme.colors.primaryContentColor,
        titleContentColor: Color = UiKitTheme.colors.primaryContentColor,
    ) = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = containerColor,
        actionIconContentColor = actionIconContentColor,
        navigationIconContentColor = navigationIconContentColor,
        titleContentColor = titleContentColor,
    )
}
