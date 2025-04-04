package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

@Stable
internal sealed class AddressSearchBottomSheetState {
    @Stable
    data class Visible(
        val type: AddressSearchType,
        val searchTextFieldState: TextFieldState,
        val searchState: AddressSearchState,
    ) : AddressSearchBottomSheetState()

    data object Hidden : AddressSearchBottomSheetState()
}
