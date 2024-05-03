package ru.livetyping.zarina.ui.screen.product

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Media
import ru.livetyping.zarina.domain.product.Price
import ru.livetyping.zarina.domain.product.ProductDetails
import ru.livetyping.zarina.ui.common.component.ProductPrice
import ru.livetyping.zarina.ui.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.ui.common.component.button.ZarinaIconButton
import ru.livetyping.zarina.ui.common.component.media.ZarinaMediaHorizontalPager
import ru.livetyping.zarina.ui.common.component.pager.ZarinaHorizontalPagerIndicator
import ru.livetyping.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.ui.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.ui.common.util.domain.toComposeColor
import ru.livetyping.zarina.ui.screen.product.ProductViewModel.ProductState
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade
import ru.livetyping.zarina.util.compose.pager.rememberEndlessPagerState

object ProductScreenComponents {

    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        productName: String?,
        onShareClicked: () -> Unit,
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
                    iconSize = TopBarIconSize,
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
            endContent = {
                ZarinaIconButton(
                    onClick = onShareClicked,
                    indication = rememberRipple(bounded = false, radius = TopBarIconSize),
                    modifier = Modifier.padding(end = 2.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_share_24),
                        contentDescription = stringResource(R.string.share),
                        modifier = Modifier.size(TopBarIconSize),
                    )
                }
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

            item(
                key = ProductDetailsListKeyGeneralInfo,
                contentType = ProductDetailsListContentTypeGeneralInfo,
            ) {
                ProductGeneralInfoItem(
                    name = product.name,
                    label = product.label,
                    price = product.price,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
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
    private fun ProductGeneralInfoItem(
        name: String,
        label: ProductDetails.Label?,
        price: Price,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = name.uppercase(),
                    style = UiKitTheme.typography.caption1.regular,
                    color = UiKitTheme.colors.text.general.regular.default,
                )

                if (label != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label.name,
                        style = UiKitTheme.typography.caption1.bold,
                        color = label.color.toComposeColor(),
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            ProductPrice(price = price)

            // TODO: [High] Add colors
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

    private val TopBarIconSize: Dp get() = 20.dp

    private const val MediaPagerAspectRatio = 0.7f

    private const val ProductDetailsContentKeySuccess = "ProductDetailsContentKeySuccess"

    private const val ProductDetailsListKeyMediaPager = "ProductDetailsListKeyMediaPager"
    private const val ProductDetailsListKeyGeneralInfo = "ProductDetailsListKeyGeneralInfo"

    private const val ProductDetailsListContentTypeMediaPager =
        "ProductDetailsListContentTypeMediaPager"
    private const val ProductDetailsListContentTypeGeneralInfo =
        "ProductDetailsListContentTypeGeneralInfo"
}
