package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.domain.model.category.Category

@Stable
internal sealed class SubcategoryListState {
    @Immutable
    data class Success(
        val categories: ImmutableList<Category>,
        val selectedCategoryId: Category.Id?,
    ) : SubcategoryListState()

    data object Empty : SubcategoryListState()

    data object Loading : SubcategoryListState()

    class Builder {
        fun build(
            categoryResult: Result<Category>?,
            selectedSubcategoryId: Category.Id?,
        ): SubcategoryListState {
            val category = categoryResult?.getOrNull()
            return if (category != null) {
                val children = category.children
                if (!children.isNullOrEmpty()) {
                    val subcategories = children.toImmutableList()
                    Success(subcategories, selectedSubcategoryId)
                } else {
                    Empty
                }
            } else {
                Loading
            }
        }
    }
}
