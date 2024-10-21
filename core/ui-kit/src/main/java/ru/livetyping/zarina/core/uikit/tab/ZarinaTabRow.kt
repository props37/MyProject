package ru.livetyping.zarina.core.uikit.tab

import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ZarinaTabRowDefaults.BackgroundColor,
    contentColor: Color = ZarinaTabRowDefaults.ContentColor,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = { tabPositions ->
        ZarinaTabIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
        )
    },
    tabs: @Composable () -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        indicator = indicator,
        divider = {},
        tabs = tabs,
        modifier = modifier,
    )
}

public object ZarinaTabRowDefaults {
    public val BackgroundColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.regular.default

    public val ContentColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.inversed.default
}
