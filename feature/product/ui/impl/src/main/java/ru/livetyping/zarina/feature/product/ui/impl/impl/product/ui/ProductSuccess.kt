package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.core.uicompose.list.canScroll
import ru.livetyping.zarina.core.uikit.blur.StatusBarBlur
import ru.livetyping.zarina.core.uikit.blur.StatusBarBlurDefaults
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.R
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductState

@Composable
internal fun ProductSuccess(
    state: ProductState.Success,
    onEvent: (ProductEvent) -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    bottomPaddingProvider: @Composable () -> Dp,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val topBarScrollBehavior = CollapsingTopBarDefaults.rememberEnterAlwaysScrollBehavior(
        canScroll = { listState.canScroll },
        scrollBeforeContent = { false },
    )

    Box(modifier = modifier) {
        val hazeState = rememberHazeState(StatusBarBlurDefaults.isStatusBarBlurEnabled())

        StatusBarBlur(
            hazeState = hazeState,
            modifier = Modifier.zIndex(1f),
        )

        CollapsingTopBarLayout(
            topBar = {
                val firstVisibleItemKey by remember(listState) {
                    derivedStateOf { listState.layoutInfo.visibleItemsInfo.firstOrNull()?.key }
                }
                val isBackgroundTransparent = firstVisibleItemKey == ContentListKey.MediaPager
                        || firstVisibleItemKey == null
                val backgroundAlpha by animateFloatAsState(
                    targetValue = if (isBackgroundTransparent) 0f else 1f,
                )

                TopBar(
                    onBackClicked = { onEvent(ProductEvent.BackClicked) },
                    onShareClicked = { onEvent(ProductEvent.ShareClicked) },
                    backgroundAlphaProvider = { backgroundAlpha },
                )
            },
            scrollBehavior = topBarScrollBehavior,
            modifier = Modifier.hazeSource(hazeState),
        ) {
            val contentPadding = PaddingValues(
                top = windowInsetsProvider().asPaddingValues().calculateTopPadding(),
                bottom = bottomPaddingProvider() + ZarinaScrollableDefaults.ScrollableBottomPadding,
            )

            ContentList(
                state = state,
                onEvent = onEvent,
                listState = listState,
                contentPadding = contentPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
            )
        }
    }
}

@Composable
private fun ContentList(
    state: ProductState.Success,
    onEvent: (ProductEvent) -> Unit,
    listState: LazyListState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        item(key = ContentListKey.MediaPager, contentType = ContentListContentType.MediaPager) {
            MediaPager(mediaList = state.product.media)
        }

        if (state.labels != null) {
            item(key = ContentListKey.LabelList, contentType = ContentListContentType.LabelList) {
                LabelList(
                    labels = state.labels,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .padding(horizontal = 16.dp),
                )
            }
        }

        item(key = ContentListKey.ProductName, contentType = ContentListContentType.ProductName) {
            val topPadding = if (state.labels != null) 12.dp else 20.dp

            ProductName(
                state.product.name.uppercase(),
                modifier = Modifier
                    .padding(top = topPadding)
                    .padding(horizontal = 16.dp),
            )
        }

        item(key = ContentListKey.PriceBlock, contentType = ContentListContentType.PriceBlock) {
            PriceBlock(
                price = state.product.price,
                podeliPrice = state.product.podeliPrice,
                bonusAccrualForPurchase = state.product.bonusAccrualForPurchase,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 16.dp),
            )
        }

        item(key = ContentListKey.ColorSelector, contentType = ContentListContentType.ColorSelector) {
            ColorSelector(
                colors = state.product.colors,
                selectedColorProductId = state.product.id,
                onColorClicked = { onEvent(ProductEvent.ProductColorClicked(it)) },
                modifier = Modifier.padding(top = 16.dp),
            )
        }

        if (state.mediaBanner != null) {
            item(key = ContentListKey.MediaBanner, contentType = ContentListContentType.MediaBanner) {
                Media(
                    media = state.mediaBanner,
                    modifier = Modifier.padding(top = 40.dp),
                )
            }
        }

        item(key = ContentListKey.ProductDetails, contentType = ContentListContentType.ProductDetails) {
            Column {
                ProductDetailsBlock(product = state.product)

                ZarinaDivider(
                    color = UiKitTheme2.colors.gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UiKitTheme2.colors.lightGray)
                        .padding(horizontal = 16.dp),
                )
            }
        }

        item(
            key = ContentListKey.DeliveryAndPaymentBlock,
            contentType = ContentListContentType.DeliveryAndPaymentBlock,
        ) {
            DeliveryAndPaymentBlock(
                freeDeliveryThreshold = state.product.freeDeliveryTotalPriceThreshold,
            )
        }

        item(key = ContentListKey.TotalLookProducts, contentType = ContentListContentType.SuggestionList) {
            SuggestionList(
                state = state.totalLookProductState,
                title = stringResource(R.string.product_suggestions_title_total_look).uppercase(),
                onProductClicked = { onEvent(ProductEvent.ProductClicked(it)) },
                onAddToWishlistClicked = { onEvent(ProductEvent.AddProductToWishlistClicked(it)) },
                onErrorRetryClicked = { onEvent(ProductEvent.TotalLookProductRefreshTriggered) },
                modifier = Modifier.padding(top = 28.dp),
            )
        }

        item(key = ContentListKey.SimilarProducts, contentType = ContentListContentType.SuggestionList) {
            SuggestionList(
                state = state.similarProductState,
                title = stringResource(R.string.product_suggestions_title_similar_products).uppercase(),
                onProductClicked = { onEvent(ProductEvent.ProductClicked(it)) },
                onAddToWishlistClicked = { onEvent(ProductEvent.AddProductToWishlistClicked(it)) },
                onErrorRetryClicked = { onEvent(ProductEvent.SimilarProductRefreshTriggered) },
                modifier = Modifier.padding(top = 28.dp),
            )
        }
    }
}

private enum class ContentListKey {
    MediaPager,
    LabelList,
    ProductName,
    PriceBlock,
    ColorSelector,
    MediaBanner,
    ProductDetails,
    DeliveryAndPaymentBlock,
    TotalLookProducts,
    SimilarProducts,
}

private enum class ContentListContentType {
    MediaPager,
    LabelList,
    ProductName,
    PriceBlock,
    ColorSelector,
    MediaBanner,
    ProductDetails,
    DeliveryAndPaymentBlock,
    SuggestionList,
}
