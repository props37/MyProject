package ru.livetyping.zarina.ui.screen.product

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.domain.common.Media
import ru.livetyping.zarina.domain.product.ProductDetails
import ru.livetyping.zarina.ui.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.ui.common.component.media.ZarinaMediaHorizontalPager
import ru.livetyping.zarina.ui.common.component.pager.ZarinaHorizontalPagerIndicator
import ru.livetyping.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.ui.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.ui.screen.product.ProductViewModel.ProductState
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade
import ru.livetyping.zarina.util.compose.pager.rememberEndlessPagerState

object ProductScreenComponents {

    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        productName: String?,
        mode: TopBarMode,
        modifier: Modifier = Modifier,
    ) {
        val contentAlpha by animateFloatAsState(
            targetValue = when (mode) {
                TopBarMode.Transparent -> 0f
                TopBarMode.Filled -> 1f
            },
            label = "content alpha",
        )
        val backgroundColor =
            UiKitTheme.colors.background.general.regular.default.copy(alpha = contentAlpha)

        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = productName.orEmpty(),
                    color = UiKitTheme.colors.text.general.regular.default.copy(alpha = contentAlpha),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            backgroundColor = backgroundColor,
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @Composable
    fun ProductDetails(
        productState: ProductState,
        onProductErrorRefreshClicked: () -> Unit,
        lazyListState: LazyListState,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = productState,
            contentKey = {
                when (it) {
                    is ProductState.Success -> ProductDetailsContentKeySuccess
                    else -> it
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is ProductState.Success -> {
                    ProductDetailsImpl(
                        product = state.product,
                        lazyListState = lazyListState,
                    )
                }

                ProductState.Loading -> {
                    // TODO: [High] Implement
                }

                is ProductState.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onProductErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun ProductDetailsImpl(
        product: ProductDetails,
        lazyListState: LazyListState,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = modifier,
        ) {
            item(
                key = ProductDetailsListKeyMediaPager,
                contentType = ProductDetailsListContentTypeMediaPager,
            ) {
                MediaPagerItem(media = product.media)
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun MediaPagerItem(
        media: List<Media>,
        modifier: Modifier = Modifier,
    ) {
        val pagerState = rememberEndlessPagerState(itemCount = media.size)
        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(MediaPagerAspectRatio),
        ) {
            ZarinaMediaHorizontalPager(
                pagerState = pagerState,
                media = media,
                modifier = Modifier.matchParentSize(),
            )

            ZarinaHorizontalPagerIndicator(
                pagerState = pagerState,
                itemCount = media.size,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp),
            )
        }
    }

    @Composable
    fun topBarModeAsState(lazyListState: LazyListState): State<TopBarMode> {
        return remember {
            derivedStateOf {
                val visibleItemsInfo = lazyListState.layoutInfo.visibleItemsInfo
                val firstVisibleItemKey = visibleItemsInfo.firstOrNull()?.key
                if (firstVisibleItemKey == ProductDetailsListKeyMediaPager) {
                    TopBarMode.Transparent
                } else {
                    TopBarMode.Filled
                }
            }
        }
    }

    enum class TopBarMode { Transparent, Filled }

    private const val MediaPagerAspectRatio = 0.7f

    private const val ProductDetailsContentKeySuccess = "ProductDetailsContentKeySuccess"

    private const val ProductDetailsListKeyMediaPager = "ProductDetailsListKeyMediaPager"

    private const val ProductDetailsListContentTypeMediaPager =
        "ProductDetailsListContentTypeMediaPager"
}
