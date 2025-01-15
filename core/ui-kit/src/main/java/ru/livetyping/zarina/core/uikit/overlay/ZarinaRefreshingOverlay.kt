package ru.livetyping.zarina.core.uikit.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.disableGestures
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaRefreshingOverlay(
    modifier: Modifier = Modifier,
    areGesturesDisabled: Boolean = true,
) {
    val color = UiKitTheme.colors.background.general.regular.default.copy(alpha = 0.5f)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color)
            .disableGestures(disabled = areGesturesDisabled),
    ) {
        ZarinaCircularLoader(modifier = Modifier.size(40.dp))
    }
}
