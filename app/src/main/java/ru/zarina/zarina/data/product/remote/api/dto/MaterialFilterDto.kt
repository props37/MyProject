package ru.zarina.zarina.data.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MaterialFilterDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("is_applied")
    val isApplied: Boolean? = null,
)
