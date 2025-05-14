package ru.livetyping.zarina.feature.home.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.home.domain.model.HomeContent

@Immutable
internal data class HomeState(
    val contentState: HomeContentState,
) {
    class Builder {
        fun build(
            genderPickerState: TabRowState<GenderTab>,
            homeContentResult: Result<HomeContent>?,
            isContentLoading: Boolean,
            isContentRefreshing: Boolean,
        ): HomeState {
            val contentState = HomeContentState.Builder().build(
                genderPickerState = genderPickerState,
                result = homeContentResult,
                isLoading = isContentLoading,
                isRefreshing = isContentRefreshing,
            )
            return HomeState(contentState)
        }
    }
}
