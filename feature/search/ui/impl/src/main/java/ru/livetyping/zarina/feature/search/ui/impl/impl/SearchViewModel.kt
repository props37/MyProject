package ru.livetyping.zarina.feature.search.ui.impl.impl

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.combine
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.search.SearchHistoryQuery
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.search.DeleteSearchHistoryQueryUseCase
import ru.livetyping.zarina.core.domain.usecase.search.GetLastSearchHistoryQueriesFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.search.GetSearchSuggestionsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.search.SaveSearchHistoryQueryUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridSideEffect
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchBarEvent
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchBarState
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchEvent
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchMode
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchResultEvent
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchState
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchStateBuilder
import ru.livetyping.zarina.feature.search.ui.impl.impl.model.SearchSuggestionItem
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: SearchDependencies,
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val searchHistoryQueriesResult: SharedFlow<Result<List<SearchHistoryQuery>>> =
        searchBarTextFieldState.textAsFlow()
            .flatMapLatest { query ->
                val params = GetLastSearchHistoryQueriesFlowUseCase.Params(
                    query = query.toString(),
                    limit = LAST_SEARCH_HISTORY_QUERY_COUNT,
                )
                deps.getLastSearchHistoryQueriesFlow(params)
            }
            .conflate()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1,
            )

    private val searchStateBuilder = SearchStateBuilder()

    val searchState: StateFlow<SearchState> = combine(
        searchHistoryQueriesResult,
        searchSuggestionsResult,
        searchBarTextFieldState.textAsFlow(),
    ) { searchHistoryQueriesResult, searchSuggestionResult, query ->
        searchStateBuilder.build(searchHistoryQueriesResult, searchSuggestionResult, query.toString())
    }.stateIn(
        scope = viewModelScopeDefault,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = SearchState(
            autocompleteSuggestions = persistentListOf(),
            suggestionState = SearchState.SuggestionState.Empty,
            query = searchBarTextFieldState.text.toString(),
        ),
    )

    private val wishlistProductIdsParams =
        GetWishlistProductIdsFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())

    private val cartProductIdsParams =
        GetCartProductIdsFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())

    private val _productGridSideEffects = Channel<ProductGridSideEffect>(Channel.UNLIMITED)
    val productGridSideEffects: Flow<ProductGridSideEffect> = _productGridSideEffects.receiveAsFlow()

    private var availableFilters: ProductFilters? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResultPagingDataFlow: Flow<PagingData<ProductShort>> = combine(
        searchQueryValueHolder.stateFlow.filter { it.isNotBlank() },
        flowOf(Unit), // TODO: [Top] Replace with filters flow
    ) { query, _ ->
        // TODO: [Top] Report AppMetrica event
        // val sorting = filters.sorting?.selected ?: Sorting.getDefault()
        deps.searchResultPager.getSearchResultPagingDataFlow(
            query = query,
            sorting = ProductSorting.getDefault(), // TODO: [Top] Implement
            filters = null, // TODO: [Top] Implement
            onAvailableFiltersReceived = { availableFilters = it },
        )
    }
        .flatMapLatest { it }
        .cachedIn(viewModelScopeDefault)
        .onEach { _productGridSideEffects.trySend(ProductGridSideEffect.ScrollToTop) }
        .transformProductPagingData()
        .cachedIn(viewModelScopeDefault)

    fun onSearchBarEvent(event: SearchBarEvent) {
        when (event) {
            SearchBarEvent.SearchClicked -> onSearchClicked()
            SearchBarEvent.Focused -> searchModeValueHolder.set(SearchMode.SEARCH)
            SearchBarEvent.CancelClicked -> onBackClicked()
            SearchBarEvent.BackClicked -> onBackClicked()
            SearchBarEvent.FiltersClicked -> Unit // // TODO: [Top] Implement
        }
    }

    fun onSearchEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.SearchSuggestionItemClicked -> onSearchSuggestionItemClicked(event)
            is SearchEvent.AutocompleteSuggestionClicked -> onAutocompleteSuggestionClicked(event)
            SearchEvent.ClearSearchHistoryClicked -> onClearSearchHistoryClicked()
            is SearchEvent.DeleteSearchHistoryQueryItemClicked -> {
                onDeleteSearchHistoryQueryItemClicked(event)
            }
        }
    }

    // TODO: [Top] Implement
    fun onSearchResultEvent(event: SearchResultEvent) {
        when (event) {
            is SearchResultEvent.ProductClicked -> TODO()
            is SearchResultEvent.AddToWishlistClicked -> TODO()
            is SearchResultEvent.AddToCartClicked -> TODO()
            is SearchResultEvent.SubscribeClicked -> TODO()
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
            saveSearchQuery(query)
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SearchScreenAction.BackClicked
            emitSideEffect(SearchSideEffect.Navigate(action))
        }
    }

    private fun onSearchSuggestionItemClicked(event: SearchEvent.SearchSuggestionItemClicked) {
        when (val item = event.item) {
            is SearchSuggestionItem.QuerySuggestionItem -> onQuerySuggestionClicked(item.query)
            is SearchSuggestionItem.HistoryQueryItem -> onQuerySuggestionClicked(item.query)
            is SearchSuggestionItem.CategoryItem -> {
                navigationThrottler.throttle {
                    val action = SearchScreenAction.CategoryClicked(item.id)
                    emitSideEffect(SearchSideEffect.Navigate(action))
                }
            }

            is SearchSuggestionItem.GenericTitle -> Unit
            SearchSuggestionItem.SearchHistoryTitle -> Unit
        }
    }

    private fun onAutocompleteSuggestionClicked(event: SearchEvent.AutocompleteSuggestionClicked) {
        val resultQuery = event.suggestion.resultQuery
        searchBarTextFieldState.setTextAndPlaceCursorAtEnd("$resultQuery ")
    }

    private fun onClearSearchHistoryClicked() {
        viewModelScope.launch {
            deps.clearSearchHistory()
        }
    }

    private fun onDeleteSearchHistoryQueryItemClicked(
        event: SearchEvent.DeleteSearchHistoryQueryItemClicked,
    ) {
        viewModelScope.launch {
            val params = DeleteSearchHistoryQueryUseCase.Params(event.item.query)
            deps.deleteSearchHistoryQuery(params)
        }
    }

    private fun onQuerySuggestionClicked(query: String) {
        emitSideEffect(SearchSideEffect.ClearSearchBarTextFieldFocus)
        searchModeValueHolder.set(SearchMode.RESULTS)
        searchBarTextFieldState.setTextAndPlaceCursorAtEnd(query)
        searchQueryValueHolder.set(query)
        saveSearchQuery(query)
    }

    private fun saveSearchQuery(query: String) {
        viewModelScope.launch {
            val params = SaveSearchHistoryQueryUseCase.Params(query)
            deps.saveSearchHistoryQuery(params)
        }
    }

    private fun Flow<PagingData<ProductShort>>.transformProductPagingData(): Flow<PagingData<ProductShort>> {
        return this.combine(
            deps.getWishlistProductIdsFlow(wishlistProductIdsParams),
            deps.getCartProductIdsFlow(cartProductIdsParams),
        ) { productPagingData, wishlistProductIdsResult, cartProductIdsResult ->
            val wishlistProductIds = wishlistProductIdsResult.getOrDefault(emptySet())
            val cartProductIds = cartProductIdsResult.getOrDefault(emptySet())
            productPagingData.map { product ->
                product.copy(
                    isInWishlist = product.id in wishlistProductIds,
                    isInCart = product.id in cartProductIds,
                )
            }
        }
    }

    private enum class Keys {
        SEARCH_MODE,
        SEARCH_QUERY;

        val key: String get() = name
    }

    private companion object {
        private const val LAST_SEARCH_HISTORY_QUERY_COUNT = 5
    }
}
