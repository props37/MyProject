package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class PickupPointListState {
    @Immutable
    data class Success(val pickupPoints: ImmutableList<PickupPoint>) : PickupPointListState()

    data object Loading : PickupPointListState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : PickupPointListState()
}
