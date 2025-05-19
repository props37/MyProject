package ru.livetyping.zarina.core.uikit.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import ru.livetyping.zarina.core.analytics.compose.LocalAppMetrica
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.domain.analytics.toAppMetricaProduct
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.uicompose.pager.rememberEndlessPagerState
import ru.livetyping.zarina.core.uicompose.preview.ProductShortPreviewParameterProvider
import ru.livetyping.zarina.core.uikit.button.ZarinaLikeIconButton
import ru.livetyping.zarina.core.uikit.media.ZarinaMediaHorizontalPager
import ru.livetyping.zarina.core.uikit.pager.ZarinaHorizontalPagerIndicator
import ru.livetyping.zarina.core.uikit.price.DiscountLabel
import ru.livetyping.zarina.core.uikit.product.ProductCardDefaults.BackgroundColor
import ru.livetyping.zarina.core.uikit.product.ProductCardDefaults.MediaAspectRatio
import ru.livetyping.zarina.core.uikit.product.ProductCardDefaults.ProductNameTextStyle
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2
import java.math.BigDecimal

@OptIn(ExperimentalMaterialApi::class)
@Composable
public fun ProductCard(
    product: Product,
    onClick: (Product) -> Unit,
    onAddToWishlistClicked: (Product) -> Unit,
    modifier: Modifier = Modifier,
    mediaShimmer: Shimmer = rememberZarinaSkeletonShimmer(),
    appMetricaScreen: Screen? = null,
) {
    val appMetrica = LocalAppMetrica.current
    DisposableEffect(Unit) {
        if (appMetricaScreen != null) {
            appMetrica?.reportShowProductCardEvent(product.toAppMetricaProduct(), appMetricaScreen)
        }
        onDispose {}
    }

    Column(
        modifier = modifier
            .background(BackgroundColor)
            .clickable { onClick(product) },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(MediaAspectRatio),
        ) {
            val pagerState = rememberEndlessPagerState(itemCount = product.media.size)
            val hazeState = rememberHazeState()

            ZarinaMediaHorizontalPager(
                pagerState = pagerState,
                media = product.media,
                shimmer = mediaShimmer,
                modifier = Modifier
                    .matchParentSize()
                    .hazeSource(hazeState),
            )

            if (product.price.discountPercent != BigDecimal.ZERO) {
                DiscountLabel(
                    discountPercent = product.price.discountPercent,
                    hazeState = hazeState,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 6.dp, bottom = 6.dp),
                )
            }

            ZarinaHorizontalPagerIndicator(
                pagerState = pagerState,
                itemCount = product.media.size,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 6.dp),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        val horizontalPaddingModifier = Modifier.padding(horizontal = 10.dp)

        Text(
            text = product.name.uppercase(),
            style = ProductNameTextStyle,
            color = UiKitTheme2.colors.mainBlack,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = horizontalPaddingModifier,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = horizontalPaddingModifier,
        ) {
            ProductPrice(price = product.price)

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(12.dp))

            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                val iconSize = 16.dp
                ZarinaLikeIconButton(
                    isLiked = product.isInWishlist,
                    onClick = { onAddToWishlistClicked(product) },
                    iconSize = iconSize,
                    indication = ripple(bounded = false, radius = iconSize),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
public fun ProductCardSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
) {
    Column(modifier = modifier.background(BackgroundColor)) {
        ZarinaSkeleton(
            shimmer = shimmer,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(MediaAspectRatio),
        )

        Spacer(modifier = Modifier.height(8.dp))

        val horizontalPaddingModifier = Modifier.padding(horizontal = 10.dp)

        ZarinaTextSkeleton(
            textStyle = ProductNameTextStyle,
            shimmer = shimmer,
            modifier = Modifier
                .fillMaxWidth()
                .then(horizontalPaddingModifier),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .then(horizontalPaddingModifier),
        ) {
            ZarinaTextSkeleton(
                text = ProductCardDefaults.PriceSkeletonText,
                textStyle = ProductNameTextStyle,
                shimmer = shimmer,
            )

            Spacer(modifier = Modifier.weight(1f))

            ZarinaSkeleton(modifier = Modifier.size(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
@Preview
private fun Preview(
    @PreviewParameter(ProductShortPreviewParameterProvider::class)
    product: ProductShort,
) {
    ZarinaTheme2 {
        ProductCard(
            product = product,
            onClick = {},
            onAddToWishlistClicked = {},
        )
    }
}

@Composable
@Preview
private fun PreviewSkeleton() {
    ZarinaTheme2 {
        ProductCardSkeleton()
    }
}

public object ProductCardDefaults {
    internal val BackgroundColor: Color
        @Composable
        get() = UiKitTheme2.colors.white

    internal const val MediaAspectRatio = 0.75f

    internal val ProductNameTextStyle: TextStyle
        @Composable
        get() = UiKitTheme2.typography.body2

    internal const val PriceSkeletonText = "1 999 Р"
}
