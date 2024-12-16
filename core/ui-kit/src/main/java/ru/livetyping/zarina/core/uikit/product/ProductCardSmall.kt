package ru.livetyping.zarina.core.uikit.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.uikit.shimmer.shimmerToggleable
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ProductCardSmall(
    product: Product,
    onClick: (Product) -> Unit,
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
    backgroundColor: Color = ProductCardDefaults.BackgroundColor,
) {
    Column(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick(product) },
    ) {
        var isImageDisplayed by remember(product.media) { mutableStateOf(false) }
        AsyncImage(
            model = remember(product.media) { product.media.first().originalUrl.value },
            contentDescription = null,
            onSuccess = { isImageDisplayed = true },
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ProductCardDefaults.MediaAspectRatio)
                .shimmerToggleable(shimmer, isEnabled = !isImageDisplayed)
                .background(UiKitTheme.colors.background.skeleton),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.name.uppercase(),
            style = UiKitTheme.typography.caption1.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        ProductPrice(price = product.price)

        ProductCardColors(colors = product.colors)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
public fun ProductCardSmallSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
) {
    Column(modifier = modifier) {
        ZarinaSkeleton(
            shimmer = shimmer,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ProductCardDefaults.MediaAspectRatio),
        )

        Spacer(modifier = Modifier.height(16.dp))

        ZarinaTextSkeleton(
            textStyle = UiKitTheme.typography.caption1.regular,
            shimmer = shimmer,
            modifier = Modifier.fillMaxWidth(fraction = 0.6f),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption1.regular,
                shimmer = shimmer,
                modifier = Modifier.width(32.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption1.regular,
                shimmer = shimmer,
                modifier = Modifier.width(32.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption2.regular,
                shimmer = shimmer,
                modifier = Modifier.width(20.dp),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ZarinaSkeleton(
            shimmer = shimmer,
            modifier = Modifier.size(width = 42.dp, height = 8.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
