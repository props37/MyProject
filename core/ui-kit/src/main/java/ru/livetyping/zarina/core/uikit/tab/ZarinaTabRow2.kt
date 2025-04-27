package ru.livetyping.zarina.core.uikit.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEach
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2

@Composable
public fun ZarinaTabRow2(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    tabs: @Composable RowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically,
        content = tabs,
        modifier = modifier,
    )
}

@Composable
@Preview
private fun Preview() {
    val items = remember { mutableListOf("Женщинам", "Мужчинам") }
    var selected by remember { mutableStateOf(items.first()) }

    ZarinaTheme2 {
        ZarinaTabRow2(modifier = Modifier.background(Color.White)) {
            items.fastForEach { item ->
                ZarinaTab2(
                    text = item.uppercase(),
                    isSelected = item == selected,
                    onClick = { selected = item },
                    addBrackets = true,
                )
            }
        }
    }
}
