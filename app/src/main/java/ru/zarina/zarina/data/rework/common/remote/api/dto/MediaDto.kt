package ru.zarina.zarina.data.rework.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaDto(
    @SerialName("media_url")
    val url: String? = null,

    @SerialName("type")
    val type: MediaTypeDto? = null,
)
