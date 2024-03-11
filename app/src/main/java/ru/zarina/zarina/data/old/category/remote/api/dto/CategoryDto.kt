package ru.zarina.zarina.data.old.category.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.old.ApiContract
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Url

@Serializable
data class CategoryDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("list_image")
    val listImage: String? = null,
    @SerialName("subs")
    val subcategories: List<CategoryDto>? = null,
) {

    fun toDomain(): Category? {
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(name, "name")
        ) {
            Category(
                id = Category.Id(id),
                name = name,
                image = listImage?.let { Url(it) },
                subcategories = subcategories
                    // TODO Remove this filter once all subcategories have unique ids of their own
                    ?.filter { it.id != id }
                    ?.mapNotNull { it.toDomain() }
                    .orEmpty()
            )
        } else {
            null
        }
    }

}
