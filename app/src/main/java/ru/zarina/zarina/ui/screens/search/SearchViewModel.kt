package ru.zarina.zarina.ui.screens.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue

@KoinViewModel
class SearchViewModel(
    private val interactor: SearchInteractor,
) : ViewModel(),
    ISideEffectSource<SearchViewModel.SideEffect> by SideEffectQueue() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    fun onQueryChange(query: String) {
        _query.value = query
    }

    fun onQueryClearClick() {
        _query.value = ""
    }

    fun onSearchClick() {
        // TODO
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}
