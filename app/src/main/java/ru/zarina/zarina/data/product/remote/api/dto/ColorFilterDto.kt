package ru.zarina.zarina.data.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.Color
import ru.zarina.zarina.domain.ListFilter

@Serializable
data class ColorFilterDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("code")
    val code: String? = null,
    @SerialName("is_applied")
    val isApplied: Boolean? = null,
) {

    fun toDomain(): ListFilter.Item? {
        val color = this.toColor() ?: return null
        return ListFilter.Item(
            id = color.id,
            name = color.name,
            color = color,
            isSelected = this.isApplied ?: false
        )
    }

    private fun toColor(): Color? {
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(title, "title")
            && ApiContract.isNotNull(code, "code")
        ) {
            Color(
                id = id,
                name = title,
                code = Color.Code(code)
            )
        } else {
            null
        }
    }

}
