package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import ru.zarina.zarina.domain.old.City
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun CityPicker(
    city: City?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownBar(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = city?.name.orEmpty(),
            style = UiKitTheme.typography.circle1718,
            color = UiKitTheme.colorsOld.primaryContentColor,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
