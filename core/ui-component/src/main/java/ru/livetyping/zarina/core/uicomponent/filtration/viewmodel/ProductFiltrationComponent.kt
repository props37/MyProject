package ru.livetyping.zarina.core.uicomponent.filtration.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable

public class ProductFiltrationComponent(
    savedStateHandle: SavedStateHandle,
    initialFilters: ProductFilters?,
    private val coroutineScope: CoroutineScope,
) {
    private val filtersValueHolder = savedStateHandle.createValueHolder<ProductFiltersParcelable?>(
        key = Keys.FILTERS.key,
        initialValue = null,
    )

    public val filters: SharedFlow<ProductFilters?> = filtersValueHolder.stateFlow
        .map { filtersParcelable ->
            filtersParcelable?.toProductFilters() ?: initialFilters
        }
        .shareIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    private val _isRefreshing = MutableStateFlow(false)
    public val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    public val isResetFiltersButtonVisible: StateFlow<Boolean> = filters
        .map { filters ->
            filters?.hasAppliedIgnoringSorting == true
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    public val isPickupStoreFilterVisible: StateFlow<Boolean> = filters
        .map { filters ->
            filters?.storePickupAvailability?.isEnabled == true
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    public fun updateFiltersWith(filter: ProductFilter<*>) {
        coroutineScope.launch {
            val filters = filters.firstOrNull()
            val newFilters = filters?.updateWith(filter)
            setFilters(newFilters)
        }
    }

    public fun resetFilters() {
        coroutineScope.launch {
            val filters = filters.firstOrNull()
            if (filters != null) {
                val newFilters = filters.reset()
                setFilters(newFilters)
            }
        }
    }

    public fun setIsRefreshing(isRefreshing: Boolean) {
        _isRefreshing.value = isRefreshing
    }

    private fun setFilters(filters: ProductFilters?) {
        val filtersParcelable = filters?.let { ProductFiltersParcelable.from(it) }
        filtersValueHolder.set(filtersParcelable)
    }

    private enum class Keys {
        FILTERS;

        val key: String get() = name
    }
}
