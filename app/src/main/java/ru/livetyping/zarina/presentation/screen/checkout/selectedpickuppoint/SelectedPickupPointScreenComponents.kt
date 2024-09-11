package ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.checkout.PickupPoint.PaymentMethod
import ru.livetyping.zarina.domain.checkout.PickupPointDetails
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaRadioButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint.SelectedPickupPointViewModel.PickupPointState
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade
import ru.livetyping.zarina.util.kotlin.capitalize

@Suppress("ConstPropertyName")
object SelectedPickupPointScreenComponents {

    @Composable
    fun TopBar(
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
                Text(text = stringResource(R.string.point_information))
            },
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    fun PickupPoint(
        state: PickupPointState,
        onDeliveryTypeClicked: (PickupPointDetails.DeliveryType) -> Unit,
        onErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        @Suppress("NAME_SHADOWING")
        Crossfade(
            targetState = state,
            contentKey = {
                when (it) {
                    is PickupPointState.Success -> PickupPointContentKeySuccess
                    is PickupPointState.Error -> it
                    PickupPointState.Loading -> it
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is PickupPointState.Success -> {
                    PickupPointImpl(
                        state = state,
                        onDeliveryTypeClicked = onDeliveryTypeClicked,
                    )
                }

                PickupPointState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ZarinaCircularLoader(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.Center)
                                .navigationBarsPadding(),
                        )
                    }
                }

                is PickupPointState.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .navigationBarsPadding(),
                    )
                }
            }
        }
    }

    @Composable
    private fun PickupPointImpl(
        state: PickupPointState.Success,
        onDeliveryTypeClicked: (PickupPointDetails.DeliveryType) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                PickupPointInfo(pickupPoint = state.pickupPoint)

                Spacer(modifier = Modifier.height(40.dp))
                PickupPointDeliveryTerms(
                    pickupPoint = state.pickupPoint,
                    selectedDeliveryTypeId = state.selectedDeliveryTypeId,
                    onDeliveryTypeClicked = onDeliveryTypeClicked,
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            Column {
                ZarinaDivider(modifier = Modifier.fillMaxWidth())
                ZarinaButton(
                    onClick = {}, // TODO: [High] Implement
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                ) {
                    Text(text = stringResource(R.string.select).uppercase())
                }
            }
        }
    }

    @Composable
    private fun PickupPointInfo(
        pickupPoint: PickupPointDetails,
        modifier: Modifier = Modifier,
    ) {
        val itemModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

        Column(modifier = modifier) {
            Text(
                text = pickupPoint.title,
                style = UiKitTheme.typography.secondary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = itemModifier,
            )

            Spacer(modifier = Modifier.height(16.dp))
            PickupPointInfoItem(
                title = stringResource(R.string.working_hours),
                info = pickupPoint.schedule.capitalize(),
                modifier = itemModifier,
            )

            Spacer(modifier = Modifier.height(12.dp))
            PickupPointInfoItem(
                title = stringResource(R.string.address),
                info = pickupPoint.address,
                modifier = itemModifier,
            )

            if (pickupPoint.availablePaymentMethods.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                PickupPointPaymentMethodsItem(
                    paymentMethods = pickupPoint.availablePaymentMethods,
                    modifier = itemModifier,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            PickupPointInfoItem(
                title = stringResource(R.string.storage_time),
                info = pluralStringResource(
                    id = R.plurals.d_days,
                    pickupPoint.storageTime,
                    pickupPoint.storageTime,
                ),
                modifier = itemModifier,
            )

            Spacer(modifier = Modifier.height(12.dp))
            PickupPointInfoItem(
                title = stringResource(R.string.tentative_delivery_date),
                info = pickupPoint.expectedDeliveryDate,
                modifier = itemModifier,
            )
        }
    }

    @Composable
    private fun PickupPointDeliveryTerms(
        pickupPoint: PickupPointDetails,
        selectedDeliveryTypeId: PickupPointDetails.DeliveryType.Id,
        onDeliveryTypeClicked: (PickupPointDetails.DeliveryType) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            Text(
                text = stringResource(R.string.choose_delivery_terms),
                style = UiKitTheme.typography.tertiary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            pickupPoint.deliveryTypes.forEachIndexed { index, deliveryType ->
                key(deliveryType.id.value) {
                    ZarinaItem(
                        onClick = { onDeliveryTypeClicked(deliveryType) },
                        startContent = {
                            Column {
                                Text(
                                    text = deliveryType.title,
                                    style = UiKitTheme.typography.secondary.light,
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = remember(deliveryType.description) {
                                        AnnotatedString.fromHtml(deliveryType.description)
                                    },
                                    style = UiKitTheme.typography.footnote.light,
                                    color = UiKitTheme.colors.text.general.regular.muted,
                                )
                            }
                        },
                        endContent = {
                            ZarinaRadioButton(
                                isSelected = deliveryType.id == selectedDeliveryTypeId,
                                onClick = { onDeliveryTypeClicked(deliveryType) },
                            )
                        },
                        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, bottom = 12.dp),
                    )

                    if (index < pickupPoint.deliveryTypes.lastIndex) {
                        ZarinaDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun PickupPointPaymentMethodsItem(
        paymentMethods: Set<PaymentMethod>,
        modifier: Modifier = Modifier,
    ) {
        val paymentMethodStrings = paymentMethods.map {
            when (it) {
                PaymentMethod.CASH -> stringResource(R.string.by_cash)
                PaymentMethod.CARD -> stringResource(R.string.by_card)
            }
        }
        val paymentMethodsText = when (paymentMethods.size) {
            0 -> null
            1 -> paymentMethodStrings.first()
            else -> stringResource(
                R.string.s_or_s,
                paymentMethodStrings[0],
                paymentMethodStrings[1],
            )
        }

        if (paymentMethodsText != null) {
            PickupPointInfoItem(
                title = stringResource(R.string.payment),
                info = paymentMethodsText.lowercase().capitalize(),
                modifier = modifier,
            )
        }
    }

    @Composable
    private fun PickupPointInfoItem(
        title: String,
        info: String,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            Text(
                text = title,
                style = UiKitTheme.typography.footnote.regular,
                color = UiKitTheme.colors.text.general.regular.muted,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = info,
                style = UiKitTheme.typography.secondary.light,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    private const val PickupPointContentKeySuccess = "PickupPointContentKeySuccess"
}
