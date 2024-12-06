package ru.livetyping.zarina.core.uimodel.product.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Color
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductColorFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductMaterialFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductPickupStoreFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductSizeFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductSortFilterItem

@Serializable
@Parcelize
public data class ProductListFilterItemParcelable(
    val id: String,
    val name: String,
    val isSelected: Boolean,
    val color: String?,
) : Parcelable {
    public fun toSortFilterItem(): ProductSortFilterItem = ProductSortFilterItem(
        id = ProductListFilterItem.Id(id),
        name = name,
        isSelected = isSelected,
    )

    public fun toMaterialFilterItem(): ProductMaterialFilterItem = ProductMaterialFilterItem(
        id = ProductListFilterItem.Id(id),
        name = name,
        isSelected = isSelected,
    )

    public fun toSizeFilterItem(): ProductSizeFilterItem = ProductSizeFilterItem(
        id = ProductListFilterItem.Id(id),
        name = name,
        isSelected = isSelected,
    )

    public fun toColorFilterItem(): ProductColorFilterItem = ProductColorFilterItem(
        id = ProductListFilterItem.Id(id),
        name = name,
        isSelected = isSelected,
        color = color?.let { Color(color) },
    )

    public fun toPickupStoreFilterItem(): ProductPickupStoreFilterItem {
        return ProductPickupStoreFilterItem(
            id = ProductListFilterItem.Id(id),
            name = name,
            isSelected = isSelected,
        )
    }

    public companion object {
        public fun from(item: ProductListFilterItem): ProductListFilterItemParcelable {
            return ProductListFilterItemParcelable(
                id = item.id.value,
                name = item.name,
                isSelected = item.isSelected,
                color = if (item is ProductColorFilterItem) item.color?.value else null,
            )
        }
    }
}
