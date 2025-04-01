package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointDetailed
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class SelectedPickupPointState {
    @Immutable
    class Success(
        val pickupPoint: PickupPointDetailed,
        val selectedDeliveryTypeId: PickupPointDetailed.DeliveryType.Id,
    ) : SelectedPickupPointState()

    data object Loading : SelectedPickupPointState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : SelectedPickupPointState()
}
