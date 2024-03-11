package ru.zarina.zarina.ui.screens.product

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.old.DeliveryAvailability
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.ui.common.base.ErrorState
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.ProductHorizontalSection
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.toolbar.BackButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.DeliveryAvailabilityProvider
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.screens.product.components.sections.ColorsSection
import ru.zarina.zarina.ui.screens.product.components.sections.DeliveryAvailabilitySection
import ru.zarina.zarina.ui.screens.product.components.sections.DetailsSection
import ru.zarina.zarina.ui.screens.product.components.sections.MediaSection
import ru.zarina.zarina.ui.screens.product.components.sections.PickupSection
import ru.zarina.zarina.ui.screens.product.components.sections.PriceSection
import ru.zarina.zarina.ui.screens.product.components.sections.ShareSection
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.old.ZarinaTheme
import ru.zarina.zarina.utils.android.share
import java.util.UUID

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ProductScreenContent(
    product: Product?,
    shakingFavorites: ImmutableSet<Product.Id>,
    onFavoriteChange: (Product, Boolean) -> Unit,
    onVariantClick: (Product.Variant) -> Unit,
    onShareClick: () -> Unit,
    onPickupClick: (Product) -> Unit,
    completeLookProducts: ImmutableList<Product>,
    similarProducts: ImmutableList<Product>,
    onProductClick: (Product) -> Unit,
    deliveryAvailability: DeliveryAvailability?,
    onBackClick: () -> Unit,
    isProductLoaderVisible: Boolean,
    errorType: ProductViewModel.ErrorType?,
    onRefreshClick: () -> Unit,
    cache: State<Cache?>,
) {
    val errorState = when (errorType) {
        ProductViewModel.ErrorType.NETWORK -> ErrorState.NETWORK
        ProductViewModel.ErrorType.NOT_FOUND -> ErrorState(
            icon = R.drawable.ic_magnifying_glass_96,
            title = ru.zarina.zarina.ui.common.base.Text.Resource(R.string.product_not_on_sale),
            subtitle = ru.zarina.zarina.ui.common.base.Text.Resource(R.string.dont_fret_catalog),
        )

        ProductViewModel.ErrorType.GENERIC -> ErrorState.GENERIC
        null -> null
    }
    val scrollState = rememberScrollState()
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = { ToolbarTitle(product = product) },
                startIcon = {
                    BackButton(onBackClick)
                },
                isElevated = scrollState.canScrollBackward,
                modifier = Modifier.fillMaxWidth()
            )
        },
        errorState = errorState,
        isModalLoaderVisible = isProductLoaderVisible,
        onErrorButtonClick = onRefreshClick,
    ) {
        if (product != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
            ) {
                val coroutineScope = rememberCoroutineScope()
                val completeLookRequester = remember { BringIntoViewRequester() }
                MediaSection(
                    product = product,
                    isFavoriteShaking = shakingFavorites.contains(product.id),
                    onFavoriteChange = { onFavoriteChange(product, it) },
                    onBuyCompleteLookClick = {
                        coroutineScope.launch {
                            completeLookRequester.bringIntoView()
                        }
                    },
                    cache = cache,
                    modifier = Modifier.fillMaxWidth()
                )
                PriceSection(
                    price = product.price,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
                ColorsSection(
                    product = product,
                    onVariantClick = onVariantClick,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                PickupSection(
                    onPickupClick = { onPickupClick(product) },
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                )
                Divider(
                    color = UiKitTheme.colorsOld.listDivider,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                DetailsSection(
                    description = product.description,
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(
                    color = UiKitTheme.colorsOld.listDivider,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                if (completeLookProducts.isNotEmpty())
                    ProductHorizontalSection(
                        title = stringResource(R.string.complete_look),
                        products = completeLookProducts,
                        shakingFavorites = shakingFavorites,
                        onProductClick = onProductClick,
                        onFavoriteChange = onFavoriteChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .bringIntoViewRequester(completeLookRequester)
                    )
                if (similarProducts.isNotEmpty())
                    ProductHorizontalSection(
                        title = stringResource(R.string.similar_products),
                        products = similarProducts,
                        shakingFavorites = shakingFavorites,
                        onProductClick = onProductClick,
                        onFavoriteChange = onFavoriteChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                DeliveryAvailabilitySection(
                    deliveryAvailability = deliveryAvailability,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                )
                if (product.url != null)
                    ShareSection(
                        onShareClick = onShareClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
private fun ToolbarTitle(
    product: Product?,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = product?.name.orEmpty(),
            style = UiKitTheme.typography.circle1718,
            color = UiKitTheme.colorsOld.primaryContentColor,
            maxLines = 1,
        )
        val attribute = product?.attributes?.firstOrNull()
        if (attribute != null) {
            Text(
                text = attribute.uppercase(),
                style = UiKitTheme.typography.circle811,
                color = UiKitTheme.colorsOld.primaryContentColor,
                maxLines = 1,
            )
        }
    }
}

@Composable
fun ProductScreen(
    showProduct: (Product.Id) -> Unit,
    showPickup: (Product.Id) -> Unit,
    goBack: () -> Unit,
) {
    val viewModel = koinViewModel<ProductViewModel>()

    val cache = viewModel.cache.collectAsStateWithLifecycle()
    val product by viewModel.product.collectAsStateWithLifecycle()
    val completeLookProducts by viewModel.completeLookProducts.collectAsStateWithLifecycle()
    val similarProducts by viewModel.similarProducts.collectAsStateWithLifecycle()
    val deliveryAvailability by viewModel.deliveryAvailability.collectAsStateWithLifecycle()
    val isProductLoaderVisible by viewModel.isProductLoaderVisible.collectAsStateWithLifecycle()
    val errorType by viewModel.errorType.collectAsStateWithLifecycle()
    val shakingFavorites by viewModel.shakingFavorites.collectAsStateWithLifecycle()

    ProductScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showProduct = showProduct,
        showPickup = showPickup,
        goBack = goBack,
    )

    ProductScreenContent(
        product = product,
        shakingFavorites = shakingFavorites,
        onFavoriteChange = viewModel::onFavoriteChange,
        onVariantClick = viewModel::onVariantClick,
        onShareClick = viewModel::onShareClick,
        onPickupClick = viewModel::onPickupClick,
        completeLookProducts = completeLookProducts,
        similarProducts = similarProducts,
        onProductClick = viewModel::onProductClick,
        deliveryAvailability = deliveryAvailability,
        onBackClick = viewModel::onBackClick,
        isProductLoaderVisible = isProductLoaderVisible,
        errorType = errorType,
        onRefreshClick = viewModel::onRefreshClick,
        cache = cache
    )
}

@Composable
fun ProductScreenBehavior(
    sideEffects: Flow<ProductViewModel.SideEffect>,
    showProduct: (Product.Id) -> Unit,
    showPickup: (Product.Id) -> Unit,
    goBack: () -> Unit,
) {
    NavigationBarState(isVisible = false, isAnimated = false)
    val context = LocalContext.current
    LaunchedEffect(context, sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                is ProductViewModel.SideEffect.ShareText -> context.share(effect.text)
                is ProductViewModel.SideEffect.ShowProduct -> showProduct(effect.product.id)
                is ProductViewModel.SideEffect.ShowPickup -> showPickup(effect.product.id)
                ProductViewModel.SideEffect.GoBack -> goBack()
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
    val deliveryAvailability = remember { DeliveryAvailabilityProvider().values.first() }
    ZarinaTheme {
        ProductScreenContent(
            product = product,
            shakingFavorites = persistentSetOf(),
            onFavoriteChange = { _, _ -> },
            onVariantClick = {},
            onShareClick = {},
            completeLookProducts = List(5) {
                product.copy(
                    id = Product.Id(
                        UUID.randomUUID().toString()
                    )
                )
            }.toPersistentList(),
            similarProducts = List(5) {
                product.copy(
                    id = Product.Id(
                        UUID.randomUUID().toString()
                    )
                )
            }.toPersistentList(),
            onProductClick = {},
            onPickupClick = {},
            deliveryAvailability = deliveryAvailability,
            onBackClick = {},
            isProductLoaderVisible = false,
            errorType = null,
            onRefreshClick = {},
            cache = remember { mutableStateOf(null) },
        )
    }
}
