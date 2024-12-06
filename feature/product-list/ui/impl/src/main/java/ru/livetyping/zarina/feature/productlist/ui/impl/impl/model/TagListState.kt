package ru.livetyping.zarina.feature.productlist.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.category.Category

@Stable
internal sealed class TagListState {
    @Immutable
    data class Success(
        val tags: ImmutableList<Category>,
        val selectedTagId: Category.Id?,
    ) : TagListState()

    data object Empty : TagListState()

    data object Loading : TagListState()
}
