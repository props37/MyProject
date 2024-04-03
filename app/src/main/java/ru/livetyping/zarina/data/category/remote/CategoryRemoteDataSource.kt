package ru.livetyping.zarina.data.category.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.category.remote.api.CategoryApi
import ru.livetyping.zarina.domain.category.Categories
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val api: CategoryApi,
) {
    fun getCategoriesFlow(): Flow<Categories> = flow {
        val categories = api.getCategories().toCategories()
        emit(categories)
    }
}
