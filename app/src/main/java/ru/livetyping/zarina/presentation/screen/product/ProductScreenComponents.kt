package ru.livetyping.zarina.presentation.screen.product

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.RectangleShape
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
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Media
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductColor
import ru.livetyping.zarina.domain.product.ProductDetails
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.presentation.common.component.ProductCardSmall
import ru.livetyping.zarina.presentation.common.component.ProductCardSmallSkeleton
import ru.livetyping.zarina.presentation.common.component.ProductColorSelector
import ru.livetyping.zarina.presentation.common.component.ProductColorSelectorSkeleton
import ru.livetyping.zarina.presentation.common.component.ProductPrice
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaIconButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaLikeIconButton
import ru.livetyping.zarina.presentation.common.component.item.ZarinaExpandableItem
import ru.livetyping.zarina.presentation.common.component.list.ZarinaListErrorItem
import ru.livetyping.zarina.presentation.common.component.media.ZarinaMediaHorizontalPager
import ru.livetyping.zarina.presentation.common.component.pager.ZarinaHorizontalPagerIndicator
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.util.domain.toComposeColor
import ru.livetyping.zarina.presentation.common.util.rememberFormattedPrice
import ru.livetyping.zarina.presentation.screen.product.ProductViewModel.ProductState
import ru.livetyping.zarina.presentation.screen.product.ProductViewModel.SuggestedProductListState
import ru.livetyping.zarina.presentation.theme.UiKitTheme
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
        onProductColorClicked: (ProductColor) -> Unit,
        onAddProductToCartClicked: (Product) -> Unit,
        onAddProductToFavoritesClicked: (Product) -> Unit,
        onProductErrorRefreshClicked: () -> Unit,
        onProductClicked: (Product) -> Unit,
        productTotalLookState: SuggestedProductListState,
        onProductTotalLookErrorRefreshClicked: () -> Unit,
        productSimilarState: SuggestedProductListState,
        onProductSimilarErrorRefreshClicked: () -> Unit,
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
                        onProductColorClicked = onProductColorClicked,
                        onAddProductToCartClicked = onAddProductToCartClicked,
                        onAddProductToFavoritesClicked = onAddProductToFavoritesClicked,
                        productTotalLookState = productTotalLookState,
                        onProductTotalLookErrorRefreshClicked = onProductTotalLookErrorRefreshClicked,
                        productSimilarState = productSimilarState,
                        onProductSimilarErrorRefreshClicked = onProductSimilarErrorRefreshClicked,
                        onProductClicked = onProductClicked,
                        onUrlClicked = onUrlClicked,
                        lazyListState = lazyListState,
                    )
                }

                ProductState.Loading -> {
                    ProductDetailsSkeleton(modifier = Modifier.fillMaxWidth())
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
        onProductColorClicked: (ProductColor) -> Unit,
        onAddProductToCartClicked: (Product) -> Unit,
        onAddProductToFavoritesClicked: (Product) -> Unit,
        productTotalLookState: SuggestedProductListState,
        onProductTotalLookErrorRefreshClicked: () -> Unit,
        productSimilarState: SuggestedProductListState,
        onProductSimilarErrorRefreshClicked: () -> Unit,
        onProductClicked: (Product) -> Unit,
        onUrlClicked: (Url) -> Unit,
        lazyListState: LazyListState,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ProductDetailsList(
                product = product,
                onProductColorClicked = onProductColorClicked,
                productTotalLookState = productTotalLookState,
                onProductTotalLookErrorRefreshClicked = onProductTotalLookErrorRefreshClicked,
                productSimilarState = productSimilarState,
                onProductSimilarErrorRefreshClicked = onProductSimilarErrorRefreshClicked,
                onProductClicked = onProductClicked,
                onUrlClicked = onUrlClicked,
                lazyListState = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )

            ProductDetailsBottomBar(
                isProductAvailable = product.isAvailable,
                isProductInCart = product.isInCart,
                isProductInFavorites = product.isInFavorites,
                onAddProductToCartClicked = { onAddProductToCartClicked(product) },
                onAddProductToFavoritesClicked = { onAddProductToFavoritesClicked(product) },
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    @Composable
    private fun ProductDetailsList(
        product: ProductDetails,
        onProductColorClicked: (ProductColor) -> Unit,
        productTotalLookState: SuggestedProductListState,
        onProductTotalLookErrorRefreshClicked: () -> Unit,
        productSimilarState: SuggestedProductListState,
        onProductSimilarErrorRefreshClicked: () -> Unit,
        onProductClicked: (Product) -> Unit,
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
                    onProductColorClicked = onProductColorClicked,
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

            if (productTotalLookState !is SuggestedProductListState.Empty) {
                item(
                    key = ProductDetailsListKeyTotalLook,
                    contentType = ProductDetailsListContentTypeTotalLook,
                ) {
                    SuggestedProducts(
                        title = stringResource(R.string.product_total_look),
                        state = productTotalLookState,
                        onProductClicked = onProductClicked,
                        onErrorRefreshClicked = onProductTotalLookErrorRefreshClicked,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            if (productSimilarState !is SuggestedProductListState.Empty) {
                item(
                    key = ProductDetailsListKeySimilar,
                    contentType = ProductDetailsListContentTypeSimilar,
                ) {
                    SuggestedProducts(
                        title = stringResource(R.string.you_may_like),
                        state = productSimilarState,
                        onProductClicked = onProductClicked,
                        onErrorRefreshClicked = onProductSimilarErrorRefreshClicked,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }

    @Composable
    private fun ProductDetailsBottomBar(
        isProductAvailable: Boolean,
        isProductInCart: Boolean,
        isProductInFavorites: Boolean,
        onAddProductToCartClicked: () -> Unit,
        onAddProductToFavoritesClicked: () -> Unit,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(contentPadding),
        ) {
            val buttonColors = when {
                !isProductAvailable -> ZarinaButtonDefaults.outlineColors()
                isProductInCart -> ZarinaButtonDefaults.outlineColors()
                else -> ZarinaButtonDefaults.primaryColors()
            }
            ZarinaButton(
                onClick = onAddProductToCartClicked,
                colors = buttonColors,
                modifier = Modifier.weight(1f),
            ) {
                val textResId = when {
                    !isProductAvailable -> R.string.notify_about_product_appearance
                    isProductInCart -> R.string.in_cart
                    else -> R.string.to_cart
                }
                Text(text = stringResource(textResId).uppercase())
            }

            Spacer(modifier = Modifier.width(8.dp))

            ZarinaLikeIconButton(
                isLiked = isProductInFavorites,
                onClick = onAddProductToFavoritesClicked,
                iconSize = 20.dp,
            )
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
        onProductColorClicked: (ProductColor) -> Unit,
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
                onProductColorClicked = onProductColorClicked,
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
    private fun SuggestedProducts(
        title: String,
        state: SuggestedProductListState,
        onProductClicked: (Product) -> Unit,
        onErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Column(
            modifier = modifier.padding(contentPadding.getVerticalPaddingValues()),
        ) {
            val layoutDirection = LocalLayoutDirection.current
            Text(
                text = title,
                style = UiKitTheme.typography.secondary.bold,
                modifier = Modifier
                    .padding(contentPadding.getHorizontalPaddingValues(layoutDirection)),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Crossfade(
                targetState = state,
                contentKey = { state ->
                    when (state) {
                        is SuggestedProductListState.Success -> ProductTotalLookContentKeySuccess
                        else -> state
                    }
                },
            ) { state ->
                when (state) {
                    is SuggestedProductListState.Success -> {
                        SuggestedProductsImpl(
                            totalLook = state.totalLook,
                            onProductClicked = onProductClicked,
                            contentPadding = contentPadding.getHorizontalPaddingValues(layoutDirection),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    SuggestedProductListState.Loading -> {
                        SuggestedProductsSkeleton(
                            contentPadding = contentPadding.getHorizontalPaddingValues(layoutDirection),
                        )
                    }

                    SuggestedProductListState.Error -> {
                        ZarinaListErrorItem(
                            onRetryClicked = onErrorRefreshClicked,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    SuggestedProductListState.Empty -> Unit
                }
            }
        }
    }

    @Composable
    private fun SuggestedProductsImpl(
        totalLook: ImmutableList<ProductItem>,
        onProductClicked: (Product) -> Unit,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(),
    ) {
        LazyRow(
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(SuggestedProductsSpacedBy),
            modifier = modifier,
        ) {
            items(
                items = totalLook,
                key = { it.id.value },
            ) { product ->
                ProductCardSmall(
                    product = product,
                    onClick = onProductClicked,
                    modifier = Modifier.width(SuggestedProductCardWidth),
                )
            }
        }
    }

    @Composable
    private fun ProductDetailsSkeleton(
        modifier: Modifier = Modifier,
    ) {
        val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.View)
        LazyColumn(modifier = modifier) {
            item {
                ZarinaSkeleton(
                    shimmer = shimmer,
                    shape = RectangleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(MediaPagerAspectRatio),
                )
            }

            item {
                ProductGeneralInfoSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                )
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .heightIn(min = 56.dp)
                        .padding(horizontal = 16.dp),
                ) {
                    ZarinaTextSkeleton(
                        textStyle = UiKitTheme.typography.secondary.light,
                        shimmer = shimmer,
                        modifier = Modifier.fillMaxWidth(0.5f),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    ZarinaSkeleton(
                        shimmer = shimmer,
                        modifier = Modifier.size(12.dp),
                    )
                }
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .heightIn(min = 56.dp)
                        .padding(horizontal = 16.dp),
                ) {
                    ZarinaTextSkeleton(
                        textStyle = UiKitTheme.typography.secondary.light,
                        shimmer = shimmer,
                        modifier = Modifier.fillMaxWidth(0.5f),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    ZarinaSkeleton(
                        shimmer = shimmer,
                        modifier = Modifier.size(12.dp),
                    )
                }
            }

            item {
                Box(modifier = Modifier.padding(16.dp)) {
                    ZarinaTextSkeleton(
                        textStyle = UiKitTheme.typography.secondary.bold,
                        shimmer = shimmer,
                        modifier = Modifier.width(80.dp),
                    )
                }
            }

            item {
                SuggestedProductsSkeleton(
                    shimmer = shimmer,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                Box(modifier = Modifier.padding(16.dp)) {
                    ZarinaTextSkeleton(
                        textStyle = UiKitTheme.typography.secondary.bold,
                        shimmer = shimmer,
                        modifier = Modifier.width(80.dp),
                    )
                }
            }

            item {
                SuggestedProductsSkeleton(
                    shimmer = shimmer,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    @Composable
    private fun ProductGeneralInfoSkeleton(
        shimmer: Shimmer,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption1.regular,
                shimmer = shimmer,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(fraction = 0.5f),
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.caption1.regular,
                    shimmer = shimmer,
                    modifier = Modifier.width(42.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.caption1.regular,
                    shimmer = shimmer,
                    modifier = Modifier.width(48.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.caption1.regular,
                    shimmer = shimmer,
                    modifier = Modifier.width(26.dp),
                )

                Spacer(modifier = Modifier.weight(1f))

                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.caption1.regular,
                    shimmer = shimmer,
                    modifier = Modifier.width(44.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                ZarinaSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier.size(12.dp),
                )
            }

            ProductColorSelectorSkeleton(
                shimmer = shimmer,
                modifier = Modifier.padding(start = 12.dp, top = 2.dp),
            )
        }
    }

    @Composable
    private fun SuggestedProductsSkeleton(
        modifier: Modifier = Modifier,
        shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
        contentPadding: PaddingValues = PaddingValues(),
    ) {
        LazyRow(
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(SuggestedProductsSpacedBy),
            modifier = modifier,
        ) {
            items(count = SuggestedProductsSkeletonCount) {
                ProductCardSmallSkeleton(
                    shimmer = shimmer,
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
    private const val ProductDetailsListKeySimilar = "ProductDetailsListKeySimilar"

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
    private const val ProductDetailsListContentTypeSimilar =
        "ProductDetailsListContentTypeSimilar"

    private const val ProductTotalLookContentKeySuccess = "ProductTotalLookContentKeySuccess"

    private val SuggestedProductsSpacedBy: Dp get() = 12.dp
    private const val SuggestedProductsSkeletonCount = 4
    private val SuggestedProductCardWidth: Dp get() = 176.dp

    private const val Colon = ':'
    private const val Space = ' '
}
