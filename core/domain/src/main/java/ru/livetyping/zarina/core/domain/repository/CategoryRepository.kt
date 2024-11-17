package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.category.Categories

public interface CategoryRepository {
    public fun getCategoriesFlow(cachePolicy: CachePolicy): Flow<Categories>
}
