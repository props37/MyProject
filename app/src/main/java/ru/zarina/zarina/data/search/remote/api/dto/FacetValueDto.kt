package ru.zarina.zarina.data.search.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.ListFilter
import ru.zarina.zarina.domain.TreeFilter

@Serializable
data class FacetValueDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("value")
    val value: Long? = null,
    @SerialName("pictureUrl")
    val pictureUrl: String? = null,
    @SerialName("children")
    val children: List<FacetValueDto>? = null,
    @SerialName("open")
    val isOpen: Boolean? = null,
    @SerialName("selected")
    val isSelected: Boolean? = null,
) {
    fun toListFilterItem(): ListFilter.Item? {
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(name, "name")
        ) {
            ListFilter.Item(
                id = id,
                name = name,
                isSelected = isSelected ?: false,
                color = null,
            )
        } else {
            null
        }
    }

    fun toTreeFilterItem(): TreeFilter.Item? {
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(name, "name")
        ) {
            val children = children?.mapNotNull { it.toTreeFilterItem() }.orEmpty()
            TreeFilter.Item(
                id = id,
                name = name,
                isExplicitSelected = isSelected ?: false,
                color = null,
                children = children,
            )
        } else {
            null
        }
    }
}
