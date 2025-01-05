package ru.livetyping.zarina.data.category.impl.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.category.find
import timber.log.Timber
import javax.inject.Inject

internal class CategoryDataHolderImpl @Inject constructor() : CategoryDataHolder {
    private val categories = MutableStateFlow<Categories?>(null)

    override fun getCategoriesFlow(): Flow<Categories?> {
        return categories
    }

    override fun getCategoryFlow(id: Category.Id): Flow<Category?> {
        // TODO: [Medium] Inject dispatcher
        return categories.map { categories ->
            categories?.find { it.id == id }
        }.flowOn(Dispatchers.Default)
    }

    override fun setCategories(categories: Categories?) {
        this.categories.value = categories
        Timber.tag(TAG).v("Categories set: $categories")
    }

    private companion object {
        private const val TAG = "CategoryDataHolderImpl"
    }
}
