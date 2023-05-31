package ru.zarina.zarina.data.category.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponseDto(
    @SerialName("categories")
    val categories: List<CategoryDto>? = listOf(),
)
