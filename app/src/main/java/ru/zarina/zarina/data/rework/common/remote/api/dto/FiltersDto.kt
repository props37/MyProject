package ru.zarina.zarina.data.rework.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FiltersDto(
    @SerialName("price")
    val price: Price? = null,

    @SerialName("materials")
    val materials: List<BasicItem>? = null,

    @SerialName("sizes")
    val sizes: List<BasicItem>? = null,

    @SerialName("colors")
    val colors: List<Color>? = null,
) {
    @Serializable
    data class Price(
        @SerialName("min")
        val min: Long? = null,

        @SerialName("max")
        val max: Long? = null,
    )

    @Serializable
    data class BasicItem(
        @SerialName("id")
        val id: String? = null,

        @SerialName("name")
        val name: String? = null,
    )

    @Serializable
    data class Color(
        @SerialName("id")
        val id: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("code")
        val code: String? = null,
    )
}
