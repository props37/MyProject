package ru.livetyping.zarina.data.category.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Categories

internal interface CategoryRemoteDataSource {
    fun getCategoriesFlow(): Flow<Categories>
}
