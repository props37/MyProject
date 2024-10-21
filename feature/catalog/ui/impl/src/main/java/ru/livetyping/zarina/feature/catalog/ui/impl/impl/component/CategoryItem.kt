package ru.livetyping.zarina.feature.catalog.ui.impl.impl.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.catalog.ui.impl.R
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.category.CategoryListItem

@Composable
internal fun CategoryItem(
    item: CategoryListItem.CategoryItem,
    onItemClicked: (CategoryListItem.CategoryItem) -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        onClick = { onItemClicked(item) },
        startContent = {
            val color = LocalContentColor.current
            // TODO: [Top] Implement toComposeColor() function
//            val color = item.category.color?.toComposeColor()
//                ?: UiKitTheme.colors.text.general.regular.default

            val nestingStartPadding =
                item.nestingLevel * CategoryItemDefaults.NestingStartPaddingPerLevel

            Text(
                text = item.category.name.uppercase(),
                style = UiKitTheme.typography.tertiary.light,
                color = color,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = nestingStartPadding),
            )

            val label = item.category.label
            if (label != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label.uppercase(),
                    style = UiKitTheme.typography.caption2.light,
                    color = color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.Top),
                )
            }
        },
        endContent = {
            if (item.isExpandable) {
                Spacer(modifier = Modifier.width(8.dp))

                val rotation by animateFloatAsState(
                    targetValue = if (isExpanded) 0f else 180f,
                    animationSpec = tween(durationMillis = 200),
                    label = "CategoryItem Expand icon rotation",
                )
                val contentDescriptionResId =
                    if (isExpanded) R.string.collapse else R.string.expand

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
                    contentDescription = stringResource(contentDescriptionResId),
                    modifier = Modifier
                        .size(16.dp)
                        .graphicsLayer { rotationZ = rotation },
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun CategoryItemSkeleton(
    index: Int,
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        startContent = {
            @Suppress("MagicNumber")
            val widthFraction = when (index % 4) {
                0 -> 0.6f
                1 -> 0.72f
                2 -> 0.48f
                else -> 0.4f
            }

            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.tertiary.light,
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(widthFraction),
            )
        },
        endContent = {
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier.size(16.dp),
            )
        },
        modifier = modifier,
    )
}

internal object CategoryItemDefaults {
    val NestingStartPaddingPerLevel: Dp get() = 20.dp
}
