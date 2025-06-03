package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.TextStyle
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun TopBar(
    productCount: Int?,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        centerContent = {
            AnimatedContent(
                targetState = productCount,
                contentAlignment = Alignment.Center,
                contentKey = { it != null && it != 0 },
            ) { count ->
                val text = pluralStringResource(
                    id = RCommon.plurals.res_product_count,
                    count = count ?: SkeletonCount,
                    count?.toString() ?: SkeletonCount.toString(),
                ).uppercase()

                if (count != null && count != 0) {
                    Text(
                        text = text,
                        style = DefaultTextStyle,
                        color = UiKitTheme2.colors.mainBlack,
                    )
                } else {
                    ZarinaTextSkeleton(
                        text = text,
                        textStyle = DefaultTextStyle,
                    )
                }
            }
        },
        modifier = modifier,
    )
}

private val DefaultTextStyle: TextStyle
    @Composable
    get() = UiKitTheme2.typography.h3

private const val SkeletonCount = 10
