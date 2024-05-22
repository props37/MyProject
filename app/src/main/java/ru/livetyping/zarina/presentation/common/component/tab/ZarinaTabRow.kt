package ru.livetyping.zarina.presentation.common.component.tab

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabPosition
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRow
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme

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

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        ZarinaTabRow(selectedTabIndex = 0) {
            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Medium,
                colors = ZarinaButtonDefaults.backlessColors(),
            ) {
                Text(text = "Женщинам".uppercase())
            }

            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Medium,
                colors = ZarinaButtonDefaults.backlessColors(),
            ) {
                Text(text = "Мужчинам".uppercase())
            }
        }
    }
}
