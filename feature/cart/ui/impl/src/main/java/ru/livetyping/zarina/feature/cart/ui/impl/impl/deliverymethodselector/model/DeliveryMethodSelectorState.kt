package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class DeliveryMethodSelectorState {
    @Immutable
    data class Success(
        val deliveryMethods: ImmutableList<DeliveryMethod>,
    ) : DeliveryMethodSelectorState()

    data object Loading : DeliveryMethodSelectorState()

    @Immutable
    data class Error(val errorState: ZarinaErrorScreenState) : DeliveryMethodSelectorState()
}
