package ru.livetyping.zarina.feature.search.ui.impl.impl

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.search.ui.api.SearchFeature
import ru.livetyping.zarina.feature.search.ui.impl.impl.component.SearchBar
import ru.livetyping.zarina.feature.search.ui.impl.impl.component.SearchContent
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchBarEvent
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchBarState
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchEvent
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchMode
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchState

@Composable
internal fun SearchScreen(
    navActions: SearchFeature.NavActions,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val searchBarState by viewModel.searchBarState.collectAsStateWithLifecycle()
    val searchMode by viewModel.searchMode.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()

    ScreenContent(
        searchBarState = searchBarState,
        onSearchBarEvent = viewModel::onSearchBarEvent,
        searchMode = searchMode,
        searchState = searchState,
        onSearchEvent = viewModel::onSearchEvent,
        onLifecycleEvent = viewModel::onLifecycleEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    searchBarState: SearchBarState,
    onSearchBarEvent: (SearchBarEvent) -> Unit,
    searchMode: SearchMode,
    searchState: SearchState,
    onSearchEvent: (SearchEvent) -> Unit,
    onLifecycleEvent: (LifecycleEvent) -> Unit,
    sideEffects: Flow<SearchSideEffect>,
    navActions: SearchFeature.NavActions,
) {
    SearchScreenBehavior(
        onLifecycleEvent = onLifecycleEvent,
        sideEffects = sideEffects,
        navActions = navActions,
    )

    val backgroundColor = UiKitTheme.colors.background.general.regular.default

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        val searchBarFocusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            if (searchBarState.searchMode == SearchMode.SEARCH) {
                withFrameMillis {}
                searchBarFocusRequester.tryRequestFocus()
            }
        }

        SearchBar(
            state = searchBarState,
            onEvent = onSearchBarEvent,
            focusRequester = searchBarFocusRequester,
        )

        Crossfade(
            targetState = searchMode,
            modifier = Modifier.fillMaxSize(),
        ) { mode ->
            when (mode) {
                SearchMode.SEARCH -> {
                    SearchContent(
                        state = searchState,
                        onEvent = onSearchEvent,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(backgroundColor),
                    )
                }

                SearchMode.RESULTS -> {
                    // TODO: [Top] Implement
                }
            }
        }
    }
}
