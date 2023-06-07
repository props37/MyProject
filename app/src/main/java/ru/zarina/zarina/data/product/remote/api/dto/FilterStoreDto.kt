package ru.zarina.zarina.data.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilterStoreDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("xml_id")
    val xmlId: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("city")
    val city: String? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("schedule")
    val schedule: String? = null,
)
