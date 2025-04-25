package ru.livetyping.zarina.core.uikit.bottombar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

@Composable
public fun ZarinaBottomBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = UiKitTheme2.colors.white,
    windowInsets: WindowInsets = ZarinaBottomBarDefaults.WindowInsets,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .drawBehind {
                drawRect(backgroundColor)
            }
            .selectableGroup()
            .windowInsetsPadding(windowInsets)
            .clipToBounds()
            .padding(top = 6.dp, bottom = 5.dp),
        content = content,
    )
}

public object ZarinaBottomBarDefaults {
    public val WindowInsets: WindowInsets
        @Composable
        get() = androidx.compose.foundation.layout.WindowInsets.navigationBars
            .union(androidx.compose.foundation.layout.WindowInsets.displayCutout)
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
}
