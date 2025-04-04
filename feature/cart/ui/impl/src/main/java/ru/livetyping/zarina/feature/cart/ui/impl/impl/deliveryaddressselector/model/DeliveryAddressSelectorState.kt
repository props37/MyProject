package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.geo.City

@Immutable
internal data class DeliveryAddressSelectorState(
    val deliveryType: DeliveryType,
    val city: City?,
    val streetSelectorTextFieldState: TextFieldState,
    val buildingSelectorTextFieldState: TextFieldState,
    val apartmentSelectorTextFieldState: TextFieldState,
    val isBuildingSelectionEnabled: Boolean,
)
