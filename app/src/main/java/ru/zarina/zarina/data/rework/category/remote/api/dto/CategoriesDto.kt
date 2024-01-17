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
        checkNotNull(women) { "women is null" }
        checkNotNull(men) { "men is null" }
        return Categories(
            women = women.map { it.toCategory() },
            men = men.map { it.toCategory() },
        )
    }
}
