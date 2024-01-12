package ru.zarina.zarina.data.rework.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.Category
import ru.zarina.zarina.domain.rework.common.Color

@Serializable
data class CategoryDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("code")
    val code: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("label")
    val label: String? = null,

    @SerialName("color")
    val color: String? = null,

    @SerialName("childs")
    val children: List<CategoryDto>? = null,
) {
    fun toCategory(): Category {
        checkNotNull(id) { "id is null" }
        checkNotNull(code) { "code is null" }
        checkNotNull(name) { "name is null" }
        return Category(
            id = Category.Id(id),
            code = Category.Code(code),
            name = name,
            label = label,
            color = color?.let { Color(it) },
            children = children?.map { it.toCategory() },
        )
    }
}
