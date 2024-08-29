package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.checkout.DeliveryOptions
import ru.livetyping.zarina.presentation.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.screen.checkout.common.CheckoutComponents
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.CheckoutCourierDeliveryViewModel.DeliveryOptionsState
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade

@Suppress("ConstPropertyName")
object CheckoutCourierDeliveryScreenComponents {

    @Suppress("NAME_SHADOWING")
    @Composable
    fun DeliveryOptions(
        state: DeliveryOptionsState?,
        onDeliveryOptionClicked: (DeliveryOptions.Option) -> Unit,
        onDeliveryOptionDateClicked: (DeliveryOptions.Option) -> Unit,
        onDeliveryOptionTimeClicked: (DeliveryOptions.Option) -> Unit,
        onDeliveryOptionShowDetailsClicked: (DeliveryOptions.Option) -> Unit,
        onDeliveryOptionsErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = state,
            contentKey = {
                when (it) {
                    is DeliveryOptionsState.Success -> DeliveryOptionsContentKeySuccess
                    DeliveryOptionsState.Loading -> it
                    is DeliveryOptionsState.Error -> it
                    null -> it
                }
            },
            modifier = modifier,
        ) { state ->
            if (state != null) {
                Column {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(R.string.choose_delivery_type),
                        style = UiKitTheme.typography.secondary.bold,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    when (state) {
                        is DeliveryOptionsState.Success -> {
                            DeliveryOptionsImpl(
                                state = state,
                                onDeliveryOptionClicked = onDeliveryOptionClicked,
                                onDeliveryOptionDateClicked = onDeliveryOptionDateClicked,
                                onDeliveryOptionTimeClicked = onDeliveryOptionTimeClicked,
                                onDeliveryOptionShowDetailsClicked = onDeliveryOptionShowDetailsClicked,
                            )
                        }

                        DeliveryOptionsState.Loading -> {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp)
                            ) {
                                ZarinaCircularLoader(modifier = Modifier.size(40.dp))
                            }
                        }

                        is DeliveryOptionsState.Error -> {
                            ZarinaErrorScreen(
                                state = state.state,
                                onButtonClicked = onDeliveryOptionsErrorRefreshClicked,
                                modifier = Modifier.padding(vertical = 32.dp),
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun DeliveryOptionsImpl(
        state: DeliveryOptionsState.Success,
        onDeliveryOptionClicked: (DeliveryOptions.Option) -> Unit,
        onDeliveryOptionDateClicked: (DeliveryOptions.Option) -> Unit,
        onDeliveryOptionTimeClicked: (DeliveryOptions.Option) -> Unit,
        onDeliveryOptionShowDetailsClicked: (DeliveryOptions.Option) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier,
        ) {
            state.options.forEach { optionState ->
                key(optionState.deliveryOption.id.value) {
                    val option = optionState.deliveryOption
                    CheckoutComponents.DeliveryOption(
                        title = option.title,
                        price = option.price,
                        deliveryDate = optionState.selectedDateTimePeriod.date,
                        deliveryTime = optionState.selectedDateTimePeriod.time,
                        isSelected = optionState.isSelected,
                        onClick = { onDeliveryOptionClicked(option) },
                        onDeliveryDateClicked = { onDeliveryOptionDateClicked(option) },
                        onDeliveryTimeClicked = { onDeliveryOptionTimeClicked(option) },
                        onShowDetailsClicked = { onDeliveryOptionShowDetailsClicked(option) },
                    )
                }
            }
        }
    }

    private const val DeliveryOptionsContentKeySuccess = "DeliveryOptionsContentKeySuccess"
}
