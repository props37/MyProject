package ru.livetyping.zarina.core.uicomponent.sizeselector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaBottomSheet
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.resource.R as RCommon

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
        SizeTableLabel(modifier = Modifier.padding(start = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))

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

@Composable
private fun SizeTableLabel(modifier: Modifier = Modifier) {
    ZarinaButton(
        onClick = {},
        isEnabled = false,
        size = ZarinaButtonSize.Medium,
        colors = ZarinaButtonDefaults.secondaryColors(
            disabledBackgroundColor = UiKitTheme.colors.background.button.secondary.default,
            disabledContentColor = UiKitTheme.colors.text.button.secondary.default,
        ),
        modifier = modifier,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_ruler_24),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = stringResource(RCommon.string.res_size_table).uppercase(),
            modifier = Modifier.padding(top = 2.dp), // Circe font padding
        )
    }
}
