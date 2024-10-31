package ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.presentation.common.component.button.ZarinaCloseIconButton
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.util.rememberFormattedPhoneNumber
import ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed.CheckoutOrderConfirmedViewModel.DescriptionType
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.text.addStyle
import ru.livetyping.zarina.util.kotlin.findSubstringBounds

object CheckoutOrderConfirmedScreenComponents {

    @Composable
    fun TopBar(
        onCloseClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            endContent = {
                ZarinaCloseIconButton(
                    onClick = onCloseClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(end = 2.dp),
                )
            },
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    fun Description(
        order: OrderDetails,
        descriptionType: DescriptionType,
        onPhoneClicked: (PhoneNumber) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val descriptionRawTextResId = when (descriptionType) {
                DescriptionType.ORDER_PAID -> R.string.order_confirmed_description_paid
                DescriptionType.ORDER_SHOULD_BE_PAID -> {
                    R.string.order_confirmed_description_should_be_paid
                }

                DescriptionType.ORDER_SHOULD_BE_PAID_UPON_RECEIPT -> {
                    R.string.order_confirmed_description_should_be_paid_upon_receipt
                }
            }
            val boldTextStyle = UiKitTheme.typography.secondary.bold
            val orderNumber = remember(order.number) {
                "№${Typography.nbsp}${order.number.value}"
            }
            val customerEmail = order.contactInfo.email.value
            val supportPhone = rememberFormattedPhoneNumber(PhoneNumber.ZARINA_SUPPORT.value)
            val descriptionRawText = stringResource(
                descriptionRawTextResId,
                orderNumber,
                customerEmail,
                supportPhone.orEmpty(),
            )
            val descriptionText = remember(
                descriptionRawText,
                orderNumber,
                customerEmail,
                boldTextStyle,
            ) {
                buildAnnotatedString {
                    val boldSpanStyle = boldTextStyle.toSpanStyle()
                    append(descriptionRawText)
                    addStyle(orderNumber, boldSpanStyle)
                    addStyle(customerEmail, boldSpanStyle)

                    if (supportPhone != null) {
                        val supportPhoneBounds = this.toAnnotatedString().findSubstringBounds(supportPhone)
                        if (supportPhoneBounds != null) {
                            addStyle(boldSpanStyle, supportPhoneBounds.first, supportPhoneBounds.last)

                            val link = LinkAnnotation.Clickable(supportPhone) {
                                if (it is LinkAnnotation.Clickable) {
                                    val phoneNumber = PhoneNumber.create(it.tag)
                                    onPhoneClicked(phoneNumber)
                                }
                            }
                            addLink(link, supportPhoneBounds.first, supportPhoneBounds.last)
                        }
                    }
                }
            }

            Text(
                text = descriptionText,
                style = UiKitTheme.typography.secondary.regular,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}
