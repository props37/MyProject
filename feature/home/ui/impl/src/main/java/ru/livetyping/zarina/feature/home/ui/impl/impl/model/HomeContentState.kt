package ru.livetyping.zarina.feature.home.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState2
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.home.domain.model.HomeContent

@Stable
internal sealed class HomeContentState {
    @Immutable
    data class Success(
        val genderPickerState: TabRowState<GenderTab>,
        val content: HomeContent,
        val isRefreshing: Boolean,
    ) : HomeContentState()

    data object Loading : HomeContentState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState2) : HomeContentState()

    class Builder {
        fun build(
            genderPickerState: TabRowState<GenderTab>,
            result: Result<HomeContent>?,
            isLoading: Boolean,
            isRefreshing: Boolean,
        ): HomeContentState {
            return if (isLoading || result == null) {
                Loading
            } else {
                result.fold(
                    onSuccess = { content ->
                        Success(genderPickerState, content, isRefreshing)
                    },
                    onFailure = { t ->
                        val errorState = ZarinaErrorScreenState2.from(t)
                        Error(errorState)
                    },
                )
            }
        }
    }
}
