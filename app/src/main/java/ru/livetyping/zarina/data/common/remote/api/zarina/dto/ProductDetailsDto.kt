package ru.livetyping.zarina.data.common.remote.api.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailsDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("price")
    val price: PriceDto? = null,

    @SerialName("offers")
    val offers: List<ProductOfferDto>? = null,

    @SerialName("colors")
    val colors: List<ProductColorDto>? = null,

    @SerialName("media")
    val media: List<MediaDto>? = null,

    // TODO: [High] Add description
    // TODO: [High] Add measurements?
    // TODO: [High] Add gender?

    @SerialName("share_url")
    val shareUrl: String? = null,
)
