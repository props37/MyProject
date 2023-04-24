package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.utils.kotlin.isNotNull

@Serializable
data class DescriptionItemDto(
    @SerialName("title")
    val title: String?,
    @SerialName("text")
    val text: String?,
) {

    fun toDomain(): Pair<String, String>? {
        return if (isNotNull(title, "title") && isNotNull(text, "text"))
            title to text
        else
            null
    }

}
