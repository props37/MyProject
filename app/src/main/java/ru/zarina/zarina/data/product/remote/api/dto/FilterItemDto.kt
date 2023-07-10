package ru.zarina.zarina.data.product.remote.api.dto

import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.ListFilter

interface FilterItemDto {
    val id: String?
    val name: String?
    val isApplied: Boolean?

    fun toDomain(): ListFilter.Item? {
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(name, "name")
        )
            ListFilter.Item(
                id = checkNotNull(id),
                name = checkNotNull(name),
                isSelected = isApplied ?: false,
            )
        else
            null
    }

}
