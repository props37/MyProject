package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.zarina.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenToolbar(
    title: String,
    modifier: Modifier = Modifier,
    endIcon: @Composable (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = UiKitTheme.typography.screenToolbarTitle,
                color = UiKitTheme.colors.primaryContentColor,
                maxLines = 1,
            )
        },
        actions = {
            if (endIcon != null) endIcon()
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = UiKitTheme.colors.screenBackground,
            actionIconContentColor = UiKitTheme.colors.primaryContentColor,
            navigationIconContentColor = UiKitTheme.colors.primaryContentColor,
            titleContentColor = UiKitTheme.colors.primaryContentColor,
        ),
        modifier = modifier.fillMaxWidth(),
    )
}
