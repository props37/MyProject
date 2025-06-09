package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.domain.model.common.Color
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductColor
import ru.livetyping.zarina.core.uikit.product.ProductDefaults
import ru.livetyping.zarina.core.uikit.shimmer.shimmerToggleable
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2
import ru.livetyping.zarina.feature.product.ui.impl.R

@Composable
internal fun ColorSelector(
    colors: List<ProductColor>,
    selectedColorProductId: Product.Id,
    onColorClicked: (ProductColor) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.product_color_selector_header).uppercase(),
            style = UiKitTheme2.typography.body,
            color = UiKitTheme2.colors.mainBlack,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        val shimmer = rememberZarinaSkeletonShimmer()

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(
                items = colors,
                key = { it.id.value },
            ) { color ->
                Color(
                    color = color,
                    isSelected = color.productId == selectedColorProductId,
                    onClick = onColorClicked,
                    shimmer = shimmer,
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
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    var isImageDisplayed by remember(color.imageUrl) { mutableStateOf(false) }

    val borderModifier = if (isSelected) {
        Modifier.border(width = 1.dp, color = UiKitTheme2.colors.mainBlack)
    } else {
        Modifier
    }

    AsyncImage(
        model = color.imageUrl?.value,
        contentDescription = null,
        onSuccess = { isImageDisplayed = true },
        modifier = modifier
            .width(60.dp)
            .aspectRatio(ProductDefaults.MediaAspectRatio)
            .shimmerToggleable(shimmer, isEnabled = !isImageDisplayed)
            .then(borderModifier)
            .graphicsLayer { alpha = if (isSelected) 0.3f else 1f }
            .background(UiKitTheme.colors.background.skeleton)
            .clickable { onClick(color) },
    )
}

@Composable
@Preview
private fun Preview() {
    ZarinaTheme2 {
        val colors = remember {
            val color = ProductColor(
                id = ProductColor.Id("1"),
                name = "",
                color = Color("#FFFFFF"),
                productId = Product.Id("p1"),
                imageUrl = Url.create(""),
            )
            listOf(
                color,
                color.copy(id = ProductColor.Id("2"), productId = Product.Id("p2")),
            )
        }

        var selectedColorProductId by remember { mutableStateOf(colors.first().productId) }

        ColorSelector(
            colors = colors,
            selectedColorProductId = selectedColorProductId,
            onColorClicked = { selectedColorProductId = it.productId },
            modifier = Modifier.background(androidx.compose.ui.graphics.Color.White),
        )
    }
}
