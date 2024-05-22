package ru.livetyping.zarina.presentation.screen.productsearch

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class ProductSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ProductSearchInteractor,
) : ViewModel(), SideEffectSource<ProductSearchViewModel.SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val searchTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val _searchMode = MutableStateFlow(SearchMode.SEARCH)
    val searchMode: StateFlow<SearchMode> = _searchMode.asStateFlow()

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductSearchScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onSearchTextFieldCancelClicked() {
        navigationThrottler.throttle {
            val action = ProductSearchScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onSearchTextFieldSearchClicked() {
        emitSideEffect(SideEffect.ReleaseSearchTextFieldFocus)
        _searchMode.value = SearchMode.SEARCH_RESULTS
    }

    fun onSearchTextFieldFocused() {
        _searchMode.value = SearchMode.SEARCH
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProductSearchScreenAction) : SideEffect

        data object ReleaseSearchTextFieldFocus : SideEffect
    }

    enum class SearchMode { SEARCH, SEARCH_RESULTS }
}
