package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.kotlinutil.findSubstringBounds
import ru.livetyping.zarina.core.platform.dialPhoneNumber
import ru.livetyping.zarina.core.uicommon.nameResId
import ru.livetyping.zarina.core.uicompose.rememberFormattedPhoneNumber
import ru.livetyping.zarina.core.uicompose.text.addStyle
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.order.OrderCard
import ru.livetyping.zarina.core.uikit.order.color
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.model.ButtonType
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.model.DescriptionType
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.model.OrderConfirmedState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun OrderConfirmedContent(
    state: OrderConfirmedState,
    onReturnToHomeClicked: () -> Unit,
    onPayClicked: () -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Title()
        Spacer(modifier = Modifier.height(16.dp))

        val order = state.order
        Description(
            order = order,
            descriptionType = state.descriptionType,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))

        val orderProductImageUrls = remember(order.products) {
            order.products.map { it.imageUrl.value }
        }
        OrderCard(
            orderNumber = state.order.number.value,
            orderStatusName = stringResource(order.status.nameResId),
            orderStatusColor = order.status.color,
            orderTotalPrice = order.totalPrice,
            orderDate = order.date,
            orderProductCount = order.productCount,
            orderProductImageUrls = orderProductImageUrls,
            onClick = null,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(16.dp))

        val onClick = when (state.buttonType) {
            ButtonType.RETURN_TO_HOME -> onReturnToHomeClicked
            ButtonType.PAY -> onPayClicked
        }
        ZarinaButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            val textResId = when (state.buttonType) {
                ButtonType.RETURN_TO_HOME -> R.string.cart_to_home_screen
                ButtonType.PAY -> RCommon.string.res_pay
            }
            Text(text = stringResource(textResId).uppercase())
        }

        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        Spacer(modifier = Modifier.windowInsetsBottomHeight(windowInsetsProvider()))
    }
}

@Composable
private fun Title(modifier: Modifier = Modifier) {
    ZarinaItem(modifier = modifier) {
        Text(
            text = stringResource(R.string.cart_thanks_for_order).uppercase(),
            style = UiKitTheme2.typography.h4,
        )
    }
}

@Composable
private fun Description(
    order: OrderDetailed,
    descriptionType: DescriptionType,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        val descriptionRawTextResId = when (descriptionType) {
            DescriptionType.ORDER_PAID -> R.string.cart_order_confirmed_description_paid
            DescriptionType.ORDER_SHOULD_BE_PAID -> {
                R.string.cart_order_confirmed_description_should_be_paid
            }

            DescriptionType.ORDER_SHOULD_BE_PAID_UPON_RECEIPT -> {
                R.string.cart_order_confirmed_description_should_be_paid_upon_receipt
            }
        }
        val boldTextStyle = UiKitTheme2.typography.bodyBold
        val orderNumber = remember(order.number) {
            "№${Typography.nbsp}${order.number.value}"
        }
        val recipientEmail = order.recipient.email.value
        val supportPhone = rememberFormattedPhoneNumber(PhoneNumber.ZARINA_SUPPORT.value)
        val descriptionRawText = stringResource(
            descriptionRawTextResId,
            orderNumber,
            recipientEmail,
            supportPhone.orEmpty(),
        )
        val descriptionText = remember(
            descriptionRawText,
            orderNumber,
            recipientEmail,
            boldTextStyle,
        ) {
            buildAnnotatedString {
                val boldSpanStyle = boldTextStyle.toSpanStyle()
                append(descriptionRawText)
                addStyle(orderNumber, boldSpanStyle)
                addStyle(recipientEmail, boldSpanStyle)

                if (supportPhone != null) {
                    val supportPhoneBounds = this.toAnnotatedString().findSubstringBounds(supportPhone)
                    if (supportPhoneBounds != null) {
                        addStyle(boldSpanStyle, supportPhoneBounds.first, supportPhoneBounds.last)

                        val link = LinkAnnotation.Clickable(supportPhone) {
                            if (it is LinkAnnotation.Clickable) {
                                val phoneNumber = PhoneNumber.create(it.tag)
                                context.dialPhoneNumber(phoneNumber.value)
                            }
                        }
                        addLink(link, supportPhoneBounds.first, supportPhoneBounds.last)
                    }
                }
            }
        }

        Text(
            text = descriptionText.toUpperCase(),
            style = UiKitTheme2.typography.body,
        )
    }
}
