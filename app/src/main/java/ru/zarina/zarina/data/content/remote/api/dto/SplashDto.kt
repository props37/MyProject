package ru.zarina.zarina.data.content.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Url

@Serializable
data class SplashDto(
    @SerialName("image")
    val url: String?,
) {
    fun toDomain() = url?.let { Url(it) }
}
