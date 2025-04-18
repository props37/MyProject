package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.category.CategoryPath

public interface CategoryRepository {
    public fun getCategoriesFlow(cachePolicy: CachePolicy): Flow<Categories>

    public fun getCategoryFlow(id: Category.Id, cachePolicy: CachePolicy): Flow<Category?>

    public suspend fun getCategoryPath(id: Category.Id, cachePolicy: CachePolicy): CategoryPath?
}
