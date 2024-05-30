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
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.presentation.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.util.library.paging.mapProducts
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.graph.SizeSelectorGraph
import ru.livetyping.zarina.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.productsearch.GetProductSearchSuggestionsFlowUseCase
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
    backStackEntrySavedStateHandle: SavedStateHandle,
    savedStateHandle: SavedStateHandle,
    private val interactor: ProductSearchInteractor,
) : ViewModel(), SideEffectSource<ProductSearchViewModel.SideEffect> by SideEffectSourceImpl() {

    private val viewModelScopeDefault = viewModelScope + Dispatchers.Default

    private val screenResultHandler = ScreenResultHandler(
        backStackEntrySavedStateHandle = backStackEntrySavedStateHandle,
        savedStateHandle = savedStateHandle,
    )

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

    val searchSuggestionsState: StateFlow<SearchSuggestionsState> = searchSuggestionsResult.mapState(
        scope = viewModelScopeDefault,
        started = SharingStarted.WhileUiSubscribed,
    ) { result ->
        result?.toSearchSuggestionsState() ?: SearchSuggestionsState.Loading
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val productSearchResultPagingDataFlow: Flow<PagingData<ProductItem>> =
        searchQueryValueHolder.stateFlow
            .filter { it.isNotBlank() }
            .flatMapLatest { query ->
                interactor.productSearchResultPager.getProductPagingDataFlow(
                    query = query,
                    sorting = Sorting.NEW,
                )
            }
            .cachedIn(viewModelScopeDefault)
            .mapProducts(
                favoriteProductIdsResultFlow = interactor.getFavoriteProductIdsFlow(),
                cartProductIdsResultFlow = interactor.getCardProductsIdsFlow(),
            )
            .cachedIn(viewModelScopeDefault)

    private val categoryParentCategoryChainRegex = CATEGORY_PARENT_CATEGORY_CHAIN_PATTERN.toRegex()

    init {
        handleSizeSelectorResult()
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
                emitSideEffect(SideEffect.ReleaseSearchTextFieldFocus)
                searchModeValueHolder.set(SearchMode.SEARCH_RESULTS)
                searchTextFieldState.edit {
                    clear()
                    append(item.query)
                    placeCursorAtEnd()
                }
                searchQueryValueHolder.set(item.query)
            }

            is SearchSuggestionItem.CategoryItem -> {
                navigationThrottler.throttle {
                    val action = ProductSearchScreenAction.CategoryClicked(item.id)
                    emitSideEffect(SideEffect.Navigate(action))
                }
            }

            is SearchSuggestionItem.GenericTitle -> Unit
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
            addProductToCart(product.id, offer.barcode)
        }
    }

    fun onSubscribeToProductClicked(product: Product) {
        navigationThrottler.throttle {
            val action = ProductSearchScreenAction.SubscribeToProductClicked(product)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    private fun addProductToCart(productId: Product.Id, barcode: Barcode) {
        viewModelScopeDefault.launch {
            val params = AddProductToCartUseCase.Params(
                productId = productId,
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

    private fun handleSizeSelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<SizeSelectorGraph.Result>(
                key = SizeSelectorGraph.RESULT_KEY,
            ) { result ->
                addProductToCart(
                    productId = result.product.toProductItem().id,
                    barcode = result.offer.toProductOffer().barcode,
                )
            }
        }
    }

    private fun Result<ProductSearchSuggestions>.toSearchSuggestionsState(): SearchSuggestionsState {
        return this.fold(
            onSuccess = { suggestions ->
                val items = suggestions.toSearchSuggestionItems().toImmutableList()
                if (items.isNotEmpty()) {
                    SearchSuggestionsState.Suggestions(items)
                }else {
                    SearchSuggestionsState.Empty
                }
            },
            onFailure = { throwable ->
                val errorState = ErrorState.from(throwable).copy(isButtonVisible = false)
                SearchSuggestionsState.Error(errorState)
            },
        )
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

        @Immutable
        data class SearchQueryItem(val query: String) : SearchSuggestionItem()

        @Immutable
        data class CategoryItem(
            val id: Category.Id,
            val name: String,
            val parentCategoryChain: String?,
        ) : SearchSuggestionItem()
    }

    @AssistedFactory
    interface Factory {
        fun create(backStackEntrySavedStateHandle: SavedStateHandle): ProductSearchViewModel
    }

    companion object {
        private const val KEY_SEARCH_MODE = "search_mode"
        private const val KEY_SEARCH_QUERY = "search_query"

        private const val SEARCH_SUGGESTIONS_QUERIES_MAX_COUNT = 5
        private const val SEARCH_SUGGESTIONS_CATEGORIES_MAX_COUNT = 5

        private const val CATEGORY_PARENT_CATEGORY_CHAIN_PATTERN = "\\(.+\\)"
        private const val CATEGORY_PARENT_CATEGORY_CHAIN_SEPARATOR = " - "

        private const val BRACKET_START = "("
        private const val BRACKET_END = ")"
    }
}
