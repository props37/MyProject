package ru.livetyping.zarina.data.category.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.category.CategoryPath

internal interface CategoryDataHolder {
    fun getCategoriesFlow(): Flow<Categories?>

    fun getCategoryFlow(id: Category.Id): Flow<Category?>

    fun setCategories(categories: Categories?)

    fun getCategoryPath(id: Category.Id): CategoryPath?
}
