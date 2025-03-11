package ru.livetyping.zarina.feature.search.ui.impl.impl.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.sizeselector.SizeSelectorEvent
import ru.livetyping.zarina.core.uikit.sizeselector.SizeSelectorModalBottomSheet
import ru.livetyping.zarina.core.uikit.sizeselector.SizeSelectorState
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikitpaging.product.ProductGrid
import ru.livetyping.zarina.core.uikitpaging.product.ProductGridSideEffect
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.component.NothingFoundPlaceholder
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.component.SearchBar
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.component.SearchContent
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.model.SearchBarEvent
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.model.SearchBarState
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.model.SearchEvent
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.model.SearchMode
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.model.SearchResultEvent
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.model.SearchState

@Composable
internal fun SearchScreen(
    navActions: SearchNavActions,
    viewModel: SearchViewModel,
) {
    val searchBarState by viewModel.searchBarState.collectAsStateWithLifecycle()
    val searchMode by viewModel.searchMode.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val sizeSelectorState by viewModel.sizeSelectorState.collectAsStateWithLifecycle()

    ScreenContent(
        searchBarState = searchBarState,
        onSearchBarEvent = viewModel::onSearchBarEvent,
        searchMode = searchMode,
        searchState = searchState,
        onSearchEvent = viewModel::onSearchEvent,
        searchResultPagingDataFlow = viewModel.searchResultPagingDataFlow,
        onSearchResultEvent = viewModel::onSearchResultEvent,
        productGridSideEffects = viewModel.productGridSideEffects,
        sizeSelectorState = sizeSelectorState,
        onSizeSelectorEvent = viewModel::onSizeSelectorEvent,
        onLifecycleEvent = viewModel::onLifecycleEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    searchBarState: SearchBarState,
    onSearchBarEvent: (SearchBarEvent) -> Unit,
    searchMode: SearchMode,
    searchState: SearchState,
    onSearchEvent: (SearchEvent) -> Unit,
    searchResultPagingDataFlow: Flow<PagingData<ProductShort>>,
    onSearchResultEvent: (SearchResultEvent) -> Unit,
    productGridSideEffects: Flow<ProductGridSideEffect>,
    sizeSelectorState: SizeSelectorState,
    onSizeSelectorEvent: (SizeSelectorEvent) -> Unit,
    onLifecycleEvent: (LifecycleEvent) -> Unit,
    sideEffects: Flow<SearchSideEffect>,
    navActions: SearchNavActions,
) {
    SearchScreenBehavior(
        onLifecycleEvent = onLifecycleEvent,
        sideEffects = sideEffects,
        navActions = navActions,
    )

    SizeSelectorModalBottomSheet(
        state = sizeSelectorState,
        onEvent = onSizeSelectorEvent,
    )

    val backgroundColor = UiKitTheme.colors.background.general.regular.default

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        val searchBarFocusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            if (searchBarState.searchMode == SearchMode.SEARCH) {
                withFrameMillis {}
                searchBarFocusRequester.tryRequestFocus()
            }
        }

        SearchBar(
            state = searchBarState,
            onEvent = onSearchBarEvent,
            focusRequester = searchBarFocusRequester,
        )

        Crossfade(
            targetState = searchMode,
            modifier = Modifier.fillMaxSize(),
        ) { mode ->
            when (mode) {
                SearchMode.SEARCH -> {
                    SearchContent(
                        state = searchState,
                        onEvent = onSearchEvent,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(backgroundColor),
                    )
                }

                SearchMode.RESULTS -> {
                    ProductGrid(
                        productPagingDataFlow = searchResultPagingDataFlow,
                        onProductClicked = {
                            onSearchResultEvent(SearchResultEvent.ProductClicked(it))
                        },
                        onAddToWishlistClicked = {
                            onSearchResultEvent(SearchResultEvent.AddToWishlistClicked(it))
                        },
                        onAddToCartClicked = {
                            onSearchResultEvent(SearchResultEvent.AddToCartClicked(it))
                        },
                        onSubscribeClicked = {
                            onSearchResultEvent(SearchResultEvent.SubscribeToProductClicked(it))
                        },
                        emptyProductsPlaceholder = {
                            NothingFoundPlaceholder(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                            )
                        },
                        sideEffects = productGridSideEffects,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(backgroundColor),
                    )
                }
            }
        }
    }
}
