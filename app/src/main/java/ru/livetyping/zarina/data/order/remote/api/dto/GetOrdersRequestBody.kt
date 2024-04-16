package ru.livetyping.zarina.data.order.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetOrdersRequestBody(
    @SerialName("page")
    val page: Int,
)
