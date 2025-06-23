package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import ru.livetyping.zarina.core.uicompose.BottomEnd
import ru.livetyping.zarina.core.uicompose.TopEnd
import ru.livetyping.zarina.core.uicompose.popup.AnimatedPopup
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ZarinaClubPopup(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
) {
    var transformOrigin by remember { mutableStateOf(TransformOrigin.Center) }

    val density = LocalDensity.current
    val positionProvider = remember(density) {
        PositionProvider(
            density = density,
            onTransformOriginObtained = { transformOrigin = it },
        )
    }

    AnimatedPopup(
        isVisible = isVisible,
        onDismissRequest = onDismissRequest,
        popupPositionProvider = positionProvider,
        transformOrigin = transformOrigin,
    ) {
        val contentColor = UiKitTheme2.colors.white

        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Row(
                modifier = Modifier
                    .background(
                        color = UiKitTheme2.colors.mainBlack.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(1.dp),
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Text(
                    text = stringResource(R.string.product_zarina_club_popup_text).uppercase(),
                    style = UiKitTheme2.typography.body2,
                    color = contentColor,
                )

                Spacer(modifier = Modifier.width(8.dp))

                ZarinaCloseIconButton(
                    onClick = onDismissRequest,
                    iconSize = 12.dp,
                    tint = contentColor,
                )
            }
        }
    }
}

private class PositionProvider(
    private val density: Density,
    private val onTransformOriginObtained: (TransformOrigin) -> Unit,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val x = (anchorBounds.right - popupContentSize.width).coerceAtLeast(0)

        val popupPadding = with(density) { zarinaClubPopupPadding.roundToPx() }
        val isEnoughSpaceAbove =
            anchorBounds.topLeft.y - popupContentSize.height - popupPadding > 0
        val y = if (isEnoughSpaceAbove) {
            onTransformOriginObtained(TransformOrigin.BottomEnd)
            anchorBounds.top - popupContentSize.height - popupPadding
        } else {
            onTransformOriginObtained(TransformOrigin.TopEnd)
            anchorBounds.bottom + popupPadding
        }

        return IntOffset(x, y)
    }
}

private val zarinaClubPopupPadding: Dp get() = 8.dp
