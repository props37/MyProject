package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductAiReviewRequestBody(
    @SerialName("externalIds")
    val externalIds: List<String>,

    @SerialName("withDescription")
    val withDescription: Boolean = true,

    @SerialName("withLongDescription")
    val withLongDescription: Boolean = true,

    @SerialName("withTableTagsData")
    val withTableTagsData: Boolean = true
)