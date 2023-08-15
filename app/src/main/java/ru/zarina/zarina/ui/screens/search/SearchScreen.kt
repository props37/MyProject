package ru.zarina.zarina.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.AutocompleteWord
import ru.zarina.zarina.domain.SearchAutocomplete
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.ElevationContainer
import ru.zarina.zarina.ui.common.components.InputSearchBar
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.utils.compose.navigationOrIme

@Composable
fun SearchScreenContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onQueryClearClick: () -> Unit,
    autocomplete: SearchAutocomplete?,
    onAutocompleteWordClick: (AutocompleteWord) -> Unit,
    onFrequentlySearchedClick: (String) -> Unit,
) {
    val contentScrollState = rememberScrollState()
    ZarinaScaffold(
        toolbar = {
            ElevationContainer(isElevated = contentScrollState.canScrollBackward) {
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
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(state = contentScrollState),
        ) {
            if (autocomplete != null) {
                Words(
                    words = autocomplete.words,
                    onWordClick = onAutocompleteWordClick,
                )
                // TODO add categories autocomplete
                FrequentlySearched(
                    queries = autocomplete.frequentQueries,
                    onQueryClick = onFrequentlySearchedClick,
                    modifier = Modifier.padding(WindowInsets.navigationOrIme.asPaddingValues())
                )
                // TODO add recommendations
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Words(
    words: ImmutableList<AutocompleteWord>,
    onWordClick: (AutocompleteWord) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 11.dp, vertical = 10.dp),
    ) {
        for (word in words) {
            Word(
                word = word,
                // TODO move input cursor to end
                onClick = { onWordClick(word) },
            )
        }
    }
}

@Composable
private fun Word(
    word: AutocompleteWord,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(5.dp)
            .background(UiKitTheme.colors.primaryBorderColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(
            text = word.word,
            maxLines = 1,
            style = UiKitTheme.typography.circle1718,
            color = UiKitTheme.colors.primaryContentColor,
        )
    }
}

@Composable
private fun FrequentlySearched(
    queries: ImmutableList<String>,
    onQueryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(id = R.string.frequently_searched),
            style = UiKitTheme.typography.circle1518,
            color = UiKitTheme.colors.primaryContentColor,
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 6.dp),
        )
        for (query in queries) {
            Text(
                text = query,
                style = UiKitTheme.typography.circle1718,
                color = UiKitTheme.colors.primaryContentColor,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onQueryClick(query) })
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            )
        }
    }
}

@Composable
fun SearchScreen() {
    val viewModel = koinViewModel<SearchViewModel>()

    val query by viewModel.query.collectAsStateWithLifecycle()
    val autocomplete by viewModel.autocomplete.collectAsStateWithLifecycle()

    SearchScreenBehavior(
        sideEffects = viewModel.sideEffects,
    )

    SearchScreenContent(
        query = query,
        onQueryChange = remember { { viewModel.onQueryChange(it) } },
        onQueryClearClick = remember { { viewModel.onQueryClearClick() } },
        autocomplete = autocomplete,
        onAutocompleteWordClick = remember { { viewModel.onAutocompleteWordClick(it) } },
        onFrequentlySearchedClick = remember { { viewModel.onFrequentlySearchedClick(it) } },
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
