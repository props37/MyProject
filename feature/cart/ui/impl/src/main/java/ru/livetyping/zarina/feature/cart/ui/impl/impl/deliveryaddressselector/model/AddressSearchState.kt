package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class AddressSearchState {
    @Immutable
    data class Success(val items: ImmutableList<AddressSearchItem>) : AddressSearchState()

    data object Empty : AddressSearchState()

    data object Loading : AddressSearchState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : AddressSearchState()
}
