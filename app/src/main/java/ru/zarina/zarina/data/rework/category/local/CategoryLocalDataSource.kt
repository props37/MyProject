package ru.zarina.zarina.data.rework.category.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.common.Category
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    private val dataHolder: CategoryDataHolder,
) {
    fun getCategoriesFlow(): Flow<List<Category>?> {
        return dataHolder.getCategoriesFlow()
    }

    fun setCategories(categories: List<Category>) {
        dataHolder.setCategories(categories)
    }
}
