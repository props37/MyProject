package ru.livetyping.zarina.core.uicomponent.filtration.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable

public class ProductFiltrationComponent(
    savedStateHandle: SavedStateHandle,
    initialFilters: ProductFilters?,
    coroutineScope: CoroutineScope,
) {
    private val filtersValueHolder = savedStateHandle.createValueHolder<ProductFiltersParcelable?>(
        key = Keys.FILTERS.key,
        initialValue = null,
    )

    public val filters: StateFlow<ProductFilters?> = filtersValueHolder.stateFlow.mapState(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
    ) { filtersParcelable ->
        filtersParcelable?.toFilters() ?: initialFilters
    }

    private val _isRefreshing = MutableStateFlow(false)
    public val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    public val isResetFiltersButtonVisible: StateFlow<Boolean> = filters.mapState(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
    ) { filters ->
        filters?.hasAppliedIgnoringSorting == true
    }

    public val isPickupStoreFilterVisible: StateFlow<Boolean> = filters.mapState(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
    ) { filters ->
        filters?.storePickupAvailability?.isEnabled == true
    }

    public fun getFilters(): ProductFilters? {
        return filters.value
    }

    public fun setFilters(filters: ProductFilters?) {
        val filtersParcelable = filters?.let { ProductFiltersParcelable.from(it) }
        filtersValueHolder.set(filtersParcelable)
    }

    public fun setIsRefreshing(isRefreshing: Boolean) {
        _isRefreshing.value = isRefreshing
    }

    private enum class Keys {
        FILTERS;

        val key: String get() = name
    }
}
