package ru.zarina.zarina.data.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.ListFilter

@Serializable
data class AttributeFilterDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("is_applied")
    val isApplied: Boolean? = null,
) {

    fun toDomain(): ListFilter.Item? {
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(name, "name")
        )
            ListFilter.Item(
                id = id,
                name = name,
                isSelected = isApplied ?: false,
                color = null
            )
        else
            null
    }

}
