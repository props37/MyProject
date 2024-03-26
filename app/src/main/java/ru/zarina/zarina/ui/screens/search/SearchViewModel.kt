package ru.zarina.zarina.ui.screens.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.old.AutocompleteWord
import ru.zarina.zarina.domain.old.FilteredProducts
import ru.zarina.zarina.domain.old.Filtration
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.domain.old.ProductSort
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.paging.PageHolder
import ru.zarina.zarina.ui.navigation.old.destinations.Catalog
import ru.zarina.zarina.ui.screens.catalog.products.ProductsViewModel
import ru.zarina.zarina.ui.screens.search.paging.SearchPagingSource
import ru.zarina.zarina.ui.screens.search.paging.SearchRemoteMediator
import ru.zarina.zarina.usecase.old.search.GetSearchPageUseCase
import ru.zarina.zarina.utils.coroutine.mapState
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
class SearchViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: SearchInteractor,
) : ViewModel(),
    ISideEffectSource<SearchViewModel.SideEffect> by SideEffectQueue() {

    val query = savedStateHandle.getStateFlow(KEY_QUERY, "")

    val sort = savedStateHandle.getStateFlow(KEY_SELECTED_SORT, ProductSort.POPULARITY)

    private val _isQueryFocused = MutableStateFlow(true)
    val isQueryFocused = _isQueryFocused.asStateFlow()
    val state = _isQueryFocused.mapState(viewModelScope) { isFocused ->
        if (isFocused) State.AUTOCOMPLETE else State.RESULT
    }

    val recommendations = interactor.getRecommendations()
        .map { it.getOrNull()?.toPersistentList() ?: persistentListOf() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), persistentListOf())

    val searchHistory = interactor.getSearchHistory(SEARCH_HISTORY_LIMIT)
        .map { it.getOrNull()?.toPersistentList() ?: persistentListOf() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, persistentListOf())
    val isSearchHistoryVisible =
        combine(query, searchHistory) { query, searchHistory ->
            query.isEmpty() && searchHistory.isNotEmpty()
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _autocompleteResult = query
        .flatMapLatest { query ->
            flowOf(interactor.getAutocomplete(query))
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val autocomplete = _autocompleteResult
        .mapState(viewModelScope) { it?.getOrNull() }

    val isAutocompleteWordsVisible = autocomplete
        .mapState(viewModelScope, SharingStarted.WhileSubscribed()) {
            !it?.words.isNullOrEmpty()
        }
    val isFrequentSearchVisible = autocomplete
        .mapState(viewModelScope, SharingStarted.WhileSubscribed()) {
            !it?.frequentQueries.isNullOrEmpty()
        }

    private val pagerQuery = MutableStateFlow<String?>(null)

    /**
     * Default filtration used when no filtration was sent.
     */
    private val baseFiltration =
        savedStateHandle.getStateFlow<Filtration?>(KEY_BASE_FILTRATION, null)

    /**
     * Filtration that was applied to the products currently displayed.
     */
    private val appliedFiltration =
        savedStateHandle.getStateFlow<Filtration?>(KEY_APPLIED_FILTRATION, null)

    /**
     * The latest filtration that was requested by the user.
     */
    private val requestedFiltration =
        savedStateHandle.getStateFlow<Filtration?>(
            KEY_REQUESTED_FILTRATION,
            savedStateHandle[Catalog.Products.ARGUMENT_FILTRATION]
        )

    val isFilterButtonEnabled = requestedFiltration
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val pager =
        combine(pagerQuery.filterNotNull(), sort, requestedFiltration) { query, sort, filtration ->
            createPager(query, sort, filtration)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val pagingSource = MutableStateFlow<SearchPagingSource?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val products = pager.flatMapLatest {
        it?.flow?.cachedIn(viewModelScope) ?: emptyFlow()
    }
        .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    private val _shakingFavorites = MutableStateFlow(setOf<Product.Id>())
    val shakingFavorites =
        _shakingFavorites.mapState(viewModelScope, SharingStarted.WhileSubscribed()) {
            it.toPersistentSet()
        }

    private val isForeground = MutableStateFlow(false)

    init {
        setupAppliedFiltrationUpdates()
        setupPagingInvalidation()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setupPagingInvalidation() {
        isForeground.flatMapLatest {
            if (it) interactor.getFavoriteIds() else flowOf(interactor.getFavoriteIds().first())
        }
            .distinctUntilChanged()
            .onEach { pagingSource.value?.invalidate() }
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setupAppliedFiltrationUpdates() {
        pagingSource
            .flatMapLatest { it?.appliedFiltration ?: emptyFlow() }
            .filterNotNull()
            .onEach {
                savedStateHandle[ProductsViewModel.KEY_APPLIED_FILTRATION] = it
                if (baseFiltration.value == null)
                    savedStateHandle[ProductsViewModel.KEY_BASE_FILTRATION] = it
                if (requestedFiltration.value == null)
                    savedStateHandle[ProductsViewModel.KEY_REQUESTED_FILTRATION] = it
            }
            .launchIn(viewModelScope)
    }

    fun onQueryFocusChange(isFocused: Boolean) {
        _isQueryFocused.value = isFocused
    }

    fun onQueryChange(query: String) {
        savedStateHandle[KEY_QUERY] = query
    }

    fun onQueryClearClick() {
        savedStateHandle[KEY_QUERY] = ""
        _isQueryFocused.value = true
    }

    fun onSearchHistoryClick(query: String) {
        savedStateHandle[KEY_QUERY] = query
    }

    fun onSearchHistoryDeleteClick(query: String) {
        viewModelScope.launch {
            interactor.removeFromSearchHistory(query)
        }
    }

    fun onAutocompleteWordClick(word: AutocompleteWord) {
        savedStateHandle[KEY_QUERY] = word.query
    }

    fun onFrequentlySearchedClick(query: String) {
        savedStateHandle[KEY_QUERY] = query
    }

    fun onSearchClick() {
        val query = query.value
        if (query.isBlank()) return
        viewModelScope.launch {
            interactor.addToSearchHistory(query)
        }
        _isQueryFocused.value = false
        pagerQuery.value = query
    }

    fun onSortClick() {
        sideEffect(SideEffect.ShowSelectSort)
    }

    fun onFilterClick() {
        sideEffect(SideEffect.ShowFilters)
    }

    fun onProductClick(product: Product) {
        sideEffect(SideEffect.ShowProduct(product))
    }

    fun onFavoriteChange(product: Product, isFavorite: Boolean) {
        viewModelScope.launch {
            interactor.setIsFavorite(product, isFavorite)
                .onFailure {
                    _shakingFavorites.update { it + product.id }
                    delay(FAVORITE_SHAKE_DURATION)
                    _shakingFavorites.update { it - product.id }
                }
        }
    }

    fun onIsForegroundChange(isForeground: Boolean) {
        this.isForeground.value = isForeground
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun createPager(
        query: String,
        sort: ProductSort,
        filtration: Filtration?,
    ): Pager<Int, Product> {
        sideEffect(SideEffect.ScrollResultsToTop)
        val pageHolder = PageHolder<FilteredProducts>()
        val mediator = SearchRemoteMediator(
            pageHolder = pageHolder,
            getSearchPageUseCase = interactor.getSearchPageUseCase,
            query = query,
            sort = sort,
            filtration = filtration,
        )
        return Pager(
            config = PagingConfig(
                pageSize = GetSearchPageUseCase.PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                val source = SearchPagingSource(
                    pageHolder = pageHolder,
                    getFavoriteIdsUseCase = interactor.getFavoriteIdsUseCase,
                )
                mediator.addListener(source)
                pagingSource.value = source
                source
            },
            remoteMediator = mediator,
            initialKey = 0,
        )
    }

    enum class State { AUTOCOMPLETE, RESULT }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        data class ShowProduct(val product: Product) : SideEffect
        object ShowSelectSort : SideEffect
        object ShowFilters : SideEffect
        object ScrollResultsToTop : SideEffect
    }

    companion object {
        private const val SEARCH_HISTORY_LIMIT = 15

        const val KEY_QUERY = "query"
        const val KEY_SELECTED_SORT = "selected_sort"
        const val KEY_BASE_FILTRATION = "base_filtration"
        const val KEY_APPLIED_FILTRATION = "applied_filtration"
        const val KEY_REQUESTED_FILTRATION = "requested_filtration"

        private val FAVORITE_SHAKE_DURATION = 1.seconds
    }

}
