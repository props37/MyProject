package ru.livetyping.zarina.feature.profile.ui.impl.impl.order.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.domain.model.order.OrderRecipient
import ru.livetyping.zarina.core.uicommon.nameResId
import ru.livetyping.zarina.core.uicompose.rememberFormattedPhoneNumber
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.label.ZarinaLabelSize
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.order.OrderPrice
import ru.livetyping.zarina.core.uikit.order.OrderStatusLabel
import ru.livetyping.zarina.core.uikit.product.ProductOrderCard
import ru.livetyping.zarina.core.uikit.product.ProductOrderCardCountStyle
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun OrderSuccess(
    order: OrderDetailed,
    onPayForOrderClicked: () -> Unit,
    onCancelOrderClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 24.dp),
        modifier = modifier,
    ) {
        item(
            key = OrderListKey.Status,
            contentType = OrderListContentType.Status,
        ) {
            ZarinaItem(
                modifier = Modifier
                    .heightIn(min = 40.dp)
                    .animateZarinaItem(this),
            ) {
                OrderStatusLabel(
                    status = order.status,
                    size = ZarinaLabelSize.Medium,
                )
            }
        }

        item(
            key = OrderListKey.Contents,
            contentType = OrderListContentType.Contents,
        ) {
            OrderProductContentsItem(
                productCount = order.productCount,
                modifier = Modifier.animateZarinaItem(this),
            )
        }

        itemsIndexed(
            items = order.products,
            key = { _, product -> product.id.value },
            contentType = { _, _ -> OrderListContentType.Product },
        ) { index, product ->
            Column(modifier = Modifier.animateZarinaItem(this)) {
                ProductOrderCard(
                    name = product.name,
                    imageUrl = product.imageUrl.value,
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
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }

        item(
            key = OrderListKey.Price,
            contentType = OrderListContentType.Price,
        ) {
            OrderPrice(
                orderPrice = order.price.orderPrice,
                deliveryPrice = order.price.deliveryPrice,
                totalPrice = order.price.totalPrice,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateZarinaItem(this),
            )
        }

        item(
            key = OrderListKey.Info,
            contentType = OrderListContentType.Info,
        ) {
            OrderInfo(
                deliveryMethodType = order.deliveryInfo.type,
                deliveryAddress = order.deliveryAddress,
                recipient = order.recipient,
                paymentMethodType = order.paymentMethodType,
                modifier = Modifier.animateZarinaItem(this),
            )
        }

        if (!order.isPaid && order.paymentUrl != null) {
            item(
                key = OrderListKey.PayButton,
                contentType = OrderListContentType.PayButton,
            ) {
                ZarinaButton(
                    onClick = onPayForOrderClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp)
                        .animateZarinaItem(this),
                ) {
                    Text(text = stringResource(RCommon.string.res_pay).uppercase())
                }
            }
        }

        if (order.isCancellable) {
            item(
                key = OrderListKey.CancelButton,
                contentType = OrderListContentType.CancelButton,
            ) {
                ZarinaButton(
                    onClick = onCancelOrderClicked,
                    colors = ZarinaButtonDefaults.backlessErrorColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp)
                        .animateZarinaItem(this),
                ) {
                    Text(text = stringResource(RCommon.string.res_cancel_order).uppercase())
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
                text = stringResource(RCommon.string.res_order_contents),
                style = UiKitTheme.typography.secondary.bold,
            )
        },
        endContent = {
            Text(
                text = pluralStringResource(
                    id = RCommon.plurals.res_product_count,
                    count = productCount,
                    productCount.toString(),
                ),
                style = UiKitTheme.typography.secondary.light,
            )
        },
        modifier = modifier.heightIn(min = 40.dp),
    )
}

@Composable
private fun OrderInfo(
    deliveryMethodType: DeliveryMethodType,
    deliveryAddress: String,
    recipient: OrderRecipient,
    paymentMethodType: PaymentMethodType,
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
                text = stringResource(RCommon.string.res_order_info),
                style = UiKitTheme.typography.secondary.bold,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OrderInfoItem(
                name = stringResource(RCommon.string.res_delivery_method),
                value = stringResource(deliveryMethodType.nameResId),
            )
            Spacer(modifier = Modifier.height(12.dp))

            OrderInfoItem(
                name = stringResource(RCommon.string.res_delivery_address),
                value = deliveryAddress,
            )
            Spacer(modifier = Modifier.height(12.dp))

            val formattedPhone =
                rememberFormattedPhoneNumber(recipient.phone?.value.orEmpty())
            OrderInfoItem(
                name = stringResource(RCommon.string.res_order_recipient),
                value = remember(recipient) {
                    buildString {
                        append("${recipient.firstName} ${recipient.lastName.orEmpty()}")
                        append("\n")
                        append(recipient.email.value)
                        val phone = formattedPhone ?: recipient.phone?.value
                        if (phone != null) {
                            append("\n")
                            append(phone)
                        }
                    }
                },
            )
            Spacer(modifier = Modifier.height(12.dp))

            OrderInfoItem(
                name = stringResource(RCommon.string.res_payment),
                value = stringResource(paymentMethodType.nameResId),
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
            color = UiKitTheme.colors.text.general.regular.muted,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = OrderInfoValueTextStyle,
        )
    }
}

private enum class OrderListKey {
    Status,
    Contents,
    Price,
    Info,
    PayButton,
    CancelButton,
}

private enum class OrderListContentType {
    Status,
    Contents,
    Product,
    Price,
    Info,
    PayButton,
    CancelButton,
}

internal val OrderInfoContentPadding: PaddingValues
    get() = PaddingValues(16.dp)

internal val OrderInfoNameTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.tertiary.light

internal val OrderInfoValueTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.light
