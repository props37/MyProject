package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.model

import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointDetailed
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

internal class PickupPointStateBuilder {
    fun build(
        pickupPointResult: Result<PickupPointDetailed>,
        pickupPointLoadingState: FlowRequester.LoadingState,
        selectedDeliveryTypeId: PickupPointDetailed.DeliveryType.Id?,
    ): PickupPointState {
        return if (pickupPointLoadingState.isLoading()) {
            PickupPointState.Loading
        } else {
            pickupPointResult.fold(
                onSuccess = { pickupPoint ->
                    PickupPointState.Success(
                        pickupPoint = pickupPoint,
                        selectedDeliveryTypeId = selectedDeliveryTypeId
                            ?: pickupPoint.deliveryTypes.first().id,
                    )
                },
                onFailure = { t ->
                    val errorState = ZarinaErrorScreenState.from(t)
                    PickupPointState.Error(errorState)
                },
            )
        }
    }
}
