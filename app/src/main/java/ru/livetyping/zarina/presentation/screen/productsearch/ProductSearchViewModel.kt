package ru.livetyping.zarina.presentation.screen.productsearch

import android.os.Parcelable
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.data.analytics.AppMetricaHelper
import ru.livetyping.zarina.data.analytics.AppMetricaScreen
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.filter.coerceInAvailable
import ru.livetyping.zarina.domain.filter.selected
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.domain.productsearch.ProductSearchHistoryQuery
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.presentation.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.util.library.paging.mapProducts
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.model.filter.FiltersParcelable
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.SizeSelectorGraph
import ru.livetyping.zarina.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.productsearch.DeleteProductSearchHistoryQueryUseCase
import ru.livetyping.zarina.usecase.productsearch.GetLastProductSearchHistoryQueriesFlowUseCase
import ru.livetyping.zarina.usecase.productsearch.GetProductSearchSuggestionsFlowUseCase
import ru.livetyping.zarina.usecase.productsearch.SaveProductSearchHistoryQueryUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.compose.text.clear
import ru.livetyping.zarina.util.compose.text.textAsFlow
import ru.livetyping.zarina.util.kotlin.capitalize
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel(assistedFactory = ProductSearchViewModel.Factory::class)
class ProductSearchViewModel @AssistedInject constructor(
    @Assisted
    private val sizeSelectorResultFlow: StateFlow<SizeSelectorGraph.Result?>,
    @Assisted
    private val filtersResultFlow: StateFlow<UnscopedDestinations.ProductSearchFilters.Result?>,
    savedStateHandle: SavedStateHandle,
    private val interactor: ProductSearchInteractor,
) : ViewModel(), SideEffectSource<ProductSearchViewModel.SideEffect> by SideEffectSourceImpl() {

    private val viewModelScopeDefault = viewModelScope + Dispatchers.Default

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val searchModeValueHolder = savedStateHandle.createValueHolder(
        key = KEY_SEARCH_MODE,
        initialValue = SearchMode.SEARCH,
    )

    private val searchQueryValueHolder = savedStateHandle.createValueHolder(
        key = KEY_SEARCH_QUERY,
        initialValue = "",
    )

    val searchTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    val searchMode: StateFlow<SearchMode> = searchModeValueHolder.stateFlow

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val searchSuggestionsResult: StateFlow<Result<ProductSearchSuggestions>?> =
        searchTextFieldState.textAsFlow()
            .debounce(300.milliseconds)
            .flatMapLatest { query ->
                val params = GetProductSearchSuggestionsFlowUseCase.Params(query.toString())
                interactor.getProductSearchSuggestionsFlow(params)
            }
            .stateIn(
                scope = viewModelScopeDefault,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    val searchAutocompleteSuggestions: StateFlow<ImmutableList<ProductSearchSuggestions.AutocompleteSuggestion>> =
        searchSuggestionsResult.mapState(
            scope = viewModelScopeDefault,
            started = SharingStarted.WhileUiSubscribed,
        ) { result ->
            result?.fold(
                onSuccess = { suggestions ->
                    suggestions.autocompleteSuggestions.toImmutableList()
                },
                onFailure = { persistentListOf() },
            ) ?: persistentListOf()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val lastSearchHistoryQueriesResult: StateFlow<Result<List<ProductSearchHistoryQuery>>?> =
        searchTextFieldState.textAsFlow()
            .flatMapLatest { query ->
                val params = GetLastProductSearchHistoryQueriesFlowUseCase.Params(
                    text = query.toString(),
                    limit = SEARCH_HISTORY_QUERIES_MAX_COUNT,
                )
                interactor.getLastProductSearchHistoryQueriesFlow(params)
            }
            .stateIn(
                scope = viewModelScopeDefault,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    val searchSuggestionsState: StateFlow<SearchSuggestionsState> = combine(
        lastSearchHistoryQueriesResult,
        searchSuggestionsResult,
    ) { lastSearchHistoryEntriesResult, searchSuggestionsResult ->
        createSearchSuggestionsState(lastSearchHistoryEntriesResult, searchSuggestionsResult)
    }.stateIn(
        scope = viewModelScopeDefault,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = SearchSuggestionsState.Loading,
    )

    private val filtersValueHolder = savedStateHandle.createValueHolder<FiltersParcelable?>(
        key = KEY_FILTERS,
        initialValue = null,
    )

    private val filters: StateFlow<Filters> = filtersValueHolder.stateFlow.mapState(
        scope = viewModelScopeDefault,
        started = SharingStarted.Eagerly,
    ) {
        it?.toFilters() ?: Filters.create(
            sorting = Filters.getDefaultSorting(Sorting.getDefault()),
        )
    }

    private var availableFilters: Filters? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val productSearchResultPagingDataFlow: Flow<PagingData<ProductItem>> = combine(
        searchQueryValueHolder.stateFlow.filter { it.isNotBlank() },
        filters,
    ) { searchQuery, filters ->
        AppMetricaHelper.reportProductSearch(searchQuery)
        val sorting = filters.sorting?.selected ?: Sorting.getDefault()
        interactor.productSearchResultPager.getProductPagingDataFlow(
            query = searchQuery,
            sorting = sorting,
            filters = filters,
            onAvailableFiltersReceived = { availableFilters = it },
        )
    }
        .flatMapLatest { it }
        .cachedIn(viewModelScopeDefault)
        .mapProducts(
            favoriteProductIdsResultFlow = interactor.getFavoriteProductIdsFlow(),
            cartProductIdsResultFlow = interactor.getCardProductsIdsFlow(),
        )
        .cachedIn(viewModelScopeDefault)

    val appliedFilterCount: StateFlow<Int> = filters.mapState(
        scope = viewModelScopeDefault,
        started = SharingStarted.WhileUiSubscribed,
    ) { it.appliedFilterCount }

    private val categoryParentCategoryChainRegex = CATEGORY_PARENT_CATEGORY_CHAIN_PATTERN.toRegex()

    init {
        handleSizeSelectorResult()
        handleFiltersResult()
    }

    fun onScreenCreated() {
        AppMetricaHelper.reportScreenOpened(AppMetricaScreen.Search)
    }

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
        val searchQuery = searchTextFieldState.text.toString()
        if (searchQuery.isNotBlank()) {
            emitSideEffect(SideEffect.ReleaseSearchTextFieldFocus)
            searchModeValueHolder.set(SearchMode.SEARCH_RESULTS)
            searchQueryValueHolder.set(searchQuery)
            saveSearchQuery(searchQuery)
        }
    }

    fun onSearchTextFieldFocused() {
        searchModeValueHolder.set(SearchMode.SEARCH)
    }

    fun onSearchAutocompleteSuggestionClicked(suggestion: ProductSearchSuggestions.AutocompleteSuggestion) {
        searchTextFieldState.edit {
            clear()
            append("${suggestion.resultQuery} ")
            placeCursorAtEnd()
        }
    }

    fun onSearchSuggestionItemClicked(item: SearchSuggestionItem) {
        when (item) {
            is SearchSuggestionItem.SearchQueryItem -> {
                onSearchSuggestionQueryItemClicked(item.query)
            }

            is SearchSuggestionItem.HistoryQueryItem -> {
                onSearchSuggestionQueryItemClicked(item.query)
            }

            is SearchSuggestionItem.CategoryItem -> {
                navigationThrottler.throttle {
                    val action = ProductSearchScreenAction.CategoryClicked(item.id)
                    emitSideEffect(SideEffect.Navigate(action))
                }
            }

            is SearchSuggestionItem.GenericTitle -> Unit
            SearchSuggestionItem.SearchHistoryTitle -> Unit
        }
    }

    fun onClearProductSearchHistoryClicked() {
        viewModelScope.launch {
            interactor.clearProductSearchHistory()
        }
    }

    fun onDeleteSearchHistoryQueryItemClicked(item: SearchSuggestionItem.HistoryQueryItem) {
        viewModelScope.launch {
            val params = DeleteProductSearchHistoryQueryUseCase.Params(item.query)
            interactor.deleteProductSearchHistoryQuery(params)
        }
    }

    fun onFiltersClicked() {
        navigationThrottler.throttle {
            val availableFilters = availableFilters
            val combinedFilters =
                availableFilters?.let { filters.value.coerceInAvailable(it) } ?: filters.value
            val action = ProductSearchScreenAction.FiltersClicked(
                searchQuery = searchQueryValueHolder.stateFlow.value,
                filters = combinedFilters,
            )
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onProductClicked(product: Product) {
        navigationThrottler.throttle {
            val action = ProductSearchScreenAction.ProductClicked(product)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onAddProductToFavoritesClicked(product: Product) {
        viewModelScopeDefault.launch {
            val params = ToggleProductPresenceInFavoritesUseCase.Params(product.id)
            interactor.toggleProductPresenceInFavorites(params)
                .onSuccess { isInFavorites ->
                    if (isInFavorites) {
                        val text = Text.Resource(R.string.product_adding_to_favorites_completed)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(SideEffect.ShowZarinaToast(message))
                        AppMetricaHelper.reportProductAddedToWishlist(product)
                    } else {
                        AppMetricaHelper.reportProductRemovedFromWishlist(product)
                    }
                }
                .onFailure {
                    val messageResId = if (product.isInFavorites) {
                        R.string.product_removing_from_favorites_error
                    } else {
                        R.string.product_adding_to_favorites_error
                    }
                    val message = ZarinaToastMessage.error(Text.Resource(messageResId))
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    fun onAddProductToCartClicked(product: Product) {
        if (product.offers.size > 1) {
            navigationThrottler.throttle {
                val action = ProductSearchScreenAction.AddProductToCartClicked(product)
                emitSideEffect(SideEffect.Navigate(action))
            }
        } else {
            val offer = product.offers.firstOrNull() ?: run {
                Timber.e("Could not add product $product to cart because it has no offers")
                return
            }
            addProductToCart(product, offer.barcode)
        }
    }

    fun onSubscribeToProductClicked(product: Product) {
        navigationThrottler.throttle {
            val action = ProductSearchScreenAction.SubscribeToProductClicked(product)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    private fun onSearchSuggestionQueryItemClicked(query: String) {
        emitSideEffect(SideEffect.ReleaseSearchTextFieldFocus)
        searchModeValueHolder.set(SearchMode.SEARCH_RESULTS)
        searchTextFieldState.edit {
            clear()
            append(query)
            placeCursorAtEnd()
        }
        searchQueryValueHolder.set(query)
        saveSearchQuery(query)
    }

    private fun addProductToCart(product: Product, barcode: Barcode) {
        viewModelScopeDefault.launch {
            val params = AddProductToCartUseCase.Params(
                product = product,
                barcode = barcode,
                count = 1,
            )
            interactor.addProductToCart(params)
                .onSuccess {
                    val text = Text.Resource(R.string.product_adding_to_cart_completed)
                    val message = ZarinaToastMessage(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
                .onFailure {
                    val text = Text.Resource(R.string.product_adding_to_cart_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    private fun saveSearchQuery(query: String) {
        viewModelScope.launch {
            val entry = ProductSearchHistoryQuery(
                text = query,
                timestampMillis = System.currentTimeMillis(),
            )
            val params = SaveProductSearchHistoryQueryUseCase.Params(entry)
            interactor.saveProductSearchHistoryQuery(params)
        }
    }

    private fun handleSizeSelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<SizeSelectorGraph.Result>(
                resultFlow = sizeSelectorResultFlow,
                key = KEY_SIZE_SELECTOR_RESULT,
            ) { result ->
                addProductToCart(
                    product = result.product.toProductItem(),
                    barcode = result.offer.toProductOffer().barcode,
                )
            }
        }
    }

    private fun handleFiltersResult() {
        viewModelScope.launch {
            screenResultHandler.handle<UnscopedDestinations.ProductSearchFilters.Result>(
                resultFlow = filtersResultFlow,
                key = KEY_FILTERS_RESULT,
            ) { result ->
                val filters = result.filters.toFilters()
                val filtersParcelable = FiltersParcelable.from(filters)
                filtersValueHolder.set(filtersParcelable)
            }
        }
    }

    private fun createSearchSuggestionsState(
        searchHistoryQueriesResult: Result<List<ProductSearchHistoryQuery>>?,
        searchSuggestionsResult: Result<ProductSearchSuggestions>?,
    ): SearchSuggestionsState {
        return searchSuggestionsResult?.fold(
            onSuccess = { suggestions ->
                val searchSuggestionItems = suggestions.toSearchSuggestionItems()
                if (searchSuggestionItems.isNotEmpty()) {
                    val historyQueryItems =
                        searchHistoryQueriesResult?.getOrNull()?.toSearchSuggestionItems()
                    val items = if (historyQueryItems != null) {
                        historyQueryItems + searchSuggestionItems
                    } else {
                        searchSuggestionItems
                    }
                    SearchSuggestionsState.Suggestions(items.toImmutableList())
                } else {
                    SearchSuggestionsState.Empty
                }
            },
            onFailure = { throwable ->
                val errorState = ErrorState.from(throwable).copy(isButtonVisible = false)
                SearchSuggestionsState.Error(errorState)
            },
        ) ?: SearchSuggestionsState.Loading
    }

    private fun List<ProductSearchHistoryQuery>.toSearchSuggestionItems(): List<SearchSuggestionItem> {
        val historyQueries = this
        return buildList {
            if (historyQueries.isNotEmpty()) {
                add(SearchSuggestionItem.SearchHistoryTitle)

                val items = historyQueries
                    .take(SEARCH_HISTORY_QUERIES_MAX_COUNT)
                    .map { query ->
                        SearchSuggestionItem.HistoryQueryItem(query.text.capitalize())
                    }
                addAll(items)
            }
        }
    }

    private fun ProductSearchSuggestions.toSearchSuggestionItems(): List<SearchSuggestionItem> {
        val suggestions = this
        return buildList {
            if (suggestions.searchQueries.isNotEmpty()) {
                val titleText = Text.Resource(R.string.search_results)
                add(SearchSuggestionItem.GenericTitle(titleText))

                val items = suggestions.searchQueries
                    .take(SEARCH_SUGGESTIONS_QUERIES_MAX_COUNT)
                    .map { query ->
                        SearchSuggestionItem.SearchQueryItem(query.capitalize())
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
        }
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

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    @Parcelize
    enum class SearchMode : Parcelable { SEARCH, SEARCH_RESULTS }

    @Stable
    sealed class SearchSuggestionsState {
        @Immutable
        data class Suggestions(val items: ImmutableList<SearchSuggestionItem>) :
            SearchSuggestionsState()

        data object Loading : SearchSuggestionsState()

        data object Empty : SearchSuggestionsState()

        @Immutable
        data class Error(val state: ErrorState) : SearchSuggestionsState()
    }

    @Stable
    sealed class SearchSuggestionItem {
        @Immutable
        data class GenericTitle(val text: Text) : SearchSuggestionItem()

        data object SearchHistoryTitle : SearchSuggestionItem()

        @Immutable
        data class SearchQueryItem(val query: String) : SearchSuggestionItem()

        @Immutable
        data class HistoryQueryItem(val query: String) : SearchSuggestionItem()

        @Immutable
        data class CategoryItem(
            val id: Category.Id,
            val name: String,
            val parentCategoryChain: String?,
        ) : SearchSuggestionItem()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            sizeSelectorResultFlow: StateFlow<SizeSelectorGraph.Result?>,
            filtersResultFlow: StateFlow<UnscopedDestinations.ProductSearchFilters.Result?>,
        ): ProductSearchViewModel
    }

    companion object {
        private const val KEY_SEARCH_MODE = "search_mode"
        private const val KEY_SEARCH_QUERY = "search_query"
        private const val KEY_FILTERS = "filters"

        private const val KEY_SIZE_SELECTOR_RESULT = "size_selector_result"
        private const val KEY_FILTERS_RESULT = "filters_result"

        private const val SEARCH_HISTORY_QUERIES_MAX_COUNT = 5
        private const val SEARCH_SUGGESTIONS_QUERIES_MAX_COUNT = 5
        private const val SEARCH_SUGGESTIONS_CATEGORIES_MAX_COUNT = 5

        private const val CATEGORY_PARENT_CATEGORY_CHAIN_PATTERN = "\\(.+\\)"
        private const val CATEGORY_PARENT_CATEGORY_CHAIN_SEPARATOR = " - "

        private const val BRACKET_START = "("
        private const val BRACKET_END = ")"
    }
}
