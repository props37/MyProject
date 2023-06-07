package ru.zarina.zarina.data.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ColorFilterDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("code")
    val code: String? = null,
    @SerialName("is_applied")
    val isApplied: Boolean? = null,
)
