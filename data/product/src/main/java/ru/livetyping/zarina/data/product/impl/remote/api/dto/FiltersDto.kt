package ru.livetyping.zarina.data.product.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Color
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.product.filter.ProductPriceFilter
import ru.livetyping.zarina.core.domain.model.product.filter.ProductToggleFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductColorFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductMaterialFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductPickupStoreFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductSizeFilterItem
import timber.log.Timber

@Serializable
internal data class FiltersDto(
    @SerialName("price")
    val price: PriceFilterDto? = null,

    @SerialName("materials")
    val materials: List<BasicItemDto>? = null,

    @SerialName("sizes")
    val sizes: List<BasicItemDto>? = null,

    @SerialName("colors")
    val colors: List<ColorItemDto>? = null,

    @SerialName("available")
    val availability: List<AvailabilityDto>? = null,

    @SerialName("shops")
    val pickupStores: List<StoreDto>? = null,
) {
    fun toFilters(): ProductFilters {
        val price = price?.let { ProductPriceFilter(min = null, max = null, limits = it.toPriceRange()) }
        val materials = if (!materials.isNullOrEmpty()) {
            val items = materials
                .mapNotNull { it.toMaterialFilterItem() }
                .distinctBy { it.id } // TODO: [Backend] Remove when ID is fixed on backend
            if (items.isNotEmpty()) {
                ProductListFilter(
                    items = items,
                    isSingleSelection = false,
                    type = ProductFilter.Type.MATERIALS,
                )
            } else null
        } else null
        val sizes = if (!sizes.isNullOrEmpty()) {
            val items = sizes
                .mapNotNull { it.toSizeFilterItem() }
                .distinctBy { it.id } // TODO: [Backend] Remove when ID is fixed on backend
            if (items.isNotEmpty()) {
                ProductListFilter(
                    items = items,
                    isSingleSelection = false,
                    type = ProductFilter.Type.SIZES,
                )
            } else null
        } else null
        val colors = if (!colors.isNullOrEmpty()) {
            val items = colors
                .mapNotNull { it.toColorFilterItem() }
                .distinctBy { it.id } // TODO: [Backend] Remove when ID is fixed on backend
            if (items.isNotEmpty()) {
                ProductListFilter(
                    items = items,
                    isSingleSelection = false,
                    type = ProductFilter.Type.COLORS,
                )
            } else null
        } else null
        val deliveryAvailability = availability?.let { list ->
            list
                .find { it.id == AvailabilityDto.DELIVERY_AVAILABILITY_ID }
                ?.toDeliveryAvailabilityFilter()
        }
        val storePickupAvailability = availability?.let { list ->
            list
                .find { it.id == AvailabilityDto.STORE_AVAILABILITY_PICKUP_ID }
                ?.toStorePickupAvailabilityFilter()
        }
        val pickupStores = pickupStores?.let { stores ->
            val items = stores.mapNotNull { it.toPickupStoreFilterItem() }
            if (items.isNotEmpty()) {
                ProductListFilter(
                    items = items,
                    isSingleSelection = false,
                    type = ProductFilter.Type.PICKUP_STORES,
                )
            } else null
        }
        return ProductFilters(
            sorting = ProductFilters.getDefaultSorting(),
            price = price,
            materials = materials,
            sizes = sizes,
            colors = colors,
            deliveryAvailability = deliveryAvailability,
            storePickupAvailability = storePickupAvailability,
            pickupStores = pickupStores,
        )
    }

    @Serializable
    data class BasicItemDto(
        @SerialName("id")
        val id: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("is_applied")
        val isApplied: Boolean? = null,

        @SerialName("available")
        val available: Boolean? = null,
    ) {
        fun toMaterialFilterItem(): ProductMaterialFilterItem? {
            if (available == false) return null
            return if (id != null && name != null && isApplied != null) {
                ProductMaterialFilterItem(
                    id = ProductListFilterItem.Id(id),
                    name = name,
                    isSelected = isApplied,
                )
            } else {
                Timber.tag(TAG).e("Drop MaterialFilterItem $this because its ID, name or isApplied is null")
                null
            }
        }

        fun toSizeFilterItem(): ProductSizeFilterItem? {
            if (available == false) return null
            return if (id != null && name != null && isApplied != null) {
                ProductSizeFilterItem(
                    id = ProductListFilterItem.Id(id),
                    name = name,
                    isSelected = isApplied,
                )
            } else {
                Timber.tag(TAG).e("Drop SizeFilterItem $this because its ID, name or isApplied is null")
                null
            }
        }
    }

    @Serializable
    data class ColorItemDto(
        @SerialName("id")
        val id: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("code")
        val code: String? = null,

        @SerialName("is_applied")
        val isApplied: Boolean? = null,

        @SerialName("available")
        val isAvailable: Boolean? = null,
    ) {
        fun toColorFilterItem(): ProductColorFilterItem? {
            if (isAvailable == false) return null
            return if (id != null && name != null && code != null && isApplied != null) {
                ProductColorFilterItem(
                    id = ProductListFilterItem.Id(id),
                    name = name,
                    isSelected = isApplied,
                    color = Color(code),
                )
            } else {
                Timber.tag(TAG).e("Drop ColorFilterItem $this because its ID, name, color code or isApplied is null")
                null
            }
        }
    }

    @Serializable
    data class AvailabilityDto(
        @SerialName("id")
        val id: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("is_applied")
        val isApplied: Boolean? = null,

        @SerialName("available")
        val isAvailable: Boolean? = null,
    ) {
        fun toStorePickupAvailabilityFilter(): ProductToggleFilter {
            return ProductToggleFilter(
                isEnabled = isApplied ?: false,
                type = ProductFilter.Type.STORE_PICKUP_AVAILABILITY,
            )
        }

        fun toDeliveryAvailabilityFilter(): ProductToggleFilter {
            return ProductToggleFilter(
                isEnabled = isApplied ?: false,
                type = ProductFilter.Type.DELIVERY_AVAILABILITY,
            )
        }

        companion object {
            const val DELIVERY_AVAILABILITY_ID = "onlineAvailable"
            const val STORE_AVAILABILITY_PICKUP_ID = "retailAvailable"
        }
    }

    @Serializable
    data class StoreDto(
        @SerialName("id")
        val id: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("available")
        val isAvailable: Boolean? = null,
    ) {
        fun toPickupStoreFilterItem(): ProductPickupStoreFilterItem? {
            if (isAvailable == false) return null
            return if (id != null && name != null) {
                ProductPickupStoreFilterItem(
                    id = ProductListFilterItem.Id(id),
                    name = name,
                    isSelected = false,
                )
            } else {
                Timber.tag(TAG).e("Drop PickupStoreFilterItem $this because its ID or name is null")
                null
            }
        }
    }

    private companion object {
        private const val TAG = "FiltersDto"
    }
}
