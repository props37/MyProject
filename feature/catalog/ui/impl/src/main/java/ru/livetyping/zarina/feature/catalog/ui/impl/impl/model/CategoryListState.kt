package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class CategoryListState {
    @Immutable
    data class Success(
        val womenItems: ImmutableList<CategoryListItem>,
        val menItems: ImmutableList<CategoryListItem>,
    ) : CategoryListState()

    data object Loading : CategoryListState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : CategoryListState()
}
