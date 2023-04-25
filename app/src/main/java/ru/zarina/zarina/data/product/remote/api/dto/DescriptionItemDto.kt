package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract

@Serializable
data class DescriptionItemDto(
    @SerialName("title")
    val title: String? = null,
    @SerialName("text")
    val text: String? = null,
) {

    fun toDomain(): Pair<String, String>? {
        return if (ApiContract.isNotNull(title, "title") && ApiContract.isNotNull(text, "text"))
            title to text
        else
            null
    }

}
