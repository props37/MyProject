package ru.livetyping.zarina.feature.search.ui.impl.impl.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.tag.ZarinaTag
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchEvent
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchState

@Composable
internal fun SearchContent(
    state: SearchState,
    onEvent: (SearchEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        AutocompleteSuggestions(
            suggestions = state.autocompleteSuggestions,
            onSuggestionClicked = { onEvent(SearchEvent.AutocompleteSuggestionClicked(it)) },
            modifier = Modifier.fillMaxWidth(),
        )

        Crossfade(
            targetState = state.suggestionState,
            contentKey = {
                when (it) {
                    is SearchState.SuggestionState.Success -> SearchContentKey.Success
                    SearchState.SuggestionState.Empty -> it
                    is SearchState.SuggestionState.Error -> it
                }
            },
            modifier = Modifier.fillMaxSize(),
        ) { state ->
            when (state) {
                is SearchState.SuggestionState.Success -> {
                    // TODO: [Top] Implement
                }

                is SearchState.SuggestionState.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = {}, // Refresh button is hidden here
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }

                SearchState.SuggestionState.Empty -> Unit
            }
        }
    }
}

@Composable
private fun AutocompleteSuggestions(
    suggestions: ImmutableList<SearchSuggestions.AutocompleteSuggestion>,
    onSuggestionClicked: (SearchSuggestions.AutocompleteSuggestion) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    DisposableEffect(suggestions) {
        listState.requestScrollToItem(0)
        onDispose {}
    }

    @Suppress("NAME_SHADOWING")
    AnimatedContent(
        targetState = suggestions,
        transitionSpec = {
            val enter = expandVertically() + fadeIn()
            val exit = shrinkVertically() + fadeOut()
            (enter togetherWith exit).using(SizeTransform(clip = false))
        },
        contentAlignment = Alignment.TopCenter,
        contentKey = { it.isNotEmpty() },
        label = "AutocompleteSuggestions",
        modifier = modifier,
    ) { suggestions ->
        if (suggestions.isNotEmpty()) {
            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                items(
                    items = suggestions,
                    key = { it.text },
                ) { suggestion ->
                    ZarinaTag(
                        onClick = { onSuggestionClicked(suggestion) },
                        modifier = Modifier.animateZarinaItem(this),
                    ) {
                        Text(text = suggestion.text.capitalize(Locale.current))
                    }
                }
            }
        }
    }
}

private enum class SearchContentKey { Success }
