package ru.livetyping.zarina.core.uikit.sizeselector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaBottomSheet

@Composable
internal fun SizeSelectorScaffold(
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout),
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onCloseClicked,
            )
            .windowInsetsPadding(windowInsets),
    ) {
        ZarinaBottomSheet(
            modifier = Modifier.clickable(
                interactionSource = null,
                indication = null,
                onClick = {},
            ),
            content = content,
        )
    }
}
