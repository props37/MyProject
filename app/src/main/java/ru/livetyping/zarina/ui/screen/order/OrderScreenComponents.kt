package ru.livetyping.zarina.ui.screen.order

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.OrderContactInfo
import ru.livetyping.zarina.domain.order.OrderDeliveryMethod
import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.domain.order.OrderPaymentMethod
import ru.livetyping.zarina.ui.common.component.OrderPrice
import ru.livetyping.zarina.ui.common.component.OrderPriceSkeleton
import ru.livetyping.zarina.ui.common.component.OrderStatusLabel
import ru.livetyping.zarina.ui.common.component.ProductOrderCard
import ru.livetyping.zarina.ui.common.component.ProductOrderCardCountStyle
import ru.livetyping.zarina.ui.common.component.ProductOrderCardSkeleton
import ru.livetyping.zarina.ui.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.ui.common.component.item.ZarinaItem
import ru.livetyping.zarina.ui.common.component.label.ZarinaLabelSize
import ru.livetyping.zarina.ui.common.component.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.ui.common.component.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.ui.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.ui.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.ui.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.ui.common.util.domain.nameResId
import ru.livetyping.zarina.ui.common.util.rememberFormattedPhoneNumber
import ru.livetyping.zarina.ui.screen.order.OrderViewModel.OrderState
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentCrossfadeTransitionSpec
import ru.livetyping.zarina.util.compose.animation.Crossfade

object OrderScreenComponents {

    @Composable
    fun TopBar(
        orderNumber: Order.Number?,
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                AnimatedContent(
                    targetState = orderNumber,
                    transitionSpec = {
                        AnimatedContentCrossfadeTransitionSpec().using(sizeTransform = null)
                    },
                    contentAlignment = Alignment.Center,
                    label = "Order number",
                ) { number ->
                    if (number != null) {
                        Text(
                            text = stringResource(R.string.order_number, number.value),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    } else {
                        ZarinaTextSkeleton(
                            textStyle = LocalTextStyle.current,
                            modifier = Modifier.width(96.dp),
                        )
                    }
                }
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Order(
        orderState: OrderState,
        onCancelOrderClicked: () -> Unit,
        isRefreshing: Boolean,
        onPullRefreshTriggered: () -> Unit,
        onOrderErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            val pullRefreshState = rememberPullRefreshState(
                refreshing = isRefreshing,
                onRefresh = onPullRefreshTriggered,
            )

            ZarinaPullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(1f),
            )

            Crossfade(
                targetState = orderState,
                contentKey = {
                    when (it) {
                        is OrderState.Order -> OrderContentKeyOrder
                        is OrderState.Error, OrderState.Loading -> it
                    }
                },
                modifier = Modifier.fillMaxSize(),
            ) { state ->
                when (state) {
                    is OrderState.Order -> {
                        OrderImpl(
                            order = state.order,
                            onCancelOrderClicked = onCancelOrderClicked,
                            modifier = Modifier
                                .fillMaxSize()
                                .pullRefresh(pullRefreshState),
                        )
                    }

                    OrderState.Loading -> {
                        OrderSkeleton(modifier = Modifier.fillMaxSize())
                    }

                    is OrderState.Error -> {
                        ZarinaErrorScreen(
                            state = state.state,
                            onButtonClicked = onOrderErrorRefreshClicked,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun OrderImpl(
        order: OrderDetails,
        onCancelOrderClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(modifier = modifier) {
            item(
                key = OrderListKeyStatus,
                contentType = OrderListContentTypeStatus,
            ) {
                ZarinaItem(modifier = Modifier.heightIn(min = 40.dp)) {
                    OrderStatusLabel(
                        status = order.status,
                        size = ZarinaLabelSize.Medium,
                    )
                }
            }

            item(
                key = OrderListKeyContents,
                contentType = OrderListContentTypeContents,
            ) {
                OrderProductContentsItem(order.productCount)
            }

            itemsIndexed(
                items = order.products,
                key = { _, order -> order.id.value },
                contentType = { _, _ -> OrderListContentTypeProduct },
            ) { index, product ->
                ProductOrderCard(
                    name = product.name,
                    imageUrl = product.imageUrl,
                    size = product.size,
                    sizeRu = null,
                    height = null,
                    color = product.color,
                    count = product.count,
                    countStyle = ProductOrderCardCountStyle.Info,
                    price = product.price,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (index < order.products.lastIndex) {
                    Divider(
                        color = UiKitTheme.colors.background.skeleton,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }

            item(
                key = OrderListKeyPrice,
                contentType = OrderListContentTypePrice,
            ) {
                OrderPrice(
                    orderPrice = order.price.orderPrice,
                    deliveryPrice = order.price.deliveryPrice,
                    totalPrice = order.price.totalPrice,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item(
                key = OrderListKeyInfo,
                contentType = OrderListContentTypeInfo,
            ) {
                OrderInfo(
                    deliveryMethod = order.deliveryInfo.method,
                    deliveryAddress = order.deliveryAddress,
                    contactInfo = order.contactInfo,
                    paymentMethod = order.paymentMethod,
                )
            }

            if (order.isCancellable) {
                item(
                    key = OrderListKeyCancelButton,
                    contentType = OrderListContentTypeCancelButton,
                ) {
                    ZarinaButton(
                        onClick = onCancelOrderClicked,
                        colors = ZarinaButtonDefaults.backlessErrorColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Text(text = stringResource(R.string.cancel_order).uppercase())
                    }
                }
            }
        }
    }

    @Composable
    private fun OrderProductContentsItem(
        productCount: Int,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            startContent = {
                Text(
                    text = stringResource(R.string.order_contents),
                    style = UiKitTheme.typography.secondary.bold,
                )
            },
            endContent = {
                Text(
                    text = pluralStringResource(R.plurals.products, productCount, productCount),
                    style = UiKitTheme.typography.secondary.light,
                )
            },
            modifier = modifier.heightIn(min = 40.dp),
        )
    }

    @Composable
    private fun OrderInfo(
        deliveryMethod: OrderDeliveryMethod,
        deliveryAddress: String,
        contactInfo: OrderContactInfo,
        paymentMethod: OrderPaymentMethod,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = OrderInfoContentPadding,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides UiKitTheme.colors.text.general.regular.default,
        ) {
            Column(
                modifier = modifier.padding(contentPadding),
            ) {
                Text(
                    text = stringResource(R.string.info_about_order),
                    style = UiKitTheme.typography.secondary.bold,
                )

                Spacer(modifier = Modifier.height(16.dp))

                OrderInfoItem(
                    name = stringResource(R.string.delivery_method),
                    value = stringResource(deliveryMethod.nameResId),
                )
                Spacer(modifier = Modifier.height(12.dp))

                OrderInfoItem(
                    name = stringResource(R.string.delivery_address),
                    value = deliveryAddress,
                )
                Spacer(modifier = Modifier.height(12.dp))

                val formattedPhone =
                    rememberFormattedPhoneNumber(contactInfo.phone?.value.orEmpty())
                OrderInfoItem(
                    name = stringResource(R.string.recipient),
                    value = remember(contactInfo) {
                        buildString {
                            append("${contactInfo.firstName} ${contactInfo.lastName.orEmpty()}")
                            append("\n")
                            append(contactInfo.email.value)
                            val phone = formattedPhone ?: contactInfo.phone?.value
                            if (phone != null) {
                                append("\n")
                                append(phone)
                            }
                        }
                    },
                )
                Spacer(modifier = Modifier.height(12.dp))

                OrderInfoItem(
                    name = stringResource(R.string.payment),
                    value = stringResource(paymentMethod.nameResId),
                )
            }
        }
    }

    @Composable
    private fun OrderInfoItem(
        name: String,
        value: String,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            Text(
                text = name,
                style = OrderInfoNameTextStyle,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = OrderInfoValueTextStyle,
            )
        }
    }

    @Composable
    private fun OrderSkeleton(
        modifier: Modifier = Modifier,
    ) {
        val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)
        LazyColumn(modifier = modifier) {
            item {
                ZarinaItem(modifier = Modifier.heightIn(min = 40.dp)) {
                    ZarinaTextSkeleton(
                        textStyle = UiKitTheme.typography.footnote.bold,
                        shimmer = shimmer,
                        modifier = Modifier.fillMaxWidth(fraction = 0.08f),
                    )
                }
            }

            item {
                ZarinaItem(
                    startContent = {
                        ZarinaTextSkeleton(
                            textStyle = UiKitTheme.typography.secondary.bold,
                            shimmer = shimmer,
                            modifier = Modifier.fillMaxWidth(fraction = 0.2f),
                        )
                    },
                    endContent = {
                        ZarinaTextSkeleton(
                            textStyle = UiKitTheme.typography.secondary.light,
                            shimmer = shimmer,
                            modifier = Modifier.fillMaxWidth(fraction = 0.16f),
                        )
                    },
                    modifier = Modifier.heightIn(min = 40.dp),
                )
            }

            items(OrderSkeletonProductCount) { index ->
                ProductOrderCardSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (index < OrderSkeletonProductCount - 1) {
                    Divider(
                        color = UiKitTheme.colors.background.skeleton,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }

            item {
                OrderPriceSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                OrderInfoSkeleton(shimmer = shimmer)
            }

            item {
                ZarinaSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                )
            }
        }
    }

    @Composable
    private fun OrderInfoSkeleton(
        shimmer: Shimmer,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = OrderInfoContentPadding,
    ) {
        Column(modifier = modifier.padding(contentPadding)) {
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.secondary.bold,
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(fraction = 0.2f),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OrderInfoItemSkeleton(shimmer = shimmer)
            Spacer(modifier = Modifier.height(12.dp))
            OrderInfoItemSkeleton(shimmer = shimmer)
            Spacer(modifier = Modifier.height(12.dp))
            OrderInfoItemSkeleton(shimmer = shimmer)
            Spacer(modifier = Modifier.height(12.dp))
            OrderInfoItemSkeleton(shimmer = shimmer)
        }
    }

    @Composable
    private fun OrderInfoItemSkeleton(
        shimmer: Shimmer,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaTextSkeleton(
                textStyle = OrderInfoNameTextStyle,
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(fraction = 0.25f),
            )
            Spacer(modifier = Modifier.height(4.dp))
            ZarinaTextSkeleton(
                textStyle = OrderInfoValueTextStyle,
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(fraction = 0.42f),
            )
        }
    }

    private const val OrderContentKeyOrder = "OrderContentKeyOrder"

    private const val OrderListKeyStatus = "OrderListKeyStatus"
    private const val OrderListKeyContents = "OrderListKeyContents"
    private const val OrderListKeyPrice = "OrderListKeyPrice"
    private const val OrderListKeyInfo = "OrderListKeyInfo"
    private const val OrderListKeyCancelButton = "OrderListKeyCancelButton"

    private const val OrderListContentTypeStatus = "OrderListContentTypeStatus"
    private const val OrderListContentTypeContents = "OrderListContentTypeContents"
    private const val OrderListContentTypeProduct = "OrderListContentTypeProduct"
    private const val OrderListContentTypePrice = "OrderListContentTypePrice"
    private const val OrderListContentTypeInfo = "OrderListContentTypeInfo"
    private const val OrderListContentTypeCancelButton = "OrderListContentTypeCancelButton"

    private val OrderInfoContentPadding: PaddingValues get() = PaddingValues(16.dp)

    private val OrderInfoNameTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.tertiary.light

    private val OrderInfoValueTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.light

    private const val OrderSkeletonProductCount = 5
}
