package ru.livetyping.zarina.data.category.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.core.domain.model.category.Category

internal interface CategoryLocalDataSource {
    fun getCategoriesFlow(): Flow<Categories?>

    fun getCategoryFlow(id: Category.Id): Flow<Category?>

    fun setCategories(categories: Categories?)
}
