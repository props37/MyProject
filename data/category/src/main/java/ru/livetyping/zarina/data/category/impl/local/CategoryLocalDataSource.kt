package ru.livetyping.zarina.data.category.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Categories

internal interface CategoryLocalDataSource {
    fun getCategoriesFlow(): Flow<Categories?>

    fun setCategories(categories: Categories)
}
