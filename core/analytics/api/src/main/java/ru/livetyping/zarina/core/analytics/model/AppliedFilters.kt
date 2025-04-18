package ru.livetyping.zarina.core.analytics.model

public data class AppliedFilters(
    val sorting: Sorting?,
    val price: IntRange?,
    val materials: List<FilterItem>?,
    val sizes: List<FilterItem>?,
    val colors: List<FilterItem>?,
    val isDeliveryAvailable: Boolean?,
    val isStorePickupAvailable: Boolean?,
    val pickupStores: List<FilterItem>?,
) {
    public enum class Sorting {
        NEW,
        POPULAR,
        DISCOUNT,
        PRICE_LOW_TO_HIGH,
        PRICE_HIGH_TO_LOW;
    }

    public data class FilterItem(val name: String)
}
