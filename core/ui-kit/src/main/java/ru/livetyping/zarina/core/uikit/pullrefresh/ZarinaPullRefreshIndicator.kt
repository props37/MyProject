package ru.livetyping.zarina.core.uikit.pullrefresh

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
public fun ZarinaPullRefreshIndicator(
    isRefreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ZarinaPullRefreshIndicatorDefaults.BackgroundColor,
    contentColor: Color = ZarinaPullRefreshIndicatorDefaults.ContentColor,
    scale: Boolean = false,
) {
    PullRefreshIndicator(
        refreshing = isRefreshing,
        state = state,
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        scale = scale,
    )
}

public object ZarinaPullRefreshIndicatorDefaults {
    internal val BackgroundColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.regular.default

    internal val ContentColor: Color
        @Composable
        get() = UiKitTheme.colors.icon.regular.default
}
