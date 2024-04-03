package ru.livetyping.zarina.data.old.content.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.old.ApiContract
import ru.livetyping.zarina.data.old.product.remote.api.dto.FiltersDto
import ru.livetyping.zarina.domain.old.Action
import ru.livetyping.zarina.domain.old.Category
import ru.livetyping.zarina.domain.old.Product
import ru.livetyping.zarina.domain.old.Url

@Serializable
data class ActionDto(
    @SerialName("type")
    val type: String,
    @SerialName("payload")
    val payload: PayloadDto,
) {

    @Serializable
    data class PayloadDto(
        @SerialName("id")
        val id: String?,
        @SerialName("category_id")
        val categoryId: Int?,
        @SerialName("filters")
        val filters: FiltersDto?,
        @SerialName("link")
        val link: String?
    )

    fun toDomain(): Action? {
        return when (type) {
            "product" -> {
                if (ApiContract.isNotNull(payload.id, "id"))
                    Action.Product(id = Product.Id(payload.id))
                else
                    null
            }

            "products-list" -> {
                if (ApiContract.isNotNull(payload.categoryId, "category_id"))
                    Action.Products(
                        categoryId = Category.Id(payload.categoryId),
                        filtration = payload.filters?.toDomain(null)
                    )
                else
                    null
            }

            "web-view" -> {
                if (ApiContract.isNotNull(payload.link, "link"))
                    Action.Link(url = Url(payload.link))
                else
                    null
            }

            else -> null
        }
    }

}
