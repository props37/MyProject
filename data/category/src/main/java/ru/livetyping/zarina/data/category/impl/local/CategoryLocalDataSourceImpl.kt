package ru.livetyping.zarina.data.category.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Categories
import javax.inject.Inject

internal class CategoryLocalDataSourceImpl @Inject constructor(
    private val dataHolder: CategoryDataHolder,
) : CategoryLocalDataSource {
    override fun getCategoriesFlow(): Flow<Categories?> {
        return dataHolder.getCategoriesFlow()
    }

    override fun setCategories(categories: Categories) {
        dataHolder.setCategories(categories)
    }
}
