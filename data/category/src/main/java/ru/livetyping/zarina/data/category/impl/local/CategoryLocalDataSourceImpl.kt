package ru.livetyping.zarina.data.category.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.core.domain.model.category.Category
import javax.inject.Inject

internal class CategoryLocalDataSourceImpl @Inject constructor(
    private val dataHolder: CategoryDataHolder,
) : CategoryLocalDataSource {
    override fun getCategoriesFlow(): Flow<Categories?> {
        return dataHolder.getCategoriesFlow()
    }

    override fun getCategoryFlow(id: Category.Id): Flow<Category?> {
        return dataHolder.getCategoryFlow(id)
    }

    override fun setCategories(categories: Categories?) {
        dataHolder.setCategories(categories)
    }
}
