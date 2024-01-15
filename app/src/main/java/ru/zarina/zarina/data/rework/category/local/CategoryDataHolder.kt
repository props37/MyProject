package ru.zarina.zarina.data.rework.category.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.zarina.zarina.domain.rework.common.Categories
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryDataHolder @Inject constructor() {
    private val categories = MutableStateFlow<Categories?>(null)

    fun getCategoriesFlow(): Flow<Categories?> {
        return categories
    }

    fun setCategories(categories: Categories) {
        this.categories.value = categories
    }
}
