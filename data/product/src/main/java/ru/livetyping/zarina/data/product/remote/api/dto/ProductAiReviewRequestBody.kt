package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductAiReviewRequestBody(
    @SerialName("withDescription")
    var withDescription: Boolean,

    @SerialName("withLongDescription")
    var withLongDescription: Boolean,

    @SerialName("withTableTagsData")
    var withTableTagsData: Boolean,

    @SerialName("externalIds")
    val externalIds: List<String>
) {
    init {
        withDescription = true
        withLongDescription = true
        withTableTagsData = true
    }
}
