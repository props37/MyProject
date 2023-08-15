package ru.zarina.zarina.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.AutocompleteWord
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.paging.PageHolder
import ru.zarina.zarina.ui.screens.search.paging.SearchPagingSource
import ru.zarina.zarina.ui.screens.search.paging.SearchRemoteMediator
import ru.zarina.zarina.usecase.search.GetSearchPageUseCase
import ru.zarina.zarina.utils.coroutine.mapState

@KoinViewModel
class SearchViewModel(
    private val interactor: SearchInteractor,
) : ViewModel(),
    ISideEffectSource<SearchViewModel.SideEffect> by SideEffectQueue() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    val searchHistory = interactor.getSearchHistory(SEARCH_HISTORY_LIMIT)
        .map { it.getOrNull()?.toPersistentList() ?: persistentListOf() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, persistentListOf())
    val isSearchHistoryVisible =
        combine(_query, searchHistory) { query, searchHistory ->
            query.isEmpty() && searchHistory.isNotEmpty()
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _autocompleteResult = _query
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

    private val pager = MutableStateFlow<Pager<Int, Product>?>(null)
    private val pagingSource = MutableStateFlow<SearchPagingSource?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val products = pager.flatMapLatest {
        it?.flow?.cachedIn(viewModelScope) ?: emptyFlow()
    }
        .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    val shakingFavorites = MutableStateFlow(persistentListOf<Product.Id>()) // TODO

    // TODO setup paging source invalidation on favorites change

    fun onQueryChange(query: String) {
        _query.value = query
    }

    fun onQueryClearClick() {
        _query.value = ""
    }

    fun onSearchHistoryClick(query: String) {
        _query.value = query
    }

    fun onSearchHistoryDeleteClick(query: String) {
        viewModelScope.launch {
            interactor.removeFromSearchHistory(query)
        }
    }

    fun onAutocompleteWordClick(word: AutocompleteWord) {
        _query.value = word.query
    }

    fun onFrequentlySearchedClick(query: String) {
        _query.value = query
    }

    fun onSearchClick() {
        val query = _query.value
        if (query.isBlank()) return
        viewModelScope.launch {
            interactor.addToSearchHistory(query)
        }
        pager.value = createPager(query)
    }


    fun onProductClick(product: Product) {
        sideEffect(SideEffect.ShowProduct(product))
    }

    fun onFavoriteChange(product: Product, favorite: Boolean) {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun createPager(query: String): Pager<Int, Product> {
        // TODO scroll to top
        val pageHolder = PageHolder<List<Product>>()
        val mediator = SearchRemoteMediator(
            pageHolder = pageHolder,
            getSearchPageUseCase = interactor.getSearchPageUseCase,
            query = query,
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

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        data class ShowProduct(val product: Product) : SideEffect
    }

    companion object {
        private const val SEARCH_HISTORY_LIMIT = 15
    }

}
