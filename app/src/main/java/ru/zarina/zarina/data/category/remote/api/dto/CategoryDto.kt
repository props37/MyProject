package ru.zarina.zarina.data.category.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("list_image")
    val listImage: String? = null,
    @SerialName("subs")
    val subs: List<CategoryDto>? = null,
)
