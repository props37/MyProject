package ru.livetyping.zarina.data.category.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.category.CategoryPath
import ru.livetyping.zarina.core.domain.model.category.find
import ru.livetyping.zarina.core.domain.model.gender.Gender
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

    override fun getCategoryPath(id: Category.Id): CategoryPath? {
        val categories = categories.value ?: return null

        var gender = Gender.FEMALE
        var categoryChain = getCategoryChain(id, categories.women)
        if (categoryChain == null) {
            gender = Gender.MALE
            categoryChain = getCategoryChain(id, categories.men)
        }

        return categoryChain?.let { chain -> CategoryPath(gender, chain) }
    }

    private fun getCategoryChain(
        targetCategoryId: Category.Id,
        categories: List<Category>,
    ): List<Category>? {
        for (category in categories) {
            if (category.id == targetCategoryId) return listOf(category)

            val children = category.children
            if (children != null) {
                val nextCategories = getCategoryChain(targetCategoryId, children)
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

    private companion object {
        private const val TAG = "CategoryDataHolderImpl"
    }
}
