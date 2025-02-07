package ru.livetyping.zarina.data.category.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.category.Categories
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.category.CategoryPath
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

    fun getCategoryPath(categoryId: Category.Id): CategoryPath? {
        return dataHolder.getCategoryPath(categoryId)
    }
}
