package ru.livetyping.zarina.ui.screen.product

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Media
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.ProductDetails
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.ui.common.component.ProductCardSmall
import ru.livetyping.zarina.ui.common.component.ProductColorSelector
import ru.livetyping.zarina.ui.common.component.ProductPrice
import ru.livetyping.zarina.ui.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.ui.common.component.button.ZarinaIconButton
import ru.livetyping.zarina.ui.common.component.item.ZarinaExpandableItem
import ru.livetyping.zarina.ui.common.component.media.ZarinaMediaHorizontalPager
import ru.livetyping.zarina.ui.common.component.pager.ZarinaHorizontalPagerIndicator
import ru.livetyping.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.ui.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.ui.common.util.domain.toComposeColor
import ru.livetyping.zarina.ui.common.util.rememberFormattedPrice
import ru.livetyping.zarina.ui.screen.product.ProductViewModel.ProductState
import ru.livetyping.zarina.ui.screen.product.ProductViewModel.ProductTotalLookState
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade
import ru.livetyping.zarina.util.compose.getHorizontalPaddingValues
import ru.livetyping.zarina.util.compose.getVerticalPaddingValues
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
        productTotalLookState: ProductTotalLookState,
        onProductTotalLookErrorRefreshClicked: () -> Unit,
        onUrlClicked: (Url) -> Unit,
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
                        productTotalLookState = productTotalLookState,
                        onProductTotalLookErrorRefreshClicked = onProductTotalLookErrorRefreshClicked,
                        onUrlClicked = onUrlClicked,
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
        productTotalLookState: ProductTotalLookState,
        onProductTotalLookErrorRefreshClicked: () -> Unit,
        onUrlClicked: (Url) -> Unit,
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
                ProductMediaPager(media = product.media)
            }

            item(
                key = ProductDetailsListKeyGeneralInfo,
                contentType = ProductDetailsListContentTypeGeneralInfo,
            ) {
                ProductGeneralInfo(
                    product = product,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                )
            }

            item(
                key = ProductDetailsListKeyDescription,
                contentType = ProductDetailsListContentTypeDescription,
            ) {
                ProductDescription(
                    description = product.description,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item(
                key = ProductDetailsListKeyDeliveryAndPayment,
                contentType = ProductDetailsListContentTypeDeliveryAndPayment,
            ) {
                ProductDeliveryAndPayment(
                    freeDeliveryTotalPriceThreshold = product.freeDeliveryTotalPriceThreshold,
                    onUrlClicked = onUrlClicked,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item(
                key = ProductDetailsListKeyTotalLook,
                contentType = ProductDetailsListContentTypeTotalLook,
            ) {
                ProductTotalLook(
                    totalLookState = productTotalLookState,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun ProductMediaPager(
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
    private fun ProductGeneralInfo(
        product: ProductDetails,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                Text(
                    text = product.name.uppercase(),
                    style = UiKitTheme.typography.caption1.regular,
                    color = UiKitTheme.colors.text.general.regular.default,
                )

                if (product.label != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = product.label.name.uppercase(),
                        style = UiKitTheme.typography.caption1.bold,
                        color = product.label.color.toComposeColor()
                            ?: UiKitTheme.colors.text.general.regular.default,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            ProductPrice(
                price = product.price,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            ProductColorSelector(
                productId = product.id,
                productColors = product.colors,
                onProductColorClicked = { /* TODO */ },
                contentPadding = PaddingValues(horizontal = 10.dp),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    @Composable
    private fun ProductDescription(
        description: List<ProductDetails.DescriptionEntry>,
        modifier: Modifier = Modifier,
    ) {
        ZarinaExpandableItem(
            title = {
                Text(text = stringResource(R.string.product_details))
            },
            modifier = modifier,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                description.forEach { descriptionEntry ->
                    val text = rememberProductDescriptionEntryText(descriptionEntry)
                    Text(text = text)
                }
            }
        }
    }

    @OptIn(ExperimentalTextApi::class)
    @Composable
    private fun ProductDeliveryAndPayment(
        freeDeliveryTotalPriceThreshold: Int,
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaExpandableItem(
            title = {
                Text(text = stringResource(R.string.delivery_and_payment))
            },
            modifier = modifier,
        ) {
            val baseText = stringResource(
                id = R.string.product_delivery_and_payment_info,
                rememberFormattedPrice(freeDeliveryTotalPriceThreshold.toLong()),
            )
            val baseTextStyle = UiKitTheme.typography.tertiary.light

            val clickableDeliveryText =
                stringResource(R.string.product_delivery_and_payment_info_delivery)
            val clickablePaymentText =
                stringResource(R.string.product_delivery_and_payment_info_payment)
            val clickableTextStyle = UiKitTheme.typography.tertiary.regular
            val deliveryAndPaymentUrl =
                stringResource(R.string.product_delivery_and_payment_info_delivery_payment_url)

            val text = remember(
                baseText,
                baseTextStyle,
                clickableDeliveryText,
                clickablePaymentText,
                clickableTextStyle,
                deliveryAndPaymentUrl,
            ) {
                buildAnnotatedString {
                    withStyle(baseTextStyle.toSpanStyle()) {
                        append(baseText)
                    }

                    val string = this.toAnnotatedString()
                    val clickableDeliveryTextStartIndex = string.lastIndexOf(clickableDeliveryText)
                    val clickablePaymentTextStartIndex = string.lastIndexOf(clickablePaymentText)

                    val clickableSpanStyle = clickableTextStyle.toSpanStyle()
                    if (clickableDeliveryTextStartIndex != -1) {
                        val end = clickableDeliveryTextStartIndex + clickableDeliveryText.length
                        addStyle(
                            style = clickableSpanStyle,
                            start = clickableDeliveryTextStartIndex,
                            end = end,
                        )
                        addUrlAnnotation(
                            urlAnnotation = UrlAnnotation(deliveryAndPaymentUrl),
                            start = clickableDeliveryTextStartIndex,
                            end = end,
                        )
                    }
                    if (clickablePaymentTextStartIndex != -1) {
                        val end = clickablePaymentTextStartIndex + clickablePaymentText.length
                        addStyle(
                            style = clickableSpanStyle,
                            start = clickablePaymentTextStartIndex,
                            end = end,
                        )
                        addUrlAnnotation(
                            urlAnnotation = UrlAnnotation(deliveryAndPaymentUrl),
                            start = clickablePaymentTextStartIndex,
                            end = end,
                        )
                    }
                }
            }

            ClickableText(
                text = text,
            ) { offset ->
                val annotation = text.getUrlAnnotations(offset, offset).firstOrNull()
                if (annotation != null) {
                    val url = Url(annotation.item.url)
                    onUrlClicked(url)
                }
            }
        }
    }

    @Composable
    private fun ProductTotalLook(
        totalLookState: ProductTotalLookState,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(),
    ) {
        Column(
            modifier = modifier.padding(contentPadding.getVerticalPaddingValues()),
        ) {
            val layoutDirection = LocalLayoutDirection.current
            Text(
                text = stringResource(R.string.product_total_look),
                style = UiKitTheme.typography.secondary.bold,
                modifier = Modifier
                    .padding(contentPadding.getHorizontalPaddingValues(layoutDirection)),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Crossfade(
                targetState = totalLookState,
                contentKey = { state ->
                    when (state) {
                        is ProductTotalLookState.Success -> ProductTotalLookContentKeySuccess
                        else -> state
                    }
                },
            ) { state ->
                when (state) {
                    is ProductTotalLookState.Success -> {
                        ProductTotalLookImpl(
                            totalLook = state.totalLook,
                            contentPadding = contentPadding.getHorizontalPaddingValues(layoutDirection),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    ProductTotalLookState.Loading -> {
                        // TODO: [High] Implement
                    }

                    ProductTotalLookState.Error -> {
                        // TODO: [High] Implement
                    }
                }
            }
        }
    }

    @Composable
    private fun ProductTotalLookImpl(
        totalLook: ImmutableList<ProductItem>,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(),
    ) {
        LazyRow(
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier,
        ) {
            items(
                items = totalLook,
                key = { it.id.value },
            ) { product ->
                ProductCardSmall(
                    product = product,
                    onClick = { /* TODO */ },
                    modifier = Modifier.width(SuggestedProductCardWidth),
                )
            }
        }
    }

    @Composable
    private fun rememberProductDescriptionEntryText(
        descriptionEntry: ProductDetails.DescriptionEntry,
    ): AnnotatedString {
        val titleStyle = UiKitTheme.typography.tertiary.regular
        val bodyStyle = UiKitTheme.typography.tertiary.light
        return remember(descriptionEntry, titleStyle, bodyStyle) {
            buildAnnotatedString {
                withStyle(titleStyle.toSpanStyle()) {
                    append(descriptionEntry.title)
                    append(Colon)
                }
                withStyle(bodyStyle.toSpanStyle()) {
                    append(Space)
                    append(descriptionEntry.body)
                }
            }
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
    private const val ProductDetailsListKeyDescription = "ProductDetailsListKeyDescription"
    private const val ProductDetailsListKeyDeliveryAndPayment =
        "ProductDetailsListKeyDeliveryAndPayment"
    private const val ProductDetailsListKeyTotalLook = "ProductDetailsListKeyTotalLook"

    private const val ProductDetailsListContentTypeMediaPager =
        "ProductDetailsListContentTypeMediaPager"
    private const val ProductDetailsListContentTypeGeneralInfo =
        "ProductDetailsListContentTypeGeneralInfo"
    private const val ProductDetailsListContentTypeDescription =
        "ProductDetailsListContentTypeDescription"
    private const val ProductDetailsListContentTypeDeliveryAndPayment =
        "ProductDetailsListContentTypeDeliveryAndPayment"
    private const val ProductDetailsListContentTypeTotalLook =
        "ProductDetailsListContentTypeTotalLook"

    private const val ProductTotalLookContentKeySuccess = "ProductTotalLookContentKeySuccess"

    private val SuggestedProductCardWidth: Dp get() = 176.dp

    private const val Colon = ':'
    private const val Space = ' '
}
