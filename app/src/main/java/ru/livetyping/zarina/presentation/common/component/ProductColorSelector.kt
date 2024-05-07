package ru.livetyping.zarina.presentation.common.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductColor
import ru.livetyping.zarina.presentation.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.presentation.common.util.domain.toComposeColor
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.presentation.theme.ZarinaTheme

@Composable
fun ProductColorSelector(
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
            )
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
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = ColorCircleSize),
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

@Preview
@Composable
private fun Preview() {
    ZarinaTheme {
        val productColors = remember { FakeDataGenerator.getProductColors(count = 10) }
        var productId by remember { mutableStateOf(productColors.random().productId) }
        ProductColorSelector(
            productId = productId,
            productColors = productColors,
            onProductColorClicked = { productId = it.productId },
        )
    }
}

private val BackgroundColor: Color
    @Composable
    get() = UiKitTheme.colors.background.general.regular.default

private val ColorInteractiveSize: Dp get() = 32.dp
private val ColorBorderSize: Dp get() = 24.dp
private val ColorCircleSize: Dp get() = 16.dp
