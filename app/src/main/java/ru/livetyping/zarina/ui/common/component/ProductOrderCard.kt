package ru.livetyping.zarina.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.Price
import ru.livetyping.zarina.domain.product.ProductColor
import ru.livetyping.zarina.domain.product.currentPrice
import ru.livetyping.zarina.ui.common.component.selector.ZarinaButtonSelector
import ru.livetyping.zarina.ui.common.component.selector.ZarinaButtonSelectorSize
import ru.livetyping.zarina.ui.common.component.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.ui.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.ui.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.common.util.rememberFormattedPrice
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.library.shimmer.shimmerToggleable
import ru.livetyping.zarina.utils.kotlin.capitalize

// TODO: [High] Refactor

@Composable
fun ProductOrderCard(
    name: String,
    imageUrl: Url,
    size: String,
    sizeRu: String?,
    height: String?,
    color: ProductColor?,
    modifier: Modifier = Modifier,
    count: Int? = null,
    countStyle: ProductOrderCardCountStyle = ProductOrderCardCountStyle.None,
    price: Price? = null,
    showOriginalPrice: Boolean = true,
    backgroundColor: Color = UiKitTheme.colors.background.general.regular.default,
    contentPadding: PaddingValues = PaddingValues(),
) {
    SideEffect {
        if (count != null) {
            require(count >= 1) { "count $count should be equal to or larger than 1" }
        }
    }

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .background(backgroundColor)
            .padding(contentPadding),
    ) {
        var isImageShimmerEnabled by remember(imageUrl) { mutableStateOf(true) }
        AsyncImage(
            model = imageUrl.value,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            onSuccess = { isImageShimmerEnabled = false },
            modifier = Modifier
                .height(ImageHeight)
                .aspectRatio(ImageAspectRatio)
                .shimmerToggleable(rememberZarinaSkeletonShimmer(), isImageShimmerEnabled)
                .background(UiKitTheme.colors.background.skeleton),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = name.uppercase(),
                style = UiKitTheme.typography.caption1.regular,
                color = UiKitTheme.colors.text.general.regular.default,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            SizeText(size = size, sizeRu = sizeRu, height = height)
            if (color != null) {
                Spacer(modifier = Modifier.height(2.dp))
                ColorText(color = color)
            }
            if (count != null && countStyle == ProductOrderCardCountStyle.Info) {
                Spacer(modifier = Modifier.height(2.dp))
                CountInfoText(count = count)
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.Bottom) {
                if (count != null && countStyle is ProductOrderCardCountStyle.Selector) {
                    ZarinaButtonSelector(
                        onClick = countStyle.onClick,
                        size = ZarinaButtonSelectorSize.Medium,
                        isEnabled = countStyle.isEnabled,
                        isEditable = countStyle.isEditable,
                    ) {
                        Text(text = count.toString())
                    }
                }

                if (price != null) {
                    Spacer(modifier = Modifier.weight(1f))
                    Price(
                        price = price,
                        count = count ?: 1,
                        showOriginalPrice = showOriginalPrice,
                    )
                }
            }
        }
    }
}

@Composable
fun ProductOrderCardSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
    contentPadding: PaddingValues = PaddingValues(),
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .padding(contentPadding),
    ) {
        ZarinaSkeleton(
            shimmer = shimmer,
            shape = RectangleShape,
            modifier = Modifier
                .height(ImageHeight)
                .aspectRatio(ImageAspectRatio),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            val height = 10.dp

            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.5f)
                    .height(height),
            )
            Spacer(modifier = Modifier.height(10.dp))
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.7f)
                    .height(height),
            )
            Spacer(modifier = Modifier.height(10.dp))
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.35f)
                    .height(height),
            )

            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.weight(1f))

            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier
                    .width(56.dp)
                    .height(24.dp),
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxHeight(),
        ) {
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier
                    .width(60.dp)
                    .height(12.dp),
            )
            Spacer(modifier = Modifier.height(10.dp))
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier
                    .width(48.dp)
                    .height(14.dp),
            )
        }
    }
}

@Composable
private fun SizeText(
    size: String,
    sizeRu: String?,
    height: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.size),
            style = InfoTextStyle,
            color = UiKitTheme.colors.text.general.regular.disabled,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = size + sizeRu.orEmpty(),
            style = InfoTextStyle,
            color = UiKitTheme.colors.text.general.regular.default,
        )

        if (height != null) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = SizeHeightSeparator,
                style = InfoTextStyle,
                color = UiKitTheme.colors.text.general.regular.disabled,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.height_cm, height),
                style = InfoTextStyle,
                color = UiKitTheme.colors.text.general.regular.default,
            )
        }
    }
}

@Composable
private fun ColorText(
    color: ProductColor,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.color),
            style = InfoTextStyle,
            color = UiKitTheme.colors.text.general.regular.disabled,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = color.name.capitalize(),
            style = InfoTextStyle,
            color = UiKitTheme.colors.text.general.regular.default,
        )
    }
}

@Composable
private fun CountInfoText(
    count: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.quantity),
            style = InfoTextStyle,
            color = UiKitTheme.colors.text.general.regular.disabled,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = count.toString(),
            style = InfoTextStyle,
            color = UiKitTheme.colors.text.general.regular.default,
        )
    }
}

@Composable
private fun Price(
    price: Price,
    count: Int,
    showOriginalPrice: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = modifier,
    ) {
        if (count > 1) {
            val priceForOne = rememberFormattedPrice(price.currentPrice)
            Text(
                text = stringResource(R.string.price_in_rubles_for_one_string, priceForOne),
                style = UiKitTheme.typography.footnote.light,
                color = UiKitTheme.colors.text.general.regular.muted,
            )
            Spacer(modifier = Modifier.height(2.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            val textStyle = UiKitTheme.typography.secondary.regular
            val totalOriginalPrice = rememberFormattedPrice(price.originalPrice * count)
            val totalCurrentPrice = rememberFormattedPrice(price.currentPrice * count)
            if (showOriginalPrice) {
                Text(
                    text = stringResource(R.string.price_in_rubles_string, totalOriginalPrice),
                    style = textStyle,
                    color = UiKitTheme.colors.text.general.regular.disabled,
                    textDecoration = TextDecoration.LineThrough,
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = stringResource(R.string.price_in_rubles_string, totalCurrentPrice),
                style = textStyle,
                color = UiKitTheme.colors.text.general.regular.default,
            )
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun PreviewNoCount() {
    ZarinaPreview {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            ProductOrderCard(
                name = "Плащ с поясом",
                imageUrl = remember { Url.EMPTY },
                size = "M",
                sizeRu = "48",
                height = "170",
                color = remember { FakeDataGenerator.getProductColor() },
                price = remember { FakeDataGenerator.getPrice() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun PreviewInfoCount() {
    ZarinaPreview {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            ProductOrderCard(
                name = "Плащ с поясом",
                imageUrl = remember { Url.EMPTY },
                size = "M",
                sizeRu = "48",
                height = "170",
                color = remember { FakeDataGenerator.getProductColor() },
                count = 3,
                countStyle = ProductOrderCardCountStyle.Info,
                price = remember { FakeDataGenerator.getPrice() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun PreviewSelectorCount() {
    ZarinaPreview {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            ProductOrderCard(
                name = "Плащ с поясом",
                imageUrl = remember { Url.EMPTY },
                size = "M",
                sizeRu = "48",
                height = "170",
                color = remember { FakeDataGenerator.getProductColor() },
                count = 3,
                countStyle = remember { ProductOrderCardCountStyle.Selector(onClick = {}) },
                price = remember { FakeDataGenerator.getPrice() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSkeleton() {
    ZarinaPreview {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            ProductOrderCardSkeleton(modifier = Modifier.fillMaxWidth())
        }
    }
}

sealed class ProductOrderCardCountStyle {
    data object None : ProductOrderCardCountStyle()

    data object Info : ProductOrderCardCountStyle()

    data class Selector(
        val isEnabled: Boolean = true,
        val isEditable: Boolean = true,
        val onClick: () -> Unit,
    ) : ProductOrderCardCountStyle()
}

private val ImageHeight: Dp get() = 128.dp
private const val ImageAspectRatio = 0.72f

private val InfoTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.footnote.regular

private const val SizeHeightSeparator = "|"
