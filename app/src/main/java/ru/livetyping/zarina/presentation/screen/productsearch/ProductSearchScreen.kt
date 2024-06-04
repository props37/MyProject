package ru.livetyping.zarina.presentation.screen.productsearch

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.component.ProductGrid
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchScreenComponents.NothingFoundPlaceholder
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchScreenComponents.SearchSuggestions
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SearchMode
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SearchSuggestionItem
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SearchSuggestionsState
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.tryRequestFocus
import kotlin.time.Duration.Companion.milliseconds

// TODO: [High] Add search history

@Composable
fun ProductSearchScreen(
    navigate: (ProductSearchScreenAction) -> Unit,
    viewModel: ProductSearchViewModel = hiltViewModel(),
) {
    val searchMode by viewModel.searchMode.collectAsStateWithLifecycle()
    val searchAutocompleteSuggestions by viewModel.searchAutocompleteSuggestions.collectAsStateWithLifecycle()
    val searchSuggestionsState by viewModel.searchSuggestionsState.collectAsStateWithLifecycle()
    val appliedFilterCount by viewModel.appliedFilterCount.collectAsStateWithLifecycle()

    ScreenContent(
        onBackClicked = viewModel::onBackClicked,
        searchTextFieldState = viewModel.searchTextFieldState,
        onSearchTextFieldSearchClicked = viewModel::onSearchTextFieldSearchClicked,
        onSearchTextFieldFocused = viewModel::onSearchTextFieldFocused,
        searchMode = searchMode,
        onSearchBarCancelClicked = viewModel::onSearchTextFieldCancelClicked,
        onFiltersClicked = viewModel::onFiltersClicked,
        appliedFilterCount = appliedFilterCount,
        searchAutocompleteSuggestions = searchAutocompleteSuggestions,
        onSearchAutocompleteSuggestionClicked = viewModel::onSearchAutocompleteSuggestionClicked,
        searchSuggestionsState = searchSuggestionsState,
        onSearchSuggestionItemClicked = viewModel::onSearchSuggestionItemClicked,
        productSearchResultPagingDataFlow = viewModel.productSearchResultPagingDataFlow,
        onProductClicked = viewModel::onProductClicked,
        onAddProductToFavoritesClicked = viewModel::onAddProductToFavoritesClicked,
        onAddProductToCartClicked = viewModel::onAddProductToCartClicked,
        onSubscribeToProductClicked = viewModel::onSubscribeToProductClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    onBackClicked: () -> Unit,
    searchTextFieldState: TextFieldState,
    onSearchTextFieldSearchClicked: () -> Unit,
    onSearchTextFieldFocused: () -> Unit,
    searchMode: SearchMode,
    onSearchBarCancelClicked: () -> Unit,
    onFiltersClicked: () -> Unit,
    appliedFilterCount: Int,
    searchAutocompleteSuggestions: ImmutableList<ProductSearchSuggestions.AutocompleteSuggestion>,
    onSearchAutocompleteSuggestionClicked: (ProductSearchSuggestions.AutocompleteSuggestion) -> Unit,
    searchSuggestionsState: SearchSuggestionsState,
    onSearchSuggestionItemClicked: (SearchSuggestionItem) -> Unit,
    productSearchResultPagingDataFlow: Flow<PagingData<ProductItem>>,
    onProductClicked: (Product) -> Unit,
    onAddProductToFavoritesClicked: (Product) -> Unit,
    onAddProductToCartClicked: (Product) -> Unit,
    onSubscribeToProductClicked: (Product) -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProductSearchScreenAction) -> Unit,
) {
    ProductSearchScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        if (searchMode == SearchMode.SEARCH) {
            delay(300.milliseconds)
            focusRequester.tryRequestFocus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .imePadding()
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TopBar(
            searchTextFieldState = searchTextFieldState,
            onSearchTextFieldSearchClicked = onSearchTextFieldSearchClicked,
            onSearchTextFieldFocused = onSearchTextFieldFocused,
            onSearchTextFieldCancelClicked = onSearchBarCancelClicked,
            searchMode = searchMode,
            onBackClicked = onBackClicked,
            onFiltersClicked = onFiltersClicked,
            appliedFilterCount = appliedFilterCount,
            modifier = Modifier.focusRequester(focusRequester),
        )

        Crossfade(
            targetState = searchMode,
            label = "Search mode",
            modifier = Modifier.fillMaxSize(),
        ) { mode ->
            when (mode) {
                SearchMode.SEARCH -> {
                    SearchSuggestions(
                        state = searchSuggestionsState,
                        autocompleteSuggestions = searchAutocompleteSuggestions,
                        query = searchTextFieldState.text.toString(),
                        onSearchSuggestionItemClicked = onSearchSuggestionItemClicked,
                        onAutocompleteSuggestionClicked = onSearchAutocompleteSuggestionClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(UiKitTheme.colors.background.general.regular.default),
                    )
                }

                SearchMode.SEARCH_RESULTS -> {
                    ProductGrid(
                        productPagingDataFlow = productSearchResultPagingDataFlow,
                        onProductClicked = onProductClicked,
                        onAddToFavoritesClicked = onAddProductToFavoritesClicked,
                        onAddToCartClicked = onAddProductToCartClicked,
                        onSubscribeClicked = onSubscribeToProductClicked,
                        noProductsPlaceholder = {
                            NothingFoundPlaceholder(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                            )
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(UiKitTheme.colors.background.general.regular.default),
                    )
                }
            }
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
