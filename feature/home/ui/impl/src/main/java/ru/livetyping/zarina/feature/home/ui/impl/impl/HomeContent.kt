package ru.livetyping.zarina.feature.home.ui.impl.impl

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.ui.compose.Crossfade
import ru.livetyping.zarina.core.ui.kit.error.ZarinaErrorScreen
import ru.livetyping.zarina.feature.home.ui.impl.impl.homecontent.HomeContentEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.homecontent.HomeContentState

@Composable
internal fun HomeContent(
    homeContentState: HomeContentState,
    onHomeContentEvent: (HomeContentEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = homeContentState,
        contentKey = { state ->
            when (state) {
                is HomeContentState.Success -> HomeContentKey.Success
                is HomeContentState.Error -> state
                HomeContentState.Loading -> state
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is HomeContentState.Success -> {
                // TODO: [Top] Implement
            }

            HomeContentState.Loading -> {
                // TODO: [Top] Implement
            }

            is HomeContentState.Error -> {
                ZarinaErrorScreen(
                    state = state.state,
                    onButtonClicked = {
                        onHomeContentEvent(HomeContentEvent.ErrorRefreshClicked)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(
                            WindowInsets.statusBars
                                .union(WindowInsets.displayCutout)
                        )
                        // TODO: [Top] Add bottom nav bar padding
                        .padding(16.dp),
                )
            }
        }
    }
}

private enum class HomeContentKey { Success }
