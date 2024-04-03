package ru.livetyping.zarina.ui.common.component.button

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun ZarinaBackIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(R.string.back),
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    iconSize: Dp = 24.dp,
    tint: Color = UiKitTheme.colors.icon.regular.default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication = rememberRipple(bounded = false, radius = iconSize),
) {
    ZarinaIconButton(
        onClick = onClick,
        isEnabled = isEnabled,
        isLoading = isLoading,
        loaderSize = iconSize,
        loaderColor = tint,
        interactionSource = interactionSource,
        indication = indication,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_small_arrow_up_24),
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier
                .size(iconSize)
                .rotate(Rotation),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        ZarinaBackIconButton(
            onClick = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}

private const val Rotation = 270f
