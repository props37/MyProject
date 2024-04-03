package ru.livetyping.zarina.data.old.search.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttributesDto(
    @SerialName("скидка")
    val discount: List<String>?,
)
