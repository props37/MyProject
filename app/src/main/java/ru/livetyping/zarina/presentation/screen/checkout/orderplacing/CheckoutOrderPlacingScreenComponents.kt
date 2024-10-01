package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.checkout.Customer
import ru.livetyping.zarina.presentation.common.animation.LazyListFadeInSpec
import ru.livetyping.zarina.presentation.common.animation.LazyListPlacementSpec
import ru.livetyping.zarina.presentation.common.component.ProductOrderCard
import ru.livetyping.zarina.presentation.common.component.ProductOrderCardCountStyle
import ru.livetyping.zarina.presentation.common.component.ProductOrderCardSkeleton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.util.domain.nameResId
import ru.livetyping.zarina.presentation.common.util.rememberFormattedPhoneNumber
import ru.livetyping.zarina.presentation.screen.cart.model.CartState
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingViewModel.DeliveryInfo
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Suppress("ConstPropertyName")
object CheckoutOrderPlacingScreenComponents {

    // TODO: [High] Add bottom bar padding
    @Composable
    fun OrderPlacing(
        customer: Customer,
        onChangeCustomerClicked: () -> Unit,
        deliveryInfo: DeliveryInfo,
        onChangeDeliveryClicked: () -> Unit,
        cartState: CartState,
        modifier: Modifier = Modifier,
    ) {
        val navigationBarHeight =
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val contentPadding = PaddingValues(bottom = navigationBarHeight + 20.dp)

        LazyColumn(
            contentPadding = contentPadding,
            modifier = modifier,
        ) {
            item(
                key = OrderPlacingKey.Customer,
                contentType = OrderPlacingContentType.Customer,
            ) {
                Customer(
                    customer = customer,
                    onChangeClicked = onChangeCustomerClicked,
                    modifier = Modifier.animateItem(
                        fadeInSpec = LazyListFadeInSpec,
                        placementSpec = LazyListPlacementSpec,
                        fadeOutSpec = LazyListFadeInSpec,
                    ),
                )
            }

            item(
                key = OrderPlacingKey.DeliveryInfo,
                contentType = OrderPlacingContentType.DeliveryInfo,
            ) {
                DeliveryInfo(
                    deliveryInfo = deliveryInfo,
                    onChangeClicked = onChangeDeliveryClicked,
                    modifier = Modifier.animateItem(
                        fadeInSpec = LazyListFadeInSpec,
                        placementSpec = LazyListPlacementSpec,
                        fadeOutSpec = LazyListFadeInSpec,
                    ),
                )
            }

            item(
                key = OrderPlacingKey.YourOrder,
                contentType = OrderPlacingContentType.YourOrder,
            ) {
                YourOrder(
                    modifier = Modifier.animateItem(
                        fadeInSpec = LazyListFadeInSpec,
                        placementSpec = LazyListPlacementSpec,
                        fadeOutSpec = LazyListFadeInSpec,
                    ),
                )
            }

            cartItems(
                cartState = cartState,
            )
        }
    }

    @Composable
    private fun Customer(
        customer: Customer,
        onChangeClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaItem(
                startContent = {
                    Text(
                        text = stringResource(R.string.customer),
                        style = UiKitTheme.typography.secondary.bold,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )
                },
                endContent = {
                    OrderListHeaderChangeButton(onClick = onChangeClicked)
                },
                contentPadding = OrderPlacingListHeaderTitleContentPadding,
            )

            ZarinaItem(contentPadding = OrderPlacingListHeaderDescriptionContentPadding) {
                Column {
                    val fullName = remember(customer) { customer.getFullName() }
                    Text(
                        text = fullName,
                        style = UiKitTheme.typography.secondary.light,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    val formattedPhone = rememberFormattedPhoneNumber(customer.phone.value)
                    val contacts = remember(customer, formattedPhone) {
                        buildString {
                            append(customer.email.value)
                            if (formattedPhone != null) {
                                append(CommaSeparator)
                                append(formattedPhone)
                            }
                        }
                    }
                    Text(
                        text = contacts,
                        style = UiKitTheme.typography.tertiary.light,
                        color = UiKitTheme.colors.text.general.regular.muted,
                    )
                }
            }
        }
    }

    @Composable
    private fun DeliveryInfo(
        deliveryInfo: DeliveryInfo,
        onChangeClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaItem(
                startContent = {
                    Text(
                        text = stringResource(R.string.delivery_method),
                        style = UiKitTheme.typography.secondary.bold,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )
                },
                endContent = {
                    OrderListHeaderChangeButton(onClick = onChangeClicked)
                },
                contentPadding = OrderPlacingListHeaderTitleContentPadding,
            )

            ZarinaItem(contentPadding = OrderPlacingListHeaderDescriptionContentPadding) {
                Column {
                    Text(
                        text = stringResource(deliveryInfo.deliveryMethodType.nameResId),
                        style = UiKitTheme.typography.secondary.light,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    deliveryInfo.descriptions.forEachIndexed { index, description ->
                        Text(
                            text = description,
                            style = UiKitTheme.typography.tertiary.light,
                            color = UiKitTheme.colors.text.general.regular.muted,
                        )

                        if (index < deliveryInfo.descriptions.lastIndex) {
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun YourOrder(
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(modifier = modifier) {
            Text(
                text = stringResource(R.string.your_order),
                style = UiKitTheme.typography.secondary.bold,
            )
        }
    }

    private fun LazyListScope.cartItems(
        cartState: CartState,
    ) {
        when (cartState) {
            is CartState.Cart -> {
                cartItemsImpl(
                    cartState = cartState,
                )
            }

            CartState.Loading -> {
                item(
                    key = OrderPlacingKey.CartSkeleton,
                    contentType = OrderPlacingContentType.CartSkeleton,
                ) {
                    CartSkeleton(
                        modifier = Modifier.animateItem(
                            fadeInSpec = LazyListFadeInSpec,
                            placementSpec = LazyListPlacementSpec,
                            fadeOutSpec = LazyListFadeInSpec,
                        ),
                    )
                }
            }

            is CartState.Error -> {
                item(
                    key = OrderPlacingKey.CartError,
                    contentType = OrderPlacingContentType.CartError,
                ) {
                    CartError(
                        cartState = cartState,
                        modifier = Modifier.animateItem(
                            fadeInSpec = LazyListFadeInSpec,
                            placementSpec = LazyListPlacementSpec,
                            fadeOutSpec = LazyListFadeInSpec,
                        ),
                    )
                }
            }

            CartState.EmptyCart -> {
                item(
                    key = OrderPlacingKey.EmptyCartError,
                    contentType = OrderPlacingContentType.EmptyCartError,
                ) {
                    EmptyCartError(
                        modifier = Modifier.animateItem(
                            fadeInSpec = LazyListFadeInSpec,
                            placementSpec = LazyListPlacementSpec,
                            fadeOutSpec = LazyListFadeInSpec,
                        ),
                    )
                }
            }
        }
    }

    private fun LazyListScope.cartItemsImpl(
        cartState: CartState.Cart,
    ) {
        itemsIndexed(
            items = cartState.productItems,
            key = { _, productItem ->
                OrderPlacingKey.CartProduct(productItem.product.id.value)
            },
            contentType = { _, _ -> OrderPlacingContentType.CartProduct },
        ) { index, productItem ->
            Column(
                modifier = Modifier.animateItem(
                    fadeInSpec = LazyListFadeInSpec,
                    placementSpec = LazyListPlacementSpec,
                    fadeOutSpec = LazyListFadeInSpec,
                )
            ) {
                val product = productItem.product
                val countStyle = remember {
                    ProductOrderCardCountStyle.Selector(isEditable = false, onClick = {})
                }
                ProductOrderCard(
                    name = product.name,
                    imageUrl = product.imageUrl,
                    size = product.size,
                    sizeRu = null,
                    height = product.height,
                    color = product.color,
                    count = product.count,
                    countStyle = countStyle,
                    price = product.price,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (index < cartState.productItems.lastIndex) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun CartSkeleton(
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

            repeat(CartSkeletonItemCount) { index ->
                ProductOrderCardSkeleton(shimmer = shimmer)

                if (index < CartSkeletonItemCount - 1) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun CartError(
        cartState: CartState.Error,
        modifier: Modifier = Modifier,
    ) {
        ZarinaErrorScreen(
            state = cartState.state,
            onButtonClicked = {}, // TODO: [High] Implement
            modifier = modifier
                .fillMaxWidth()
                .padding(CartErrorPadding),
        )
    }

    @Composable
    private fun EmptyCartError(
        modifier: Modifier = Modifier,
    ) {
        val errorState = remember { ErrorState.GENERIC }
        ZarinaErrorScreen(
            state = errorState,
            onButtonClicked = {}, // TODO: [High] Implement
            modifier = modifier
                .fillMaxWidth()
                .padding(CartErrorPadding),
        )
    }

    @Composable
    private fun OrderListHeaderChangeButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaButton(
            onClick = onClick,
            size = ZarinaButtonSize.Medium,
            colors = ZarinaButtonDefaults.backlessColors(),
            modifier = modifier,
        ) {
            Text(text = stringResource(R.string.change).uppercase())
        }
    }

    @Parcelize
    @Stable
    private sealed class OrderPlacingKey : Parcelable {
        data object Customer : OrderPlacingKey()

        data object DeliveryInfo : OrderPlacingKey()

        data object YourOrder : OrderPlacingKey()

        data object CartError : OrderPlacingKey()

        data object EmptyCartError : OrderPlacingKey()

        data object CartSkeleton : OrderPlacingKey()

        data class CartProduct(val id: Long) : OrderPlacingKey()
    }

    @Stable
    private enum class OrderPlacingContentType {
        Customer,
        DeliveryInfo,
        YourOrder,
        CartError,
        EmptyCartError,
        CartSkeleton,
        CartProduct,
    }

    @Stable
    private val OrderPlacingListHeaderTitleContentPadding: PaddingValues
        get() = PaddingValues(start = 16.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)

    @Stable
    private val OrderPlacingListHeaderDescriptionContentPadding: PaddingValues
        get() = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 8.dp)

    private const val CommaSeparator = ", "

    private const val CartSkeletonItemCount = 4

    private val CartErrorPadding: PaddingValues
        get() = PaddingValues(horizontal = 16.dp, vertical = 56.dp)
}
