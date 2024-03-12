package ru.zarina.zarina.ui.common.component

import androidx.annotation.IntRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.common.Url
import ru.zarina.zarina.domain.product.Price
import ru.zarina.zarina.domain.product.ProductColor
import ru.zarina.zarina.domain.product.currentPrice
import ru.zarina.zarina.ui.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.zarina.zarina.ui.common.tooling.FakeDataGenerator
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.common.util.rememberFormattedPrice
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.library.shimmer.shimmerToggleable
import ru.zarina.zarina.utils.kotlin.capitalize

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
) {
    Box(modifier = modifier) {
        Row {
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
                )
                Spacer(modifier = Modifier.height(4.dp))
                SizeText(size = size, sizeRu = sizeRu, height = height)
                if (color != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    ColorText(color = color)
                }
            }
        }

        // TODO: [High] Add counter

        if (price != null) {
            Price(
                price = price,
                count = count ?: 1,
                showOriginalPrice = showOriginalPrice,
                modifier = Modifier
                    .align(Alignment.BottomEnd),
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
private fun Price(
    price: Price,
    @IntRange(from = 0)
    count: Int,
    showOriginalPrice: Boolean,
    modifier: Modifier = Modifier,
) {
    SideEffect {
        require(count > 0) { "Count $count can not be less than zero" }
    }

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
private fun ProductOrderCardPreview() {
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
                price = remember { FakeDataGenerator.getPrice() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

enum class ProductOrderCardCountStyle { None, Info, Selector }

private val ImageHeight: Dp get() = 128.dp
private const val ImageAspectRatio = 0.72f

private val InfoTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.footnote.regular

private const val SizeHeightSeparator = "|"
