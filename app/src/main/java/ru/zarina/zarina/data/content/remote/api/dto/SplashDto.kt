package ru.zarina.zarina.data.content.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.Url

@Serializable
data class SplashDto(
    @SerialName("image")
    val url: String? = null,
) {
    fun toDomain(): Url? {
        if (ApiContract.isNotNull(url)) return Url(url)
        return null
    }
}
