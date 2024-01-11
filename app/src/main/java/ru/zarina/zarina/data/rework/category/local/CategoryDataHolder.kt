package ru.zarina.zarina.data.rework.category.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.zarina.zarina.domain.rework.common.Category
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryDataHolder @Inject constructor() {
    private val categories = MutableStateFlow<List<Category>?>(null)

    fun getCategories(): Flow<List<Category>?> {
        return categories
    }

    fun setCategories(categories: List<Category>) {
        this.categories.value = categories
    }
}
