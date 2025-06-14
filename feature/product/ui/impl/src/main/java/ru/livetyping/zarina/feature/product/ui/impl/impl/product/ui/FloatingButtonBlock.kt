package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.pressBounce
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.resource.R as RCommon

// TODO: [Top] Implement
@Composable
internal fun FloatingButtonBlock(
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        val buttonSize = ZarinaButtonSize.Large

        ZarinaButton(
            onClick = {},
            size = buttonSize,
            modifier = Modifier.weight(1f),
        ) {
            Text(text = "Добавить в корзину".uppercase())
        }

        Spacer(modifier = Modifier.width(4.dp))

        val interactionSource = remember { MutableInteractionSource() }

        ZarinaButton(
            onClick = {},
            size = buttonSize,
            interactionSource = interactionSource,
            contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(RCommon.drawable.ic_heart_24),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .pressBounce(interactionSource),
            )
        }
    }
}

internal val FloatingButtonBlockHeight = ZarinaButtonDefaults.SizeLarge
