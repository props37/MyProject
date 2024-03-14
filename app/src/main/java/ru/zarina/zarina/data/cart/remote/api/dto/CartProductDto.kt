package ru.zarina.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartProductDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("offer")
    val offer: Offer? = null,
) {
    @Serializable
    data class Offer(
        @SerialName("id")
        val id: String? = null,

        @SerialName("barcode")
        val barcode: String? = null,

        @SerialName("title")
        val title: String? = null,

        @SerialName("color")
        val color: Color? = null,

        @SerialName("cover_picture")
        val imageUrl: String? = null,

        @SerialName("size_name")
        val size: String? = null,

        @SerialName("growth")
        val growth: String? = null,

        @SerialName("quantity")
        val count: Int? = null,

        @SerialName("is_favorite")
        val isInFavorites: Boolean? = null,
    ) {
        @Serializable
        data class Color(
            @SerialName("id") 
            val id: String? = null,
            
            @SerialName("title") 
            val name: String? = null,
            
            @SerialName("code")
            val code: String? = null,

            @SerialName("productId")
            val productId: String? = null,
        )
    }
}
