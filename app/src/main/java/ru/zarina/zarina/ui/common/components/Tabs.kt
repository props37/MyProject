package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun <T> Tabs(
    options: ImmutableList<T>,
    selectedOption: T,
    textResolver: @Composable (T) -> String,
    onOptionClick: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedTabIndex = remember(options, selectedOption) { options.indexOf(selectedOption) }
    TabRow(
        selectedTabIndex = selectedTabIndex,
        divider = {
            Divider(
                thickness = 1.dp,
                color = UiKitTheme.colorsOld.listDivider
            )
        },
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                color = UiKitTheme.colorsOld.primaryContentColor,
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
            )
        },
        containerColor = UiKitTheme.colorsOld.screenBackground,
        contentColor = UiKitTheme.colorsOld.primaryContentColor,
        modifier = modifier,
    ) {
        options.forEach { item ->
            Text(
                text = textResolver(item),
                color = UiKitTheme.colorsOld.primaryContentColor,
                style = UiKitTheme.typography.circle1718,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = UiKitTheme.colorsOld.primaryBorderColor,
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onOptionClick(item) }
                    )
                    .padding(vertical = 8.dp)
            )
        }
    }
}
