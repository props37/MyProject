package ru.livetyping.zarina.presentation.screen.productsearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchScreenComponents.SearchSuggestions
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SearchMode
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SearchSuggestionItem
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.tryRequestFocus
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ProductSearchScreen(
    navigate: (ProductSearchScreenAction) -> Unit,
    viewModel: ProductSearchViewModel = hiltViewModel(),
) {
    val searchMode by viewModel.searchMode.collectAsStateWithLifecycle()
    val searchSuggestionItems by viewModel.searchSuggestionItems.collectAsStateWithLifecycle()

    ScreenContent(
        onBackClicked = viewModel::onBackClicked,
        searchTextFieldState = viewModel.searchTextFieldState,
        onSearchTextFieldSearchClicked = viewModel::onSearchTextFieldSearchClicked,
        onSearchTextFieldFocused = viewModel::onSearchTextFieldFocused,
        searchMode = searchMode,
        onSearchBarCancelClicked = viewModel::onSearchTextFieldCancelClicked,
        searchSuggestionItems = searchSuggestionItems,
        onSearchSuggestionItemClicked = viewModel::onSearchSuggestionItemClicked,
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
    searchSuggestionItems: ImmutableList<SearchSuggestionItem>,
    onSearchSuggestionItemClicked: (SearchSuggestionItem) -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProductSearchScreenAction) -> Unit,
) {
    ProductSearchScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(300.milliseconds)
        focusRequester.tryRequestFocus()
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
            modifier = Modifier.focusRequester(focusRequester),
        )

        SearchSuggestions(
            query = searchTextFieldState.text.toString(),
            items = searchSuggestionItems,
            onItemClicked = onSearchSuggestionItemClicked,
            modifier = Modifier.fillMaxSize(),
        )
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
