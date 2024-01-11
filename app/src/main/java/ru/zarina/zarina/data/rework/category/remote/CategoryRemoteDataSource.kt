package ru.zarina.zarina.data.rework.category.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.rework.category.remote.api.CategoryApi
import ru.zarina.zarina.domain.rework.common.Category
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val api: CategoryApi,
) {
    fun getCategories(): Flow<List<Category>> = flow {
        val categories = api.getCategories().map { it.toCategory() }
        emit(categories)
    }
}
