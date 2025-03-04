package ru.livetyping.zarina.core.uicomponent.filtration.listfilter.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.copy
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uimodel.product.filter.ProductListFilterParcelable

public class ListFilterComponent(
    savedStateHandle: SavedStateHandle,
    initialFilter: ProductListFilter<ProductListFilterItem>,
    private val coroutineScope: CoroutineScope,
) {
    private val filterValueHolder = savedStateHandle.createValueHolder<ProductListFilterParcelable?>(
        key = Keys.FILTER.key,
        initialValue = null,
    )

    public val filter: SharedFlow<ProductListFilter<ProductListFilterItem>> =
        filterValueHolder.stateFlow
            .map { parcelable ->
                parcelable?.toListFilter() ?: initialFilter
            }
            .shareIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1,
            )

    public val isResetFilterButtonVisible: StateFlow<Boolean> = filter
        .map { filter ->
            filter.type != ProductFilter.Type.SORTING && filter.selectedItems.isNotEmpty()
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    private val isApplyButtonVisibleValueHolder = savedStateHandle.createValueHolder<Boolean>(
        key = Keys.IS_APPLY_BUTTON_VISIBLE.key,
        initialValue = false,
    )

    public val isApplyButtonVisible: StateFlow<Boolean> = isApplyButtonVisibleValueHolder.stateFlow

    public fun toggleItem(item: ProductListFilterItem) {
        coroutineScope.launch {
            val filter = filter.firstOrNull() ?: return@launch
            val newItems = filter.items.map {
                when {
                    it.id == item.id -> {
                        val isSelected =
                            if (filter.type != ProductFilter.Type.SORTING) !item.isSelected else true
                        it.copy(isSelected = isSelected)
                    }

                    filter.isSingleSelection -> it.copy(isSelected = false)
                    else -> it
                }
            }
            val newFilter = filter.copy(items = newItems)
            val newFilterParcelable = ProductListFilterParcelable.from(newFilter)
            filterValueHolder.set(newFilterParcelable)
        }
    }

    public fun resetFilter() {
        coroutineScope.launch {
            val filter = filter.firstOrNull() ?: return@launch
            val resetItems = filter.items.map {
                if (it.isSelected) it.copy(isSelected = false) else it
            }
            val resetFilter = filter.copy(items = resetItems)
            filterValueHolder.set(ProductListFilterParcelable.from(resetFilter))
        }
    }

    public fun setIsApplyButtonVisible(isVisible: Boolean) {
        isApplyButtonVisibleValueHolder.set(isVisible)
    }

    private enum class Keys {
        FILTER,
        IS_APPLY_BUTTON_VISIBLE;

        val key: String get() = name
    }
}
