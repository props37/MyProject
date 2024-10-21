package ru.livetyping.zarina.feature.catalog.ui.impl.impl.category

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableSet
import ru.livetyping.zarina.core.domain.model.category.Category

@Immutable
internal data class CategoryListItemsState(
    val visibleCategoryIds: ImmutableSet<Category.Id>,
    val expandedCategoryIds: ImmutableSet<Category.Id>,
)
