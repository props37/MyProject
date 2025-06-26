package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun TopBar(
    productCount: Int?,
    alphaProvider: () -> Float,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        centerContent = {
            AnimatedContent(
                targetState = productCount,
                contentAlignment = Alignment.Center,
                contentKey = { it != null && it != 0 },
                modifier = Modifier.graphicsLayer { alpha = alphaProvider() },
            ) { count ->
                val text = if (count != null && count != 0) {
                    pluralStringResource(RCommon.plurals.res_product_count, count, count.toString())
                } else {
                    stringResource(RCommon.string.res_wishlist)
                }

                Text(
                    text = text.uppercase(),
                    style = DefaultTextStyle,
                    color = UiKitTheme2.colors.mainBlack,
                )
            }
        },
        modifier = modifier,
    )
}

private val DefaultTextStyle: TextStyle
    @Composable
    get() = UiKitTheme2.typography.h3
