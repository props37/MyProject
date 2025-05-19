package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.component

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable

internal class FilterComponent(
    savedStateHandle: SavedStateHandle,
    coroutineScope: CoroutineScope,
) {
    var initialFilters: ProductFilters? = null

    var availableFilters: ProductFilters? = null

    private val currentFiltersValueHolder = savedStateHandle.createValueHolder<ProductFiltersParcelable?>(
        key = Keys.FILTERS.key,
        initialValue = null,
    )

    val currentFilters = currentFiltersValueHolder.stateFlow
        .map {
            it?.toProductFilters() ?: run {
                val fallbackFilters = initialFilters
                    ?: ProductFilters.create(sorting = ProductFilters.getDefaultSorting())
                fallbackFilters
            }
        }
        .shareIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    fun getCurrentFilters(): ProductFilters? {
        return currentFiltersValueHolder.get()?.toProductFilters()
    }

    fun setCurrentFilters(filters: ProductFilters) {
        val parcelable = ProductFiltersParcelable.from(filters)
        currentFiltersValueHolder.set(parcelable)
    }

    private enum class Keys {
        FILTERS;

        val key: String get() = name
    }
}
