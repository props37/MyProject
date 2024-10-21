package ru.livetyping.zarina.data.category.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.data.category.impl.remote.api.CategoryApi
import javax.inject.Inject

internal class CategoryRemoteDataSourceImpl @Inject constructor(
    private val api: CategoryApi,
) : CategoryRemoteDataSource {
    override fun getCategoriesFlow(): Flow<Categories> = flow {
        val categories = api.getCategories().toCategories()
        emit(categories)
    }
}
