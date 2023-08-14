package ru.zarina.zarina.ui.screens.search

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.InputSearchBar
import ru.zarina.zarina.ui.common.components.ZarinaScaffold

@Composable
fun SearchScreenContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onQueryClearClick: () -> Unit,
) {
    ZarinaScaffold(
        toolbar = {
            // TODO add elevation
            val focusRequester = remember { FocusRequester() }
            InputSearchBar(
                value = query,
                onValueChange = onQueryChange,
                onClearClick = onQueryClearClick,
                modifier = Modifier
                    .statusBarsPadding()
                    .focusRequester(focusRequester)
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    ) {

    }
}

@Composable
fun SearchScreen() {
    val viewModel = koinViewModel<SearchViewModel>()

    val query by viewModel.query.collectAsStateWithLifecycle()

    SearchScreenBehavior(
        sideEffects = viewModel.sideEffects,
    )

    SearchScreenContent(
        query = query,
        onQueryChange = remember { { viewModel.onQueryChange(it) } },
        onQueryClearClick = remember { { viewModel.onQueryClearClick() } }
    )
}

@Composable
fun SearchScreenBehavior(
    sideEffects: Flow<SearchViewModel.SideEffect>,
) {
    NavigationBarState(isVisible = false, isAnimated = false)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
            }
        }
    }
}
