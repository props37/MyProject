package ru.zarina.zarina.data.rework.category.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.common.Categories
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    private val dataHolder: CategoryDataHolder,
) {
    fun getCategoriesFlow(): Flow<Categories?> {
        return dataHolder.getCategoriesFlow()
    }

    fun setCategories(categories: Categories) {
        dataHolder.setCategories(categories)
    }
}
