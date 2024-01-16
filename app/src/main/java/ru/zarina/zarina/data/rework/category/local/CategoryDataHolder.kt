package ru.zarina.zarina.data.rework.category.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.zarina.zarina.domain.rework.common.Categories
import ru.zarina.zarina.domain.rework.common.Category
import ru.zarina.zarina.domain.rework.common.find
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryDataHolder @Inject constructor() {
    private val categories = MutableStateFlow<Categories?>(null)

    fun getCategoriesFlow(): Flow<Categories?> {
        return categories
    }

    fun getCategoryFlow(id: Category.Id): Flow<Category?> {
        return categories.map { categories ->
            categories?.find { it.id == id }
        }.flowOn(Dispatchers.Default)
    }

    fun setCategories(categories: Categories) {
        this.categories.value = categories
        Timber.v("Categories cached")
    }
}
