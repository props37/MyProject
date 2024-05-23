package ru.livetyping.zarina.presentation.screen.productsearch

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.usecase.productsearch.GetProductSearchSuggestionsFlowUseCase
import ru.livetyping.zarina.util.compose.text.textAsFlow
import ru.livetyping.zarina.util.kotlin.capitalize
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
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

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val searchSuggestionsResult: StateFlow<Result<ProductSearchSuggestions>?> =
        searchTextFieldState.textAsFlow()
            .debounce(300.milliseconds)
            .flatMapLatest { query ->
                val params = GetProductSearchSuggestionsFlowUseCase.Params(query.toString())
                interactor.getProductSearchSuggestionsFlow(params)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    val searchSuggestionItems: StateFlow<ImmutableList<SearchSuggestionItem>> =
        searchSuggestionsResult.mapState(
            scope = viewModelScope + Dispatchers.Default,
            started = SharingStarted.WhileUiSubscribed,
        ) { result ->
            result?.toSearchSuggestionItems()?.toImmutableList() ?: persistentListOf()
        }

    private val categoryParentCategoryChainRegex = CATEGORY_PARENT_CATEGORY_CHAIN_PATTERN.toRegex()

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

    fun onSearchSuggestionItemClicked(item: SearchSuggestionItem) {
        // TODO: [High] Implement
    }

    private fun Result<ProductSearchSuggestions>.toSearchSuggestionItems(): List<SearchSuggestionItem> {
        return this.fold(
            onSuccess = { suggestions ->
                buildList {
                    if (suggestions.resultSuggestions.isNotEmpty()) {
                        val titleText = Text.Resource(R.string.search_results)
                        add(SearchSuggestionItem.GenericTitle(titleText))

                        val items = suggestions.resultSuggestions
                            .take(SEARCH_SUGGESTIONS_QUERIES_MAX_COUNT)
                            .map { query ->
                                SearchSuggestionItem.QueryItem(query.capitalize())
                            }
                        addAll(items)
                    }

                    if (suggestions.categories.isNotEmpty()) {
                        val titleText = Text.Resource(R.string.categories)
                        add(SearchSuggestionItem.GenericTitle(titleText))

                        val items = suggestions.categories
                            .take(SEARCH_SUGGESTIONS_CATEGORIES_MAX_COUNT)
                            .map { it.toCategoryItem() }
                        addAll(items)
                    }
                }.toImmutableList()
            },
            onFailure = { persistentListOf() },
        )
    }

    private fun ProductSearchSuggestions.Category.toCategoryItem(): SearchSuggestionItem.CategoryItem {
        var name = this.name
        var parentCategoryChain = categoryParentCategoryChainRegex.find(name)?.value

        if (parentCategoryChain != null) {
            name = name.removeSuffix(parentCategoryChain).trim()
            parentCategoryChain = parentCategoryChain
                .removeSurrounding(BRACKET_START, BRACKET_END)
                .split(CATEGORY_PARENT_CATEGORY_CHAIN_SEPARATOR)
                .joinToString(separator = CATEGORY_PARENT_CATEGORY_CHAIN_SEPARATOR) {
                    it.capitalize()
                }
        }

        return SearchSuggestionItem.CategoryItem(
            id = this.id,
            name = name.capitalize(),
            parentCategoryChain = parentCategoryChain,
        )
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProductSearchScreenAction) : SideEffect

        data object ReleaseSearchTextFieldFocus : SideEffect
    }

    enum class SearchMode { SEARCH, SEARCH_RESULTS }

    @Stable
    sealed class SearchSuggestionItem {
        @Immutable
        data class GenericTitle(val text: Text) : SearchSuggestionItem()

        @Immutable
        data class QueryItem(val query: String) : SearchSuggestionItem()

        @Immutable
        data class CategoryItem(
            val id: Category.Id,
            val name: String,
            val parentCategoryChain: String?,
        ) : SearchSuggestionItem()
    }

    companion object {
        private const val SEARCH_SUGGESTIONS_QUERIES_MAX_COUNT = 5
        private const val SEARCH_SUGGESTIONS_CATEGORIES_MAX_COUNT = 5

        private const val CATEGORY_PARENT_CATEGORY_CHAIN_PATTERN = "\\(.+\\)"
        private const val CATEGORY_PARENT_CATEGORY_CHAIN_SEPARATOR = " - "

        private const val BRACKET_START = "("
        private const val BRACKET_END = ")"
    }
}
