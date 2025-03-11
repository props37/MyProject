package ru.livetyping.zarina.feature.product.ui.impl.impl.product.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductColor
import ru.livetyping.zarina.core.uicompose.toComposeColor
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
internal fun ProductColorSelector(
    productId: Product.Id,
    productColors: List<ProductColor>,
    onProductColorClicked: (ProductColor) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    backgroundColor: Color = BackgroundColor,
) {
    LazyRow(
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        modifier = modifier.background(backgroundColor),
    ) {
        items(
            items = productColors,
            key = { it.productId.value },
        ) { color ->
            Color(
                color = color,
                isSelected = color.productId == productId,
                onClick = onProductColorClicked,
                modifier = Modifier.animateZarinaItem(this),
            )
        }
    }
}

@Composable
internal fun ProductColorSelectorSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
) {
    val colorInteractiveSize = ColorInteractiveSize
    val colorBorderSize = ColorBorderSize

    Row(modifier = modifier) {
        repeat(SkeletonColorCount) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(colorInteractiveSize),
            ) {
                ZarinaSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier.size(colorBorderSize),
                )
            }
        }
    }
}

@Composable
private fun Color(
    color: ProductColor,
    isSelected: Boolean,
    onClick: (ProductColor) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(ColorInteractiveSize)
            .clickable(
                interactionSource = null,
                indication = ripple(bounded = false, radius = ColorCircleSize),
                onClick = { onClick(color) },
            ),
    ) {
        val borderColor by animateColorAsState(
            targetValue = if (isSelected) {
                UiKitTheme.colors.border.general.active
            } else {
                UiKitTheme.colors.border.general.default
            },
            label = "border color",
        )

        Box(
            modifier = Modifier
                .size(ColorBorderSize)
                .border(width = 1.dp, color = borderColor, shape = CircleShape)
                .padding((ColorBorderSize - ColorCircleSize) / 2)
                .background(
                    color = color.color.toComposeColor() ?: Color.Unspecified,
                    shape = CircleShape,
                ),
        )
    }
}

private val BackgroundColor: Color
    @Composable
    get() = UiKitTheme.colors.background.general.regular.default

private val ColorInteractiveSize: Dp get() = 32.dp
private val ColorBorderSize: Dp get() = 24.dp
private val ColorCircleSize: Dp get() = 16.dp

private const val SkeletonColorCount = 5
