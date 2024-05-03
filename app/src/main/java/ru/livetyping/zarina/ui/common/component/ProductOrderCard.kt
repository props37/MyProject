package ru.livetyping.zarina.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
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
import ru.livetyping.zarina.ui.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.ui.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.ui.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.common.util.rememberFormattedPrice
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.kotlin.capitalize
import ru.livetyping.zarina.util.library.shimmer.shimmerToggleable

// TODO: [Low] Refactor

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
    backgroundColor: Color = BackgroundColor,
    contentPadding: PaddingValues = ContentPadding,
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
    contentPadding: PaddingValues = ContentPadding,
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
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption1.regular,
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(fraction = 0.5f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            ZarinaTextSkeleton(
                textStyle = InfoTextStyle,
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(fraction = 0.7f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            ZarinaTextSkeleton(
                textStyle = InfoTextStyle,
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
            )

            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.weight(1f))

            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier.size(width = 56.dp, height = 24.dp),
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxHeight(),
        ) {
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.footnote.light,
                shimmer = shimmer,
                modifier = Modifier.width(60.dp),
            )
            Spacer(modifier = Modifier.height(6.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.secondary.regular,
                shimmer = shimmer,
                modifier = Modifier.width(48.dp),
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
            text = stringResource(R.string.items_short, count.toString()),
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
            if (showOriginalPrice && price.currentPrice != price.originalPrice) {
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
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun PreviewNoCount() {
    ZarinaPreview {
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

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun PreviewInfoCount() {
    ZarinaPreview {
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

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun PreviewSelectorCount() {
    ZarinaPreview {
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

@Preview
@Composable
private fun PreviewSkeleton() {
    ZarinaPreview {
        ProductOrderCardSkeleton(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(),
        )
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

private val BackgroundColor: Color
    @Composable
    get() = UiKitTheme.colors.background.general.regular.default

private val ContentPadding: PaddingValues
    get() = PaddingValues(horizontal = 16.dp, vertical = 12.dp)

private val ImageHeight: Dp get() = 128.dp
private const val ImageAspectRatio = 0.72f

private val InfoTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.footnote.regular

private const val SizeHeightSeparator = "|"
