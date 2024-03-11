package ru.zarina.zarina.data.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.rework.common.Color
import timber.log.Timber

@Serializable
data class CategoryDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("label")
    val label: String? = null,

    @SerialName("color")
    val color: String? = null,

    @SerialName("is_expandable")
    val isExpandable: Boolean? = null,

    @SerialName("childs")
    val children: List<CategoryDto>? = null,
) {
    fun toCategory(): Category? {
        return if (id != null && name != null) {
            Category(
                id = Category.Id(id),
                name = name,
                label = label,
                color = color?.let { Color(it) },
                isExpandable = isExpandable ?: false,
                children = children?.mapNotNull { it.toCategory() },
            )
        } else {
            Timber.e("Drop Category because its ID or name is null")
            null
        }
    }
}
