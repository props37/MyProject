package ru.zarina.zarina.ui.common.components.buttons

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun ZarinaTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    colors: ZarinaButtonColors = ZarinaButtonDefaults.primaryColors(),
) {
    ZarinaButton(
        onClick = onClick,
        colors = colors,
        isLoading = isLoading,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = colors.foreground,
            style = UiKitTheme.typography.circle1720bold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun ZarinaTextButtonPreview() {
    ZarinaTheme {
        ZarinaTextButton(
            text = "ZarinaTextButtonLongPreview",
            onClick = {},
            colors = ZarinaButtonDefaults.primaryColors(),
        )
    }
}
