package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.geo.AddressPart

@Immutable
internal data class AddressSearchItem(val address: AddressPart)
