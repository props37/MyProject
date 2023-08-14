package ru.zarina.zarina.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.AutocompleteWord
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.utils.coroutine.mapState

@KoinViewModel
class SearchViewModel(
    private val interactor: SearchInteractor,
) : ViewModel(),
    ISideEffectSource<SearchViewModel.SideEffect> by SideEffectQueue() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _autocompleteResult = _query
        .flatMapLatest { query ->
            flowOf(interactor.getAutocomplete(query))
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val autocomplete = _autocompleteResult
        .mapState(viewModelScope) { it?.getOrNull() }

    fun onQueryChange(query: String) {
        _query.value = query
    }

    fun onQueryClearClick() {
        _query.value = ""
    }

    fun onAutocompleteWordClick(word: AutocompleteWord) {
        _query.value = word.query
    }

    fun onSearchClick() {
        // TODO
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
