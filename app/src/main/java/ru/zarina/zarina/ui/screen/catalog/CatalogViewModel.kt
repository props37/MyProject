package ru.zarina.zarina.ui.screen.catalog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<CatalogViewModel.SideEffect> by SideEffectSourceImpl() {

    val searchQuery = savedStateHandle.getStateFlow(
        key = KEY_SEARCH_QUERY,
        initialValue = "",
    )

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[KEY_SEARCH_QUERY] = query
    }

    fun onSearchBarClearClicked() {
        savedStateHandle[KEY_SEARCH_QUERY] = ""
    }

    fun onSearchBarCancelClicked() {
        emitSideEffect(SideEffect.FreeSearchBarFocus)
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data object FreeSearchBarFocus : SideEffect
    }

    companion object {
        private const val KEY_SEARCH_QUERY = "search_query"
    }
}
