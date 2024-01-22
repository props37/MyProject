package ru.zarina.zarina.data.rework.category.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.category.Categories
import ru.zarina.zarina.domain.rework.category.Category
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    private val dataHolder: CategoryDataHolder,
) {
    fun getCategoriesFlow(): Flow<Categories?> {
        return dataHolder.getCategoriesFlow()
    }

    fun getCategoryFlow(id: Category.Id): Flow<Category?> {
        return dataHolder.getCategoryFlow(id)
    }

    fun setCategories(categories: Categories) {
        dataHolder.setCategories(categories)
    }
}
