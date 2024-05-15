package ru.livetyping.zarina.data.common.remote.api.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Color
import ru.livetyping.zarina.domain.filter.ColorFilterItem
import ru.livetyping.zarina.domain.filter.Filter
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.filter.ListFilter
import ru.livetyping.zarina.domain.filter.ListFilterItem
import ru.livetyping.zarina.domain.filter.MaterialFilterItem
import ru.livetyping.zarina.domain.filter.PickupStoreFilterItem
import ru.livetyping.zarina.domain.filter.PriceFilter
import ru.livetyping.zarina.domain.filter.SizeFilterItem
import ru.livetyping.zarina.domain.filter.ToggleFilter
import timber.log.Timber

@Serializable
data class FiltersDto(
    @SerialName("price")
    val price: PriceFilterDto? = null,

    @SerialName("materials")
    val materials: List<BasicItem>? = null,

    @SerialName("sizes")
    val sizes: List<BasicItem>? = null,

    @SerialName("colors")
    val colors: List<ColorItem>? = null,

    @SerialName("available_for_shipping")
    val deliveryAvailability: DeliveryAvailability? = null,

    @SerialName("available_for_store_pickup")
    val storePickupAvailability: StorePickupAvailability? = null,
) {
    fun toFilters(): Filters {
        val price = price?.let { PriceFilter(min = null, max = null, limits = it.toPriceRange()) }
        val materials = if (!materials.isNullOrEmpty()) {
            val items = materials
                .mapNotNull { it.toMaterialFilterItem() }
                .distinctBy { it.id } // TODO: [High] Remove when ID is fixed on backend
            if (items.isNotEmpty()) {
                ListFilter(
                    items = items,
                    isSingleSelection = false,
                    type = Filter.Type.MATERIALS,
                )
            } else null
        } else null
        val sizes = if (!sizes.isNullOrEmpty()) {
            val items = sizes
                .mapNotNull { it.toSizeFilterItem() }
                .distinctBy { it.id } // TODO: [High] Remove when ID is fixed on backend
            if (items.isNotEmpty()) {
                ListFilter(
                    items = items,
                    isSingleSelection = false,
                    type = Filter.Type.SIZES,
                )
            } else null
        } else null
        val colors = if (!colors.isNullOrEmpty()) {
            val items = colors
                .mapNotNull { it.toColorFilterItem() }
                .distinctBy { it.id } // TODO: [High] Remove when ID is fixed on backend
            if (items.isNotEmpty()) {
                ListFilter(
                    items = items,
                    isSingleSelection = false,
                    type = Filter.Type.COLORS,
                )
            } else null
        } else null
        val deliveryAvailability = deliveryAvailability?.let {
            if (it.isAvailable != false) {
                ToggleFilter(
                    isEnabled = deliveryAvailability.isApplied ?: false,
                    type = Filter.Type.DELIVERY_AVAILABILITY,
                )
            } else null
        }
        val storePickupAvailability = storePickupAvailability?.let {
            if (it.isAvailable != false) {
                ToggleFilter(
                    isEnabled = storePickupAvailability.isApplied ?: false,
                    type = Filter.Type.STORE_PICKUP_AVAILABILITY,
                )
            } else null
        }
        val pickupStores = if (this.storePickupAvailability?.stores != null) {
            val items = this.storePickupAvailability.stores
                .mapNotNull { it.toPickupStoreFilterItem() }
            if (items.isNotEmpty()) {
                ListFilter(
                    items = items,
                    isSingleSelection = false,
                    type = Filter.Type.PICKUP_STORES,
                )
            } else null
        } else null
        return Filters(
            sorting = Filters.getDefaultSorting(),
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
    data class BasicItem(
        @SerialName("id")
        val id: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("is_applied")
        val isApplied: Boolean? = null,

        @SerialName("available")
        val available: Boolean? = null,
    ) {
        fun toMaterialFilterItem(): MaterialFilterItem? {
            if (available == false) return null
            return if (id != null && name != null && isApplied != null) {
                MaterialFilterItem(
                    id = ListFilterItem.Id(id),
                    name = name,
                    isSelected = isApplied,
                )
            } else {
                Timber.e("Drop MaterialFilterItem because its ID, name or isApplied is null")
                null
            }
        }

        fun toSizeFilterItem(): SizeFilterItem? {
            if (available == false) return null
            return if (id != null && name != null && isApplied != null) {
                SizeFilterItem(
                    id = ListFilterItem.Id(id),
                    name = name,
                    isSelected = isApplied,
                )
            } else {
                Timber.e("Drop SizeFilterItem because its ID, name or isApplied is null")
                null
            }
        }
    }

    @Serializable
    data class ColorItem(
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
        fun toColorFilterItem(): ColorFilterItem? {
            if (isAvailable == false) return null
            return if (id != null && name != null && code != null && isApplied != null) {
                ColorFilterItem(
                    id = ListFilterItem.Id(id),
                    name = name,
                    isSelected = isApplied,
                    color = Color(code),
                )
            } else {
                Timber.e("Drop ColorFilterItem because its ID, name, color code or isApplied is null")
                null
            }
        }
    }
    
    @Serializable
    data class DeliveryAvailability(
        @SerialName("is_applied")
        val isApplied: Boolean? = null,

        @SerialName("available")
        val isAvailable: Boolean? = null,
    )

    @Serializable
    data class StorePickupAvailability(
        @SerialName("is_applied")
        val isApplied: Boolean? = null,

        @SerialName("available")
        val isAvailable: Boolean? = null,

        @SerialName("shops")
        val stores: List<Store>? = null,
    ) {
        @Serializable
        data class Store(
            @SerialName("id")
            val id: String? = null,

            @SerialName("name")
            val name: String? = null,

            @SerialName("available")
            val isAvailable: Boolean? = null,
        ) {
            fun toPickupStoreFilterItem(): PickupStoreFilterItem? {
                if (isAvailable == false) return null
                return if (id != null && name != null) {
                    PickupStoreFilterItem(
                        id = ListFilterItem.Id(id),
                        name = name,
                        isSelected = false,
                    )
                } else {
                    Timber.e("Drop PickupStoreFilterItem because its ID or name is null")
                    null
                }
            }
        }
    }
}
