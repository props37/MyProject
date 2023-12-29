package ru.zarina.zarina.ui.common.component.button

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun CloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(R.string.close),
    isEnabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication = rememberRipple(bounded = false, radius = 24.dp),
) {
    IconButtonCustom(
        onClick = onClick,
        modifier = modifier,
        enabled = isEnabled,
        interactionSource = interactionSource,
        indication = indication,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_close_24),
            contentDescription = contentDescription,
            tint = UiKitTheme.colorsReworked.icon.regular.default,
        )
    }
}
