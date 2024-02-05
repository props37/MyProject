package ru.zarina.zarina.data.rework.product.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.zarina.zarina.data.rework.common.remote.api.dto.PriceFilterDto
import ru.zarina.zarina.data.rework.common.remote.api.dto.SortingDto
import ru.zarina.zarina.data.rework.product.remote.api.dto.ProductsDto
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.common.Sorting
import ru.zarina.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject
import ru.zarina.zarina.domain.rework.filter.Filters as DomainFilters

class ProductApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getProducts(
        categoryId: Category.Id,
        filters: DomainFilters?,
        sorting: Sorting,
        page: Int,
    ): ProductsDto {
        val body = GetProductsBody(
            categoryId = categoryId.value,
            filters = filters?.let { FiltersBodyDto.from(it) },
            sorting = SortingDto.from(sorting),
            page = page,
        )
        return httpClient.post("/api/v1/products") {
            setJsonBody(body)
        }.body()
    }

    suspend fun getCategoryProductInfo(
        categoryId: Category.Id,
        filters: DomainFilters?,
    ): ProductsDto {
        val body = GetProductsBody(
            categoryId = categoryId.value,
            filters = filters?.let { FiltersBodyDto.from(it) },
            sorting = SortingDto.from(Sorting.getDefault()),
            page = 1,
            returnProducts = false,
        )
        return httpClient.post("/api/v1/products") {
            setJsonBody(body)
        }.body()
    }

    @Serializable
    private data class GetProductsBody(
        @SerialName("category_id")
        val categoryId: Long,

        @SerialName("filters")
        val filters: FiltersBodyDto?,

        @SerialName("sort")
        val sorting: SortingDto,

        @SerialName("page")
        val page: Int,

        @Transient
        val returnProducts: Boolean = true,
    ) {
        @SerialName("count")
        val itemCount: Boolean? = if (!returnProducts) true else null

        @SerialName("filterRanges")
        val filterRanges: Boolean? = if (!returnProducts) true else null
    }

    @Serializable
    private data class FiltersBodyDto(
        @SerialName("price")
        val price: PriceFilterDto? = null,

        @SerialName("materials")
        val materials: List<String>? = null,

        @SerialName("sizes")
        val sizes: List<String>? = null,

        @SerialName("colors")
        val colors: List<String>? = null,

        @SerialName("available_for_shipping")
        val availableForDelivery: Boolean? = null,

        @SerialName("available_for_store_pickup")
        val availableForStorePickup: StorePickupAvailability? = null,
    ) {
        @Serializable
        data class StorePickupAvailability(
            @SerialName("applied")
            val isApplied: Boolean,
        )

        companion object {
            fun from(filters: DomainFilters): FiltersBodyDto? {
                return if (!filters.isEmptyIgnoringSorting) {
                    val materials = filters.materials?.let { filter ->
                        if (!filter.isEmpty) filter.selectedItems.map { it.id.value } else null
                    }
                    val sizes = filters.sizes?.let { filter ->
                        if (!filter.isEmpty) filter.selectedItems.map { it.id.value } else null
                    }
                    val colors = filters.colors?.let { filter ->
                        if (!filter.isEmpty) filter.selectedItems.map { it.id.value } else null
                    }
                    val availableForDelivery = filters.deliveryAvailability?.let { filter ->
                        if (filter.isEnabled) true else null
                    }
                    val availableForStorePickup = filters.storePickupAvailability?.let { filter ->
                        if (filter.isEnabled) StorePickupAvailability(isApplied = true) else null
                    }
                    FiltersBodyDto(
                        price = filters.price?.let { PriceFilterDto.from(it) },
                        materials = materials,
                        sizes = sizes,
                        colors = colors,
                        availableForDelivery = availableForDelivery,
                        availableForStorePickup = availableForStorePickup,
                    )
                } else {
                    null
                }
            }
        }
    }
}
