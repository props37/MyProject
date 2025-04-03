package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable

@Immutable
internal data class DeliveryAddressSelectorState(
    val deliveryType: DeliveryType,
    val streetTextFieldState: TextFieldState,
    val buildingTextFieldState: TextFieldState,
    val apartmentTextFieldState: TextFieldState,
    val isBuildingSelectionEnabled: Boolean,
)
