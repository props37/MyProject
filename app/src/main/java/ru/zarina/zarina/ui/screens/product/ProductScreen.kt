package ru.zarina.zarina.ui.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.DeliveryAvailability
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.screens.product.components.sections.ColorsSection
import ru.zarina.zarina.ui.screens.product.components.sections.CompleteLookSection
import ru.zarina.zarina.ui.screens.product.components.sections.DetailsSection
import ru.zarina.zarina.ui.screens.product.components.sections.MediaSection
import ru.zarina.zarina.ui.screens.product.components.sections.PriceSection
import ru.zarina.zarina.ui.screens.product.components.sections.ShareSection
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.utils.android.share

@Composable
fun ProductScreenContent(
    product: Product?,
    onVariantClick: (Product.Variant) -> Unit,
    onShareClick: () -> Unit,
    completeLookProducts: List<Product>,
    onProductClick: (Product) -> Unit,
    cache: State<Cache?>,
    deliveryAvailability: DeliveryAvailability?,
) {
    if (product != null)
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
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
            item(contentType = ProductScreenSection.COMPLETE_LOOK) {
                CompleteLookSection(
                    products = completeLookProducts,
                    onProductClick = onProductClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
            if (product.url != null)
                item(contentType = ProductScreenSection.SHARE) {
                    ShareSection(
                        onShareClick = onShareClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            item(contentType = ProductScreenSection.DELIVERY) {

            }
        }
}

private enum class ProductScreenSection { MEDIA, PRICE, COLORS, DETAILS, SHARE, COMPLETE_LOOK, DELIVERY, DIVIDER }

@Composable
fun ProductScreen(
    showProduct: (String) -> Unit,
) {
    val viewModel = hiltViewModel<ProductViewModel>()

    val cache = viewModel.cache.collectAsStateWithLifecycle()
    val product by viewModel.product.collectAsStateWithLifecycle()
    val completeLookProducts by viewModel.completeLookProducts.collectAsStateWithLifecycle()
    val deliveryAvailability by viewModel.deliveryAvailability.collectAsStateWithLifecycle()

    ProductScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showProduct = showProduct,
    )

    ProductScreenContent(
        product = product,
        onVariantClick = viewModel::onVariantClick,
        onShareClick = viewModel::onShareClick,
        completeLookProducts = completeLookProducts,
        onProductClick = viewModel::onProductClick,
        deliveryAvailability = deliveryAvailability,
        cache = cache,
    )
}

@Composable
fun ProductScreenBehavior(
    sideEffects: Flow<ProductViewModel.SideEffect>,
    showProduct: (String) -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(context, sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                is ProductViewModel.SideEffect.ShareText -> context.share(effect.text)
                is ProductViewModel.SideEffect.ShowProduct -> showProduct(effect.product.id)
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
            onShareClick = {},
            completeLookProducts = List(5) { product },
            onProductClick = {},
            cache = remember { mutableStateOf(null) },
            deliveryAvailability = DeliveryAvailability(
                cityName = "Санкт-Петербург",
                options = listOf(
                    DeliveryAvailability.Option(
                        type = DeliveryAvailability.Option.Type.EXPRESS,
                        name = "Курьерская доставка",
                        estimatedTimeDays = 1
                    ),
                    DeliveryAvailability.Option(
                        type = DeliveryAvailability.Option.Type.POST,
                        name = "Почта России",
                        estimatedTimeDays = 4
                    ),
                    DeliveryAvailability.Option(
                        type = DeliveryAvailability.Option.Type.PICKUP,
                        name = "Пункт самовывоза",
                        estimatedTimeDays = 2
                    ),
                    DeliveryAvailability.Option(
                        type = DeliveryAvailability.Option.Type.RETAIL,
                        name = "Забрать из магазина",
                        estimatedTimeDays = 3
                    ),
                )
            ),
        )
    }
}
