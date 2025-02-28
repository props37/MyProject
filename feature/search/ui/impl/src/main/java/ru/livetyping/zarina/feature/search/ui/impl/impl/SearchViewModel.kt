package ru.livetyping.zarina.feature.search.ui.impl.impl

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions
import ru.livetyping.zarina.core.domain.usecase.search.GetSearchSuggestionsFlowUseCase
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchBarEvent
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchBarState
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchEvent
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchMode
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchState
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchStateBuilder
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    deps: SearchDependencies,
) : ViewModel(), SideEffectSource<SearchSideEffect> by SideEffectSourceImpl() {

    private val viewModelScopeDefault = viewModelScope + Dispatchers.Default

    private val navigationThrottler = Throttler.getNavigationThrottler()

    @OptIn(SavedStateHandleSaveableApi::class)
    private val searchBarTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    // Separate value used to start search only when needed
    private val searchModeValueHolder = savedStateHandle.createValueHolder(
        key = Keys.SEARCH_MODE.key,
        initialValue = SearchMode.SEARCH,
    )

    val searchMode: StateFlow<SearchMode> = searchModeValueHolder.stateFlow

    val searchBarState: StateFlow<SearchBarState> = combine(
        searchMode,
        flowOf(Unit), // TODO: [Top] Pass filters flow
    ) { searchMode, _ ->
        SearchBarState(
            textFieldState = searchBarTextFieldState,
            searchMode = searchMode,
            appliedFilterCount = 0, // TODO: [Top] Implement
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = SearchBarState(
            textFieldState = searchBarTextFieldState,
            searchMode = searchModeValueHolder.get(),
            appliedFilterCount = 0,
        ),
    )

    private val searchQueryValueHolder = savedStateHandle.createValueHolder(
        key = Keys.SEARCH_QUERY.key,
        initialValue = "",
    )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val searchSuggestionsResult: SharedFlow<Result<SearchSuggestions>> =
        searchBarTextFieldState.textAsFlow()
            .debounce(300.milliseconds)
            .flatMapLatest { query ->
                val params = GetSearchSuggestionsFlowUseCase.Params(query.toString())
                deps.getSearchSuggestionsFlow(params)
            }
            .conflate()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1,
            )

    private val searchStateBuilder = SearchStateBuilder()

    val searchState: StateFlow<SearchState> = combine(
        searchSuggestionsResult,
        searchBarTextFieldState.textAsFlow(),
    ) { searchSuggestionResult, query ->
        searchStateBuilder.build(searchSuggestionResult, query.toString())
    }.stateIn(
        scope = viewModelScopeDefault,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = SearchState(
            autocompleteSuggestions = persistentListOf(),
            suggestionState = SearchState.SuggestionState.Empty,
            query = searchBarTextFieldState.text.toString(),
        ),
    )

    fun onSearchBarEvent(event: SearchBarEvent) {
        when (event) {
            SearchBarEvent.SearchClicked -> onSearchClicked()
            SearchBarEvent.Focused -> searchModeValueHolder.set(SearchMode.SEARCH)
            SearchBarEvent.CancelClicked -> onBackClicked()
            SearchBarEvent.BackClicked -> onBackClicked()
            SearchBarEvent.FiltersClicked -> Unit // // TODO: [Top] Implement
        }
    }

    // TODO: [Top] Implement
    fun onSearchEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.SearchSuggestionItemClicked -> TODO()
            is SearchEvent.AutocompleteSuggestionClicked -> onAutocompleteSuggestionClicked(event)
            SearchEvent.ClearSearchHistoryClicked -> TODO()
            is SearchEvent.DeleteSearchHistoryQueryItemClicked -> TODO()
        }
    }

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> Unit // // TODO: [Top] Implement
            LifecycleEvent.ON_START -> Unit
            LifecycleEvent.ON_RESUME -> Unit
        }
    }

    private fun onSearchClicked() {
        val query = searchBarTextFieldState.text.toString()
        if (query.isNotBlank()) {
            emitSideEffect(SearchSideEffect.ClearSearchBarTextFieldFocus)
            searchModeValueHolder.set(SearchMode.RESULTS)
            searchQueryValueHolder.set(query)
            // TODO: [Top] Save search query
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SearchScreenAction.BackClicked
            emitSideEffect(SearchSideEffect.Navigate(action))
        }
    }

    private fun onAutocompleteSuggestionClicked(event: SearchEvent.AutocompleteSuggestionClicked) {
        val resultQuery = event.suggestion.resultQuery
        searchBarTextFieldState.setTextAndPlaceCursorAtEnd("$resultQuery ")
    }

    private enum class Keys {
        SEARCH_MODE,
        SEARCH_QUERY;

        val key: String get() = name
    }
}
