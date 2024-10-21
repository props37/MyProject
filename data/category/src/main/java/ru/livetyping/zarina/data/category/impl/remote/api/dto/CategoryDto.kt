package ru.livetyping.zarina.data.category.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.common.Color
import timber.log.Timber

@Serializable
internal data class CategoryDto(
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
                id = Category.Id(id.toString()),
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
