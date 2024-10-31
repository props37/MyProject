package ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.presentation.common.component.OrderCard
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.common.util.domain.color
import ru.livetyping.zarina.presentation.common.util.domain.nameResId
import ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed.CheckoutOrderConfirmedScreenComponents.Description
import ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed.CheckoutOrderConfirmedScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed.CheckoutOrderConfirmedViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun CheckoutOrderConfirmedScreen(
    navigate: (CheckoutOrderConfirmedScreenAction) -> Unit,
    viewModel: CheckoutOrderConfirmedViewModel = hiltViewModel(),
) {
    val order by viewModel.order.collectAsStateWithLifecycle()

    ScreenContent(
        order = order,
        onPayForOrderClicked = viewModel::onPayForOrderClicked,
        onReturnToHomeScreenClicked = viewModel::onReturnToHomeScreenClicked,
        onPhoneClicked = viewModel::onPhoneNumberClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    order: OrderDetails,
    onPayForOrderClicked: () -> Unit,
    onReturnToHomeScreenClicked: () -> Unit,
    onPhoneClicked: (PhoneNumber) -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutOrderConfirmedScreenAction) -> Unit,
) {
    CheckoutOrderConfirmedScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.systemBars
                    .union(WindowInsets.displayCutout),
            ),
    ) {
        TopBar(onCloseClicked = onReturnToHomeScreenClicked)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            ZarinaItem {
                Text(
                    text = stringResource(R.string.thanks_for_order),
                    style = UiKitTheme.typography.primary.bold,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Description(
                order = order,
                onPhoneClicked = onPhoneClicked,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            val orderProductImageUrls = remember(order.products) {
                order.products.map { it.imageUrl.value }
            }
            OrderCard(
                orderNumber = order.number.value,
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

            val onClick = if (order.isPaid) {
                onReturnToHomeScreenClicked
            } else {
                onPayForOrderClicked
            }
            ZarinaButton(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp),
            ) {
                val textResId = if (order.isPaid) R.string.to_home_screen else R.string.pay
                Text(text = stringResource(textResId).uppercase())
            }
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // Add preview
    }
}
