package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class PickupStoreSelectorState {

    @Immutable
    data class Success(
        val stores: ImmutableList<PickupStore>,
        val cartItemCount: Int,
    ) : PickupStoreSelectorState()

    data object Loading : PickupStoreSelectorState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : PickupStoreSelectorState()
}
