package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.component

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoryFlowUseCase

internal class CategoryComponent(
    private val getCategoryFlowUseCase: GetCategoryFlowUseCase,
) {
    private var fetchCategoryJob: Job? = null

    private var initialCategoryId: Category.Id? = null

    private val _categoryResult = MutableStateFlow<Result<Category>?>(null)
    val categoryResult = _categoryResult.asStateFlow()

    private val _selectedSubcategoryId = MutableStateFlow<Category.Id?>(null)
    val selectedSubcategoryId = _selectedSubcategoryId.asStateFlow()

    val currentCategoryId = selectedSubcategoryId.map {
        it ?: requireInitialCategoryId()
    }

    fun requireInitialCategoryId(): Category.Id {
        val categoryId = initialCategoryId
        checkNotNull(categoryId) { "categoryId is null" }
        return categoryId
    }

    fun setInitialCategoryId(id: Category.Id) {
        initialCategoryId = id
    }

    fun getCurrentCategoryId(): Category.Id {
        return selectedSubcategoryId.value ?: requireInitialCategoryId()
    }

    suspend fun fetchCategory() {
        fetchCategoryJob?.cancel()

        coroutineScope {
            fetchCategoryJob = launch {
                val params = GetCategoryFlowUseCase.Params(
                    id = requireInitialCategoryId(),
                    cachePolicy = CachePolicy.LocalFirstThenRemote(),
                )
                // TODO: [Top] Implement
            }
        }
    }

    fun isCategoryFetched(): Boolean {
        return categoryResult.value?.isSuccess == true
    }

    fun setSelectedSubcategoryId(id: Category.Id?) {
        _selectedSubcategoryId.value = id
    }
}
