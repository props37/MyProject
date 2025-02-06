package ru.livetyping.zarina.data.category.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.domain.category.Categories
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.category.CategoryPath
import ru.livetyping.zarina.domain.category.find
import ru.livetyping.zarina.domain.common.Gender
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
        Timber.v("Set categories: $categories")
        this.categories.value = categories
    }

    fun getCategoryPath(categoryId: Category.Id): CategoryPath? {
        val categories = categories.value ?: return null

        var gender = Gender.FEMALE
        var categoryChain = getCategoryChain(categoryId, categories.women)
        if (categoryChain == null) {
            gender = Gender.MALE
            categoryChain = getCategoryChain(categoryId, categories.men)
        }

        return categoryChain?.let { chain -> CategoryPath(gender, chain) }
    }

    private fun getCategoryChain(
        targetCategoryId: Category.Id,
        categories: List<Category>,
    ): List<Category>? {
        for (category in categories) {
            if (category.id == targetCategoryId) return listOf(category)

            if (category.children != null) {
                val nextCategories = getCategoryChain(targetCategoryId, category.children)
                if (nextCategories != null) {
                    return buildList {
                        add(category)
                        addAll(nextCategories)
                    }
                }
            }
        }

        return null
    }
}
