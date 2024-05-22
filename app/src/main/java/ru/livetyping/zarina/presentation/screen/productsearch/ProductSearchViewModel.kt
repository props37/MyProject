package ru.livetyping.zarina.presentation.screen.productsearch

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.usecase.productsearch.GetProductSearchSuggestionsFlowUseCase
import ru.livetyping.zarina.util.compose.text.textAsFlow
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

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

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchSuggestions: StateFlow<ProductSearchSuggestions?> = searchTextFieldState.textAsFlow()
        .debounce(500.milliseconds)
        .flatMapLatest { query ->
            val params = GetProductSearchSuggestionsFlowUseCase.Params(query.toString())
            interactor.getProductSearchSuggestionsFlow(params)
                .map { it.getOrNull() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

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
