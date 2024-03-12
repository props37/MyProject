package ru.zarina.zarina.ui.common.component.tab

import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.zarina.zarina.ui.theme.UiKitTheme

// TODO: [High] Add support for Pager integration

@Composable
fun ZarinaTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = UiKitTheme.colors.background.general.regular.default,
    contentColor: Color = UiKitTheme.colors.background.general.inversed.default,
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

// TODO: [High] Add preview
