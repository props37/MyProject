package ru.zarina.zarina.ui.screen.catalog.tooling.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentSet
import ru.zarina.zarina.domain.rework.common.Category
import ru.zarina.zarina.ui.common.base.ErrorStateRework
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel

class CategoryListStatePreviewParameterProvider :
    PreviewParameterProvider<CatalogViewModel.CategoryListState> {

    override val values: Sequence<CatalogViewModel.CategoryListState>
        get() = sequenceOf(
            CatalogViewModel.CategoryListState.Success(
                womenItems = getCategoryListItems().toImmutableList(),
                menItems = getCategoryListItems().toImmutableList(),
            ),
            CatalogViewModel.CategoryListState.Loading,
            CatalogViewModel.CategoryListState.Error(ErrorStateRework.NETWORK),
        )

    private fun getCategoryListItems(): List<CatalogViewModel.CategoryListItem> {
        val baseCategory = Category(
            id = Category.Id(0),
            name = "Категория",
            label = null,
            color = null,
            children = null,
        )
        return listOf(
            CatalogViewModel.CategoryListItem.CategoryItem(
                category = baseCategory.copy(id = Category.Id(1)),
                nestingLevel = 0,
                isExpandable = false,
            ),
            CatalogViewModel.CategoryListItem.CategoryItem(
                category = baseCategory.copy(id = Category.Id(2)),
                nestingLevel = 0,
                isExpandable = false,
            ),
            CatalogViewModel.CategoryListItem.CategoryItem(
                category = baseCategory.copy(id = Category.Id(3)),
                nestingLevel = 0,
                isExpandable = true,
            ),
            CatalogViewModel.CategoryListItem.CategoryItem(
                category = baseCategory.copy(id = Category.Id(4)),
                nestingLevel = 1,
                isExpandable = false,
            ),
            CatalogViewModel.CategoryListItem.CategoryItem(
                category = baseCategory.copy(id = Category.Id(5)),
                nestingLevel = 1,
                isExpandable = false,
            ),
            CatalogViewModel.CategoryListItem.CategoryItem(
                category = baseCategory.copy(id = Category.Id(6)),
                nestingLevel = 1,
                isExpandable = true,
            ),
            CatalogViewModel.CategoryListItem.CategoryItem(
                category = baseCategory.copy(id = Category.Id(7)),
                nestingLevel = 2,
                isExpandable = false,
            ),
            CatalogViewModel.CategoryListItem.CategoryItem(
                category = baseCategory.copy(id = Category.Id(8)),
                nestingLevel = 2,
                isExpandable = false,
            ),
            CatalogViewModel.CategoryListItem.CategoryItem(
                category = baseCategory.copy(id = Category.Id(9)),
                nestingLevel = 1,
                isExpandable = true,
            ),
            CatalogViewModel.CategoryListItem.CategoryItem(
                category = baseCategory.copy(id = Category.Id(10)),
                nestingLevel = 0,
                isExpandable = false,
            ),
            CatalogViewModel.CategoryListItem.CategoryItem(
                category = baseCategory.copy(id = Category.Id(11)),
                nestingLevel = 0,
                isExpandable = true,
            ),
        )
    }

    companion object {
        fun getCategoryListItemsStatePreview(): CatalogViewModel.CategoryListItemsState {
            val visibleCategoryIds = List(11) {
                Category.Id((it + 1).toLong())
            }.toPersistentSet()
            return CatalogViewModel.CategoryListItemsState(
                visibleCategoryIds = visibleCategoryIds,
                expandedCategoryIds = persistentSetOf(
                    Category.Id(3),
                    Category.Id(6),
                ),
            )
        }
    }
}
