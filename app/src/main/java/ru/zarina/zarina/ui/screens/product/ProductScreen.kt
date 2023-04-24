package ru.zarina.zarina.ui.screens.product

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Price
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.components.CollapsibleContainer
import ru.zarina.zarina.ui.common.components.ColorPicker
import ru.zarina.zarina.ui.common.components.DiscountBadge
import ru.zarina.zarina.ui.common.components.MediaPager
import ru.zarina.zarina.ui.common.components.PageDots
import ru.zarina.zarina.ui.common.components.ProductPrice
import ru.zarina.zarina.ui.common.components.buttons.ZarinaButton
import ru.zarina.zarina.ui.common.components.buttons.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun ProductScreenContent(
    product: Product?,
    onVariantClick: (Product.Variant) -> Unit,
    cache: State<Cache?>,
) {
    if (product != null)
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(UiKitTheme.colors.screenBackground)
                .statusBarsPadding(),
        ) {
            item(contentType = ProductScreenSection.MEDIA) {
                MediaSection(
                    product = product,
                    cache = cache,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item(contentType = ProductScreenSection.PRICE) {
                PriceSection(
                    price = product.price,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
            item(contentType = ProductScreenSection.COLORS) {
                ColorsSection(
                    product = product,
                    onVariantClick = onVariantClick,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp)
                )
            }
            item(contentType = ProductScreenSection.DIVIDER) {
                Divider(
                    color = UiKitTheme.colors.listDivider,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
            item(contentType = ProductScreenSection.DETAILS) {
                DetailsSection(
                    description = product.description,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item(contentType = ProductScreenSection.DIVIDER) {
                Divider(
                    color = UiKitTheme.colors.listDivider,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
            item(contentType = ProductScreenSection.SHARE) {
                ShareSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaSection(
    product: Product,
    cache: State<Cache?>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val pagerState = rememberPagerState()
        MediaPager(
            media = product.media,
            cache = cache,
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        )
        val coroutineScope = rememberCoroutineScope()
        PageDots(
            count = product.media.size,
            activeIndex = pagerState.currentPage,
            onDotClick = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        DiscountBadge(
            price = product.price,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}

@Composable
private fun PriceSection(
    price: Price,
    modifier: Modifier = Modifier,
) {
    ProductPrice(
        price = price,
        modifier = modifier
    )
}

@Composable
private fun ColorsSection(
    product: Product,
    onVariantClick: (Product.Variant) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = remember(product) { product.colorVariants.map { it.key } }
    val selectedColor = remember(product) {
        product.colorVariants.entries.firstOrNull { it.value.isCurrent }?.key
    }
    ColorPicker(
        colors = colors,
        onColorSelected = { color ->
            val variant = product.colorVariants[color]
            if (variant != null) onVariantClick(variant)
        },
        selectedColor = selectedColor,
        modifier = modifier
    )
}

@Composable
private fun DetailsSection(
    description: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
) {
    CollapsibleContainer(
        header = {
            Text(
                text = stringResource(id = R.string.details),
                style = UiKitTheme.typography.productDetailsHeader,
                color = UiKitTheme.colors.primaryContentColor,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        },
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            description.forEach {
                Text(
                    text = stringResource(R.string.key_value, it.first, it.second),
                    style = UiKitTheme.typography.productDetailsContent,
                    color = UiKitTheme.colors.primaryContentColor,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}

@Composable
private fun ShareSection(
    modifier: Modifier = Modifier,
) {
    ZarinaButton(
        onClick = { /*TODO*/ },
        colors = ZarinaButtonDefaults.secondaryColors(),
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share_24),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.share_product),
                style = UiKitTheme.typography.button,
                color = UiKitTheme.colors.secondaryButtonForeground,
                maxLines = 1,
            )
        }
    }
}

private enum class ProductScreenSection { MEDIA, PRICE, COLORS, DETAILS, SHARE, DIVIDER }

@Composable
fun ProductScreen() {
    val viewModel = hiltViewModel<ProductViewModel>()

    val cache = viewModel.cache.collectAsStateWithLifecycle()
    val product by viewModel.product.collectAsStateWithLifecycle()

    ProductScreenBehavior(
        sideEffects = viewModel.sideEffects,
    )

    ProductScreenContent(
        product = product,
        onVariantClick = viewModel::onVariantClick,
        cache = cache,
    )
}

@Composable
fun ProductScreenBehavior(
    sideEffects: Flow<ProductViewModel.SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun ProductScreenContentPreview(
    @PreviewParameter(ProductProvider::class, limit = 1)
    product: Product,
) {
    ZarinaTheme {
        ProductScreenContent(
            product = product,
            onVariantClick = {},
            cache = remember { mutableStateOf(null) },
        )
    }
}
