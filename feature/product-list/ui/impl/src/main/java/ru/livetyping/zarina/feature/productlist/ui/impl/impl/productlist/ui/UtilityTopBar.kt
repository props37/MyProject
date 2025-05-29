package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.button.ZarinaIconButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.core.resource.R as RCommon

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun UtilityTopBar(
    onBackClicked: () -> Unit,
    onFiltersClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    collapsingProgressProvider: () -> Float,
    modifier: Modifier = Modifier,
) {
    val borderColor = UiKitTheme2.colors.lightGray

    ZarinaTopBar(
        startContent = {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Row(
                    modifier = Modifier.offset {
                        val xOffset = lerp(
                            start = -(IconButtonSize.roundToPx()),
                            stop = 0,
                            fraction = collapsingProgressProvider(),
                        )
                        IntOffset(x = xOffset, y = 0)
                    },
                ) {
                    ZarinaBackIconButton(
                        onClick = onBackClicked,
                        iconSize = IconSize,
                        modifier = Modifier
                            .size(IconButtonSize)
                            .graphicsLayer {
                                alpha = collapsingProgressProvider()
                            },
                    )

                    FilterButton(
                        onClick = onFiltersClicked,
                        modifier = Modifier.size(IconButtonSize),
                    )
                }
            }
        },
        endContent = {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                SearchButton(
                    onClick = onSearchClicked,
                    modifier = Modifier.size(IconButtonSize),
                )
            }
        },
        contentPadding = PaddingValues(4.dp),
        backgroundColor = Color.Transparent,
        modifier = modifier.drawWithContent {
            drawContent()
            drawTopBorder(
                color = borderColor,
                alpha = 1f - collapsingProgressProvider(),
            )
            drawBottomBorder(borderColor)
        },
    )
}

@Composable
private fun FilterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaIconButton(
        onClick = onClick,
        indication = ripple(bounded = false, radius = IconSize),
        modifier = modifier,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_settings_menu_24),
            contentDescription = stringResource(RCommon.string.res_filters),
            modifier = Modifier.size(IconSize),
        )
    }
}

@Composable
private fun SearchButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaIconButton(
        onClick = onClick,
        indication = ripple(bounded = false, radius = IconSize),
        modifier = modifier,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_magnifying_glass_24),
            contentDescription = stringResource(RCommon.string.res_search_noun),
            modifier = Modifier.size(IconSize),
        )
    }
}

private fun DrawScope.drawTopBorder(
    color: Color,
    alpha: Float,
) {
    drawRect(
        color = color,
        size = Size(
            width = size.width,
            height = BorderWidth.toPx(),
        ),
        alpha = alpha,
    )
}

private fun DrawScope.drawBottomBorder(color: Color) {
    drawRect(
        color = color,
        topLeft = Offset(x = 0f, y = size.height - 1.dp.toPx()),
        size = Size(
            width = size.width,
            height = 1.dp.toPx(),
        ),
    )
}

private val IconButtonSize: Dp get() = 40.dp
private val IconSize: Dp get() = 16.dp

private val BorderWidth: Dp get() = 1.dp
