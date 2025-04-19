package ru.livetyping.zarina.core.domain.analytics

import ru.livetyping.zarina.core.analytics.model.AppliedFilters
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.product.filter.list.selected
import ru.livetyping.zarina.core.analytics.model.AppliedFilters as AppMetricaFilters

public fun ProductFilters.toAppMetricaFilters(): AppMetricaFilters {
    val sorting = sorting?.selected?.toAppMetricaFiltersSorting()
    val price = price?.let {
        AppMetricaFilters.PriceRange(min = it.min, max = it.max)
    }
    val materials = materials?.selectedItems?.map { AppliedFilters.FilterItem(name = it.name) }
    val sizes = sizes?.selectedItems?.map { AppliedFilters.FilterItem(it.name) }
    val colors = colors?.selectedItems?.map { AppliedFilters.FilterItem(it.name) }
    val isDeliveryAvailable = deliveryAvailability?.isApplied?.takeIf { it }
    val isStorePickupAvailable = storePickupAvailability?.isApplied?.takeIf { it }
    val pickupStores = pickupStores?.selectedItems?.map { AppliedFilters.FilterItem(it.name) }
    return AppMetricaFilters(
        sorting = sorting,
        price = price,
        materials = materials,
        sizes = sizes,
        colors = colors,
        isDeliveryAvailable = isDeliveryAvailable,
        isStorePickupAvailable = isStorePickupAvailable,
        pickupStores = pickupStores,
    )
}

private fun ProductSorting.toAppMetricaFiltersSorting(): AppMetricaFilters.Sorting {
    return when (this) {
        ProductSorting.NEW -> AppMetricaFilters.Sorting.NEW
        ProductSorting.POPULAR -> AppMetricaFilters.Sorting.POPULAR
        ProductSorting.DISCOUNT -> AppMetricaFilters.Sorting.DISCOUNT
        ProductSorting.PRICE_LOW_TO_HIGH -> AppMetricaFilters.Sorting.PRICE_LOW_TO_HIGH
        ProductSorting.PRICE_HIGH_TO_LOW -> AppMetricaFilters.Sorting.PRICE_HIGH_TO_LOW
    }
}
