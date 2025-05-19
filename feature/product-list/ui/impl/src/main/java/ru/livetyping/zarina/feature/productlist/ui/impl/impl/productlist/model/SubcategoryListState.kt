package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.category.Category

@Stable
internal sealed class SubcategoryListState {
    @Immutable
    data class Success(
        val tags: ImmutableList<Category>,
        val selectedTagId: Category.Id?,
    ) : SubcategoryListState()

    data object Empty : SubcategoryListState()

    data object Loading : SubcategoryListState()
}
