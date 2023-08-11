package ru.zarina.zarina.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.datasource.cache.Cache
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.domain.Banner
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Selection
import ru.zarina.zarina.domain.Url
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.ElevationContainer
import ru.zarina.zarina.ui.common.components.MediaPager
import ru.zarina.zarina.ui.common.components.PageDots
import ru.zarina.zarina.ui.common.components.ProductHorizontalSection
import ru.zarina.zarina.ui.common.components.RedirectSearchBar
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.bottomNavigationPaddingValues
import ru.zarina.zarina.ui.common.components.rememberInfinitePagerState
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.utils.compose.plus


@Composable
fun HomeScreenContent(
    banners: ImmutableList<Banner>,
    selections: ImmutableList<Selection>,
    shakingFavorites: ImmutableSet<Product.Id>,
    onBannerClick: (Banner) -> Unit,
    onProductClick: (Product) -> Unit,
    onFavoriteChange: (Product, Boolean) -> Unit,
    cache: State<Cache?>,
) {
    val lazyColumnState = rememberLazyListState()
    ZarinaScaffold(
        toolbar = {
            ElevationContainer(
                isElevated = lazyColumnState.canScrollBackward,
                modifier = Modifier
            ) {
                RedirectSearchBar(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                )
            }
        }
    ) {
        LazyColumn(
            state = lazyColumnState,
            contentPadding = WindowInsets.navigationBars.asPaddingValues()
                    + bottomNavigationPaddingValues(),
            modifier = Modifier
                .fillMaxSize()
                .background(UiKitTheme.colors.screenBackground),
        ) {
            item(
                key = LAZY_KEY_BANNERS,
                contentType = ContentType.BANNERS,
            ) {
                Banners(
                    banners = banners,
                    cache = cache,
                    onBannerClick = onBannerClick,
                )
            }
            items(
                items = selections,
                contentType = { selection ->
                    when (selection) {
                        is Selection.Banners -> ContentType.BANNERS
                        is Selection.Products -> ContentType.PRODUCTS
                    }
                }
            ) { selection ->
                when (selection) {
                    is Selection.Banners -> Banners(
                        banners = selection.banners,
                        cache = cache,
                        onBannerClick = onBannerClick,
                    )

                    is Selection.Products -> ProductHorizontalSection(
                        title = selection.title,
                        products = selection.products,
                        shakingFavorites = shakingFavorites,
                        onProductClick = onProductClick,
                        onFavoriteChange = onFavoriteChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

private const val LAZY_KEY_BANNERS = "lazy_key_banners"

private enum class ContentType { BANNERS, PRODUCTS }

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Banners(
    banners: ImmutableList<Banner>,
    onBannerClick: (Banner) -> Unit,
    cache: State<Cache?>,
    modifier: Modifier = Modifier,
) {
    if (banners.isNotEmpty())
        Box(
            modifier = modifier.fillMaxWidth(),
        ) {
            val pagerState = rememberInfinitePagerState(banners.size)
            MediaPager(
                media = banners.map { it.media }.toPersistentList(),
                onMediaClick = { media ->
                    val banner = banners.firstOrNull { it.media == media }
                    banner?.let { onBannerClick(it) }
                },
                aspectRatio = Media.Defaults.BANNER_MEDIA_ASPECT_RATIO,
                state = pagerState,
                cache = cache,
                modifier = Modifier.fillMaxWidth(),
            )
            if (banners.size > 1)
                PageDots(
                    count = banners.size,
                    activeIndex = pagerState.currentPage % banners.size,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(10.dp),
                )
        }
}

@Composable
fun HomeScreen(
    showProduct: (id: Product.Id) -> Unit,
    showProducts: (categoryId: Category.Id, filtration: Filtration?) -> Unit,
    showWebpage: (url: Url) -> Unit,
) {
    val viewModel = koinViewModel<HomeViewModel>()

    val banners by viewModel.banners.collectAsStateWithLifecycle()
    val selections by viewModel.selections.collectAsStateWithLifecycle()
    val cache = viewModel.cache.collectAsStateWithLifecycle()
    val shakingFavorites by viewModel.shakingFavorites.collectAsStateWithLifecycle()

    HomeScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showProduct = showProduct,
        showProducts = showProducts,
        showWebpage = showWebpage,
    )

    HomeScreenContent(
        banners = banners,
        selections = selections,
        shakingFavorites = shakingFavorites,
        onBannerClick = remember { { viewModel.onBannerClick(it) } },
        onProductClick = remember { { viewModel.onProductClick(it) } },
        onFavoriteChange = remember {
            { product, isFavorite ->
                viewModel.onFavoriteChange(product, isFavorite)
            }
        },
        cache = cache,
    )
}

@Composable
fun HomeScreenBehavior(
    sideEffects: Flow<HomeViewModel.SideEffect>,
    showProduct: (id: Product.Id) -> Unit,
    showProducts: (categoryId: Category.Id, filtration: Filtration?) -> Unit,
    showWebpage: (url: Url) -> Unit,
) {
    NavigationBarState(isVisible = true, isAnimated = true)
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                is HomeViewModel.SideEffect.ShowProduct ->
                    showProduct(effect.productId)

                is HomeViewModel.SideEffect.ShowProducts ->
                    showProducts(effect.categoryId, effect.filtration)

                is HomeViewModel.SideEffect.ShowWebpage ->
                    showWebpage(effect.url)
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun HomeScreenContentPreview() {
    ZarinaTheme {
        HomeScreenContent(
            banners = persistentListOf(),
            selections = persistentListOf(),
            shakingFavorites = persistentSetOf(),
            onBannerClick = {},
            onProductClick = {},
            onFavoriteChange = { _, _ -> },
            cache = remember { mutableStateOf<Cache?>(null) },
        )
    }
}
