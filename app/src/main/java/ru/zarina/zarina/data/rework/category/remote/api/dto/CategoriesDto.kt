package ru.zarina.zarina.data.rework.category.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.rework.common.remote.api.dto.CategoryDto
import ru.zarina.zarina.domain.rework.category.Categories

@Serializable
data class CategoriesDto(
    @SerialName("woman")
    val women: List<CategoryDto>? = null,

    @SerialName("man")
    val men: List<CategoryDto>? = null,
) {
    fun toCategories(): Categories {
        return Categories(
            women = women?.mapNotNull { it.toCategory() } ?: emptyList(),
            men = men?.mapNotNull { it.toCategory() } ?: emptyList(),
        )
    }
}
