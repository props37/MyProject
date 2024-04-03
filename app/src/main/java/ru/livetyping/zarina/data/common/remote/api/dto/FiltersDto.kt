package ru.livetyping.zarina.data.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Color
import ru.livetyping.zarina.domain.filter.ColorFilterItem
import ru.livetyping.zarina.domain.filter.Filter
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.filter.ListFilter
import ru.livetyping.zarina.domain.filter.ListFilterItem
import ru.livetyping.zarina.domain.filter.MaterialFilterItem
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
    val availableForDelivery: Boolean? = null,

    @SerialName("available_for_store_pickup")
    val availableForStorePickup: StorePickupAvailability? = null,
) {
    fun toFilters(): Filters {
        val price = price?.let { PriceFilter(min = null, max = null, limits = it.toPriceRange()) }
        val materials = if (!materials.isNullOrEmpty()) {
            ListFilter(
                items = materials
                    .mapNotNull { it.toMaterialFilterItem() }
                    .distinctBy { it.id }, // TODO: [High] Remove when ID is fixed on backend
                isSingleSelection = false,
                type = Filter.Type.MATERIALS,
            )
        } else null
        val sizes = if (!sizes.isNullOrEmpty()) {
            ListFilter(
                items = sizes
                    .mapNotNull { it.toSizeFilterItem() }
                    .distinctBy { it.id }, // TODO: [High] Remove when ID is fixed on backend
                isSingleSelection = false,
                type = Filter.Type.SIZES,
            )
        } else null
        val colors = if (!colors.isNullOrEmpty()) {
            ListFilter(
                items = colors
                    .mapNotNull { it.toColorFilterItem() }
                    .distinctBy { it.id }, // TODO: [High] Remove when ID is fixed on backend
                isSingleSelection = false,
                type = Filter.Type.COLORS,
            )
        } else null
        val deliveryAvailability = availableForDelivery?.let {
            ToggleFilter(
                isEnabled = availableForDelivery,
                type = Filter.Type.DELIVERY_AVAILABILITY,
            )
        }
        val storePickupAvailability = availableForStorePickup?.let {
            if (it.available != false) {
                ToggleFilter(
                    isEnabled = availableForStorePickup.isApplied ?: false,
                    type = Filter.Type.STORE_PICKUP_AVAILABILITY,
                )
            } else null
        }
        return Filters(
            sorting = Filters.getDefaultSorting(),
            price = price,
            materials = materials,
            sizes = sizes,
            colors = colors,
            deliveryAvailability = deliveryAvailability,
            storePickupAvailability = storePickupAvailability,
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
        val available: Boolean? = null,
    ) {
        fun toColorFilterItem(): ColorFilterItem? {
            if (available == false) return null
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
    data class StorePickupAvailability(
        @SerialName("is_applied")
        val isApplied: Boolean? = null,

        @SerialName("available")
        val available: Boolean? = null,
    )
}
