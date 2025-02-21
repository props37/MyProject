package ru.livetyping.zarina.core.uicomponent.filtration.listfilter.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model.ListFilterEvent
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model.ListFilterState
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model.ListFilterTopBarEvent
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model.ListFilterTopBarState
import ru.livetyping.zarina.core.uicompose.plus
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults

@Composable
public fun ListFilterContent(
    topBarState: ListFilterTopBarState,
    onTopBarEvent: (ListFilterTopBarEvent) -> Unit,
    state: ListFilterState,
    onEvent: (ListFilterEvent) -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    Column(
        modifier = modifier
            .windowInsetsPadding(
                windowInsetsProvider()
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
            ),
    ) {
        ListFilterTopBar(
            state = topBarState,
            onEvent = onTopBarEvent,
        )

        val contentPadding = if (!state.isApplyButtonVisible) {
            windowInsetsProvider().only(WindowInsetsSides.Bottom).asPaddingValues()
        } else {
            PaddingValues()
        }.plus(
            other = PaddingValues(bottom = ZarinaScrollableDefaults.ScrollableBottomPadding),
            layoutDirection = LocalLayoutDirection.current,
        )

        ListFilterItems(
            filter = state.filter,
            onItemClicked = { onEvent(ListFilterEvent.ItemClicked(it)) },
            cityHeader = state.cityHeader,
            contentPadding = contentPadding,
            modifier = Modifier.weight(1f),
        )

        ListFilterApplyButton(
            isVisible = state.isApplyButtonVisible,
            onClick = { onEvent(ListFilterEvent.ApplyClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(
                    windowInsetsProvider().only(WindowInsetsSides.Bottom)
                ),
        )
    }
}
