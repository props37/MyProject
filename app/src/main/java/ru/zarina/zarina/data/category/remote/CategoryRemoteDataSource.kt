package ru.zarina.zarina.data.category.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.category.remote.api.CategoryApi
import ru.zarina.zarina.domain.rework.category.Categories
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val api: CategoryApi,
) {
    fun getCategoriesFlow(): Flow<Categories> = flow {
        val categories = api.getCategories().toCategories()
        emit(categories)
    }
}
