package ru.zarina.zarina.data.old.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableForStorePickupDto(
    @SerialName("is_applied")
    val isApplied: Boolean? = null,
    @SerialName("stores")
    val stores: List<FilterStoreDto?>? = null,
)
