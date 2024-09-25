package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.checkout.Customer
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.util.rememberFormattedPhoneNumber
import ru.livetyping.zarina.presentation.theme.UiKitTheme

object CheckoutOrderPlacingScreenComponents {

    // TODO: [High] Add bottom bar padding
    @Composable
    fun OrderPlacing(
        customer: Customer,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(
            modifier = modifier,
        ) {
            item(
                key = OrderPlacingKey.Customer,
                contentType = OrderPlacingContentType.Customer,
            ) {
                Customer(customer = customer)
            }
        }
    }

    @Composable
    private fun Customer(
        customer: Customer,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaItem {
                Text(
                    text = stringResource(R.string.customer),
                    style = UiKitTheme.typography.secondary.bold,
                    color = UiKitTheme.colors.text.general.regular.default,
                )
            }

            ZarinaItem(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 0.dp,
                    end = 16.dp,
                    bottom = 8.dp,
                ),
            ) {
                Column {
                    val name = remember(customer) {
                        "${customer.firstName} ${customer.lastName}"
                    }
                    Text(
                        text = name,
                        style = UiKitTheme.typography.secondary.light,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Row {
                        val formattedPhone = rememberFormattedPhoneNumber(customer.phone.value)
                        val contacts = remember(customer, formattedPhone) {
                            buildString {
                                append(customer.email.value)
                                if (formattedPhone != null) {
                                    append(", ")
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
    }

    @Parcelize
    @Stable
    private sealed class OrderPlacingKey : Parcelable {
        data object Customer : OrderPlacingKey()
    }

    private sealed class OrderPlacingContentType {
        data object Customer : OrderPlacingContentType()
    }
}
