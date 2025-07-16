package ru.livetyping.zarina.feature.home.ui.impl.screen.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen2
import ru.livetyping.zarina.core.uikit.screen.ZarinaLogoLoadingScreen
import ru.livetyping.zarina.feature.home.ui.impl.screen.model.HomeContentState
import ru.livetyping.zarina.feature.home.ui.impl.screen.model.HomeEvent
import ru.livetyping.zarina.feature.home.ui.impl.screen.model.HomeState

@Composable
internal fun HomeContent(
    homeState: HomeState,
    onHomeEvent: (HomeEvent) -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    bottomPaddingProvider: @Composable () -> Dp,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = homeState.contentState,
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
                HomeContentSuccess(
                    state = state,
                    onHomeEvent = onHomeEvent,
                    windowInsetsProvider = windowInsetsProvider,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            HomeContentState.Loading -> {
                ZarinaLogoLoadingScreen(modifier = Modifier.fillMaxSize())
            }

            is HomeContentState.Error -> {
                ZarinaErrorScreen2(
                    state = state.state,
                    onButtonClick = { onHomeEvent(HomeEvent.RefreshClicked) },
                    bottomPaddingProvider = bottomPaddingProvider,
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(
                            windowInsetsProvider()
                                .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                        ),
                )
            }
        }
    }
}

private enum class HomeContentKey { Success }
