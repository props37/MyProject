package ru.livetyping.zarina.data.category.impl.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.livetyping.zarina.core.domain.model.category.Categories
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CategoryDataHolderImpl @Inject constructor() : CategoryDataHolder {
    private val categories = MutableStateFlow<Categories?>(null)

    override fun getCategoriesFlow(): Flow<Categories?> {
        return categories
    }

    override fun setCategories(categories: Categories?) {
        Timber.tag(TAG).v("Set categories: $categories")
        this.categories.value = categories
    }

    private companion object {
        private const val TAG = "CategoryDataHolderImpl"
    }
}
