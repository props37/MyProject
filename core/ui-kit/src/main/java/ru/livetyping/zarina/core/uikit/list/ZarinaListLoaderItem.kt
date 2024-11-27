package ru.livetyping.zarina.core.uikit.list

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
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaListLoaderItem(
    modifier: Modifier = Modifier,
    color: Color = ZarinaListLoaderItemDefaults.Color,
    backgroundColor: Color = ZarinaListLoaderItemDefaults.BackgroundColor,
    contentPadding: PaddingValues = ZarinaListLoaderItemDefaults.ContentPadding,
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

public object ZarinaListLoaderItemDefaults {
    internal val Color: Color
        @Composable
        get() = UiKitTheme.colors.icon.regular.default

    internal val BackgroundColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.regular.default

    internal val ContentPadding: PaddingValues get() = PaddingValues(vertical = 24.dp)
}
