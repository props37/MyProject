package ru.livetyping.zarina.core.uicomponent.filtration.listfilter.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.copy
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uimodel.product.filter.ProductListFilterParcelable

public class ListFilterComponent(
    savedStateHandle: SavedStateHandle,
    initialFilter: ProductListFilter<ProductListFilterItem>,
    coroutineScope: CoroutineScope,
) {
    private val filterValueHolder = savedStateHandle.createValueHolder<ProductListFilterParcelable?>(
        key = Keys.FILTER.key,
        initialValue = null,
    )

    public val filter: StateFlow<ProductListFilter<ProductListFilterItem>> =
        filterValueHolder.stateFlow.mapState(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            parcelable?.toListFilter() ?: initialFilter
        }

    public val isResetFilterButtonVisible: StateFlow<Boolean> = filter.mapState(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
    ) { filter ->
        filter.type != ProductFilter.Type.SORTING && filter.selectedItems.isNotEmpty()
    }

    private val isApplyButtonVisibleValueHolder = savedStateHandle.createValueHolder<Boolean>(
        key = Keys.IS_APPLY_BUTTON_VISIBLE.key,
        initialValue = false,
    )

    public val isApplyButtonVisible: StateFlow<Boolean> = isApplyButtonVisibleValueHolder.stateFlow

    public fun getFilter(): ProductListFilter<ProductListFilterItem> {
        return filter.value
    }

    public fun toggleItem(item: ProductListFilterItem) {
        val filter = getFilter()
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

    public fun setIsApplyButtonVisible(isVisible: Boolean) {
        isApplyButtonVisibleValueHolder.set(isVisible)
    }

    private enum class Keys {
        FILTER,
        IS_APPLY_BUTTON_VISIBLE;

        val key: String get() = name
    }
}
