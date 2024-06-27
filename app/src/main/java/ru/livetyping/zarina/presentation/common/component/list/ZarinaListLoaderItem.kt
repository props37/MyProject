package ru.livetyping.zarina.presentation.common.component.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.presentation.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun ZarinaListLoaderItem(
    modifier: Modifier = Modifier,
    color: Color = ColorDefault,
    backgroundColor: Color = BackgroundColor,
    contentPadding: PaddingValues = ContentPadding,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(contentPadding),
    ) {
        ZarinaCircularLoader(
            color = color,
            modifier = Modifier.size(32.dp),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        ZarinaListLoaderItem()
    }
}

private val ColorDefault: Color
    @Composable
    get() = UiKitTheme.colors.icon.regular.default

private val BackgroundColor: Color
    @Composable
    get() = UiKitTheme.colors.background.general.regular.default

private val ContentPadding: PaddingValues get() = PaddingValues(vertical = 24.dp)
