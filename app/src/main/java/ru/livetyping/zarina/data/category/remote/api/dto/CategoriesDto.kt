package ru.livetyping.zarina.data.category.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.CategoryDto
import ru.livetyping.zarina.domain.category.Categories

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
