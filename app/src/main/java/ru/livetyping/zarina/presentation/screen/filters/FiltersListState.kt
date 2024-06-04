package ru.livetyping.zarina.presentation.screen.filters

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.presentation.common.error.ErrorState

@Stable
sealed class FilterListState {
    data object Loading : FilterListState()

    @Immutable
    data class FilterList(val filters: Filters) : FilterListState()

    @Immutable
    data class Error(val errorState: ErrorState) : FilterListState()
}
