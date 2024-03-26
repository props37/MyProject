package ru.zarina.zarina.ui.screens.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.old.AutocompleteWord
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.domain.old.ProductSort
import ru.zarina.zarina.domain.old.SearchAutocomplete
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.ElevationContainer
import ru.zarina.zarina.ui.common.components.FilterBar
import ru.zarina.zarina.ui.common.components.InputSearchBar
import ru.zarina.zarina.ui.common.components.ProductCard
import ru.zarina.zarina.ui.common.components.ProductHorizontalSection
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.bottomNavigationPadding
import ru.zarina.zarina.ui.common.components.color.ColorPickerDefaults
import ru.zarina.zarina.ui.common.utils.domain.getStringResource
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.utils.compose.navigationOrIme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchScreenContent(
    state: SearchViewModel.State,
    query: String,
    isQueryFocused: Boolean,
    onQueryFocusChange: (Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    onQueryClearClick: () -> Unit,
    onSearchClick: () -> Unit,
    sort: ProductSort,
    onSortClick: () -> Unit,
    isFilterButtonEnabled: Boolean,
    onFilterClick: () -> Unit,
    isSearchHistoryVisible: Boolean,
    searchHistory: ImmutableList<String>,
    onSearchHistoryClick: (String) -> Unit,
    onSearchHistoryDeleteClick: (String) -> Unit,
    isAutocompleteWordsVisible: Boolean,
    isFrequentSearchVisible: Boolean,
    autocomplete: SearchAutocomplete?,
    onAutocompleteWordClick: (AutocompleteWord) -> Unit,
    onFrequentlySearchedClick: (String) -> Unit,
    products: LazyPagingItems<Product>,
    resultsLazyGridState: LazyGridState,
    onProductClick: (Product) -> Unit,
    onFavoriteChange: (Product, Boolean) -> Unit,
    recommendations: ImmutableList<Product>,
    shakingFavorites: ImmutableSet<Product.Id>,
) {
    val contentScrollState = rememberScrollState()
    val nothingFoundScrollState = rememberScrollState()
    ZarinaScaffold(
        toolbar = {
            // TODO elevation for all screen states
            ElevationContainer(isElevated = contentScrollState.canScrollBackward) {
                val focusRequester = remember { FocusRequester() }
                InputSearchBar(
                    value = query,
                    onValueChange = onQueryChange,
                    onClearClick = onQueryClearClick,
                    onSearchClick = onSearchClick,
                    modifier = Modifier
                        .statusBarsPadding()
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            onQueryFocusChange(it.hasFocus)
                        }
                )
                val focusManager = LocalFocusManager.current
                LaunchedEffect(isQueryFocused) {
                    if (isQueryFocused)
                        focusRequester.requestFocus()
                    else
                        focusManager.clearFocus()
                }
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedContent(
                targetState = state,
                label = "state content",
                modifier = Modifier.fillMaxSize(),
            ) { state ->
                when (state) {
                    SearchViewModel.State.AUTOCOMPLETE -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(state = contentScrollState),
                        ) {
                            if (isSearchHistoryVisible)
                                SearchHistory(
                                    queries = searchHistory,
                                    onQueryClick = onSearchHistoryClick,
                                    onQueryDeleteClick = onSearchHistoryDeleteClick
                                )
                            if (isAutocompleteWordsVisible)
                                Words(
                                    words = autocomplete?.words ?: persistentListOf(),
                                    onWordClick = onAutocompleteWordClick,
                                )
                            // TODO add categories autocomplete
                            if (isFrequentSearchVisible)
                                FrequentlySearched(
                                    queries = autocomplete?.frequentQueries ?: persistentListOf(),
                                    onQueryClick = onFrequentlySearchedClick,
                                    modifier = Modifier.padding(WindowInsets.navigationOrIme.asPaddingValues())
                                )
                        }
                    }

                    SearchViewModel.State.RESULT -> {
                        if (
                            products.itemCount == 0
                            && products.loadState.refresh != LoadState.Loading
                            && products.loadState.append != LoadState.Loading
                        ) {
                            NothingFound(
                                recommendations = recommendations,
                                shakingFavorites = shakingFavorites,
                                onProductClick = onProductClick,
                                onFavoriteChange = onFavoriteChange,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(nothingFoundScrollState)
                            )
                        } else {
                            Results(
                                lazyGridState = resultsLazyGridState,
                                products = products,
                                sort = sort,
                                onSortClick = onSortClick,
                                isFilterButtonEnabled = isFilterButtonEnabled,
                                onFilterClick = onFilterClick,
                                onProductClick = onProductClick,
                                onFavoriteChange = onFavoriteChange,
                                shakingFavorites = shakingFavorites,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .bottomNavigationPadding()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchHistory(
    queries: ImmutableList<String>,
    onQueryClick: (String) -> Unit,
    onQueryDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionHeader(
        text = stringResource(id = R.string.search_history),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 6.dp)
    )
    Column(
        modifier = modifier
    ) {
        queries.forEach { query ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onQueryClick(query) })
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = query,
                    style = UiKitTheme.typographyOld.circle1718,
                    color = UiKitTheme.colorsOld.primaryContentColor,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 12.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_cross_24),
                    contentDescription = stringResource(id = R.string.remove_from_history),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { onQueryDeleteClick(query) }
                        .minimumInteractiveComponentSize()
                        .clip(CircleShape)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Words(
    words: ImmutableList<AutocompleteWord>,
    onWordClick: (AutocompleteWord) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        horizontalArrangement = Arrangement.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 11.dp, vertical = 10.dp),
    ) {
        for (word in words) {
            Word(
                word = word,
                // TODO move input cursor to end
                onClick = { onWordClick(word) },
            )
        }
    }
}

@Composable
private fun Word(
    word: AutocompleteWord,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(5.dp)
            .background(UiKitTheme.colorsOld.primaryBorderColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(
            text = word.word,
            maxLines = 1,
            style = UiKitTheme.typographyOld.circle1718,
            color = UiKitTheme.colorsOld.primaryContentColor,
        )
    }
}

@Composable
private fun FrequentlySearched(
    queries: ImmutableList<String>,
    onQueryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        SectionHeader(
            text = stringResource(id = R.string.frequently_searched),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 6.dp),
        )
        for (query in queries) {
            Text(
                text = query,
                style = UiKitTheme.typographyOld.circle1718,
                color = UiKitTheme.colorsOld.primaryContentColor,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onQueryClick(query) })
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            )
        }
    }
}

@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = UiKitTheme.typographyOld.circle1518,
        color = UiKitTheme.colorsOld.primaryContentColor,
        maxLines = 1,
        modifier = modifier,
    )
}

@Composable
private fun Results(
    lazyGridState: LazyGridState,
    products: LazyPagingItems<Product>,
    sort: ProductSort,
    onSortClick: () -> Unit,
    isFilterButtonEnabled: Boolean,
    onFilterClick: () -> Unit,
    onProductClick: (Product) -> Unit,
    onFavoriteChange: (Product, Boolean) -> Unit,
    shakingFavorites: ImmutableSet<Product.Id>,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        val colorPickerDimensions = ColorPickerDefaults.tinyDimensions()

        FilterBar(
            sort = sort,
            sortName = { stringResource(it.getStringResource()) },
            onSortClick = onSortClick,
            isFilterButtonEnabled = isFilterButtonEnabled,
            onFiltersClick = onFilterClick,
            modifier = Modifier.fillMaxWidth()
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = lazyGridState,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        ) {
            items(
                count = products.itemCount,
                key = products.itemKey { it.id.value },
                contentType = products.itemContentType { null }
            ) { productIndex ->
                val product = products[productIndex]
                if (product != null)
                    ProductCard(
                        product = product,
                        isMediaScrollable = true,
                        onClick = { onProductClick(product) },
                        isFavoriteShaking = shakingFavorites.contains(product.id),
                        onFavoriteChange = { onFavoriteChange(product, it) },
                        colorPickerDimensions = colorPickerDimensions,
                    )
            }
        }
    }
}

@Composable
private fun NothingFound(
    recommendations: ImmutableList<Product>,
    shakingFavorites: ImmutableSet<Product.Id>,
    onProductClick: (Product) -> Unit,
    onFavoriteChange: (Product, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(36.dp))
        Image(
            painter = painterResource(id = R.drawable.old_ic_magnifying_glass_96),
            contentDescription = null,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = stringResource(id = R.string.nothing_found),
            style = UiKitTheme.typographyOld.circle1720bold,
            color = UiKitTheme.colorsOld.primaryContentColor,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.nothing_found),
            style = UiKitTheme.typographyOld.circle1518,
            color = UiKitTheme.colorsOld.primaryContentColor,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp)
        )
        Spacer(modifier = Modifier.height(36.dp))
        ProductHorizontalSection(
            title = stringResource(id = R.string.recommended_for_you),
            products = recommendations,
            shakingFavorites = shakingFavorites,
            onProductClick = onProductClick,
            onFavoriteChange = onFavoriteChange,
        )
        Spacer(modifier = Modifier.padding(WindowInsets.navigationOrIme.asPaddingValues()))
    }
}

@Composable
fun SearchScreen(
    savedStateHandle: SavedStateHandle,
    showProduct: (Product) -> Unit,
    showSelectSort: () -> Unit,
    showFilters: () -> Unit,
) {
    val viewModel = koinViewModel<SearchViewModel> { parametersOf(savedStateHandle) }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val recommendations by viewModel.recommendations.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val sort by viewModel.sort.collectAsStateWithLifecycle()
    val isFilterButtonEnabled by viewModel.isFilterButtonEnabled.collectAsStateWithLifecycle()
    val isQueryFocused by viewModel.isQueryFocused.collectAsStateWithLifecycle()
    val isSearchHistoryVisible by viewModel.isSearchHistoryVisible.collectAsStateWithLifecycle()
    val searchHistory by viewModel.searchHistory.collectAsStateWithLifecycle()
    val isAutocompleteWordsVisible by viewModel.isAutocompleteWordsVisible.collectAsStateWithLifecycle()
    val isFrequentSearchVisible by viewModel.isFrequentSearchVisible.collectAsStateWithLifecycle()
    val autocomplete by viewModel.autocomplete.collectAsStateWithLifecycle()
    val products = viewModel.products.collectAsLazyPagingItems()
    val shakingFavorites by viewModel.shakingFavorites.collectAsStateWithLifecycle()

    val resultsLazyGridState = rememberLazyGridState()

    DisposableEffect(Unit) {
        viewModel.onIsForegroundChange(true)
        onDispose { viewModel.onIsForegroundChange(false) }
    }

    SearchScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showProduct = showProduct,
        showSelectSort = showSelectSort,
        showFilters = showFilters,
        resultsLazyGridState = resultsLazyGridState,
    )

    SearchScreenContent(
        state = state,
        query = query,
        isQueryFocused = isQueryFocused,
        onQueryFocusChange = remember { { viewModel.onQueryFocusChange(it) } },
        onQueryChange = remember { { viewModel.onQueryChange(it) } },
        onQueryClearClick = remember { { viewModel.onQueryClearClick() } },
        onSearchClick = remember { { viewModel.onSearchClick() } },
        sort = sort,
        onSortClick = remember { { viewModel.onSortClick() } },
        isFilterButtonEnabled = isFilterButtonEnabled,
        onFilterClick = remember { { viewModel.onFilterClick() } },
        isSearchHistoryVisible = isSearchHistoryVisible,
        searchHistory = searchHistory,
        onSearchHistoryClick = remember { { viewModel.onSearchHistoryClick(it) } },
        onSearchHistoryDeleteClick = remember { { viewModel.onSearchHistoryDeleteClick(it) } },
        isAutocompleteWordsVisible = isAutocompleteWordsVisible,
        isFrequentSearchVisible = isFrequentSearchVisible,
        autocomplete = autocomplete,
        onAutocompleteWordClick = remember { { viewModel.onAutocompleteWordClick(it) } },
        onFrequentlySearchedClick = remember { { viewModel.onFrequentlySearchedClick(it) } },
        resultsLazyGridState = resultsLazyGridState,
        products = products,
        onProductClick = remember { { viewModel.onProductClick(it) } },
        onFavoriteChange = remember {
            { product, isFavorite ->
                viewModel.onFavoriteChange(
                    product,
                    isFavorite
                )
            }
        },
        recommendations = recommendations,
        shakingFavorites = shakingFavorites,
    )
}

@Composable
fun SearchScreenBehavior(
    sideEffects: Flow<SearchViewModel.SideEffect>,
    showProduct: (Product) -> Unit,
    showSelectSort: () -> Unit,
    showFilters: () -> Unit,
    resultsLazyGridState: LazyGridState,
) {
    NavigationBarState(isVisible = false, isAnimated = false)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                is SearchViewModel.SideEffect.ShowProduct -> showProduct(effect.product)
                SearchViewModel.SideEffect.ScrollResultsToTop -> resultsLazyGridState.scrollToItem(0)
                SearchViewModel.SideEffect.ShowSelectSort -> showSelectSort()
                SearchViewModel.SideEffect.ShowFilters -> showFilters()
            }
        }
    }
}
