package ru.livetyping.zarina.ui.screen.catalog

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.catalog.CatalogScreenComponents.GenderCategoryPager
import ru.livetyping.zarina.ui.screen.catalog.CatalogScreenComponents.GenderPicker
import ru.livetyping.zarina.ui.screen.catalog.CatalogScreenComponents.SearchBar
import ru.livetyping.zarina.ui.screen.catalog.CatalogViewModel.CategoryListItem
import ru.livetyping.zarina.ui.screen.catalog.CatalogViewModel.CategoryListItemsState
import ru.livetyping.zarina.ui.screen.catalog.CatalogViewModel.CategoryListState
import ru.livetyping.zarina.ui.screen.catalog.CatalogViewModel.GenderTab
import ru.livetyping.zarina.ui.screen.catalog.CatalogViewModel.SideEffect
import ru.livetyping.zarina.ui.screen.catalog.tooling.preview.CategoryListStatePreviewParameterProvider
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.util.compose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.util.compose.pager.PagerTabRowIntegration

@Composable
fun CatalogScreen(
    navigate: (CatalogScreenAction) -> Unit,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate, // TODO: [Low] remove after migration to BasicTextField2
    )
    val genderTabs by viewModel.genderTabs.collectAsStateWithLifecycle()
    val currentGenderTab by viewModel.currentGenderTab.collectAsStateWithLifecycle()
    val categoryListState by viewModel.categoryListState.collectAsStateWithLifecycle()
    val categoryListItemsState by viewModel.categoryListItemsState.collectAsStateWithLifecycle()

    ScreenContent(
        searchQuery = searchQuery,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSearchBarCancelClicked = viewModel::onSearchBarCancelClicked,
        genderTabs = genderTabs,
        currentGenderTab = currentGenderTab,
        onGenderTabChanged = viewModel::onGenderTabChanged,
        categoryListState = categoryListState,
        categoryListItemsState = categoryListItemsState,
        onCategoryListItemClicked = viewModel::onCategoryListItemClicked,
        onCategoryListErrorRefreshClicked = viewModel::onCategoryListErrorRefreshClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScreenContent(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchBarCancelClicked: () -> Unit,
    genderTabs: ImmutableList<GenderTab>,
    currentGenderTab: GenderTab,
    onGenderTabChanged: (GenderTab) -> Unit,
    categoryListState: CategoryListState,
    categoryListItemsState: CategoryListItemsState,
    onCategoryListItemClicked: (CategoryListItem) -> Unit,
    onCategoryListErrorRefreshClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CatalogScreenAction) -> Unit,
) {
    CatalogScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    val topBarScrollBehavior = CollapsingTopBarDefaults.rememberEnterAlwaysScrollBehavior()
    CollapsingTopBarLayout(
        topBar = {
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged,
                onCancelClicked = onSearchBarCancelClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 4.dp),
            )
        },
        scrollBehavior = topBarScrollBehavior,
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .imePadding()
            .bottomNavBarPadding(WindowInsets.ime)
            .clipToBounds(),
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
        ) {
            val pagerState = rememberPagerState(
                initialPage = remember { genderTabs.indexOf(currentGenderTab) },
                pageCount = { genderTabs.size },
            )

            PagerTabRowIntegration(
                pagerState = pagerState,
                tabs = genderTabs,
                currentTab = currentGenderTab,
                onCurrentTabChanged = onGenderTabChanged,
            )

            GenderPicker(
                genders = genderTabs,
                pagerState = pagerState,
                onGenderСhanged = onGenderTabChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            GenderCategoryPager(
                genders = genderTabs,
                pagerState = pagerState,
                categoryListState = categoryListState,
                categoryListItemsState = categoryListItemsState,
                onCategoryListItemClicked = onCategoryListItemClicked,
                onCategoryListErrorRefreshClicked = onCategoryListErrorRefreshClicked,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview(
    @PreviewParameter(CategoryListStatePreviewParameterProvider::class)
    categoryListState: CategoryListState,
) {
    ZarinaPreview {
        ScreenContent(
            searchQuery = "",
            onSearchQueryChanged = {},
            onSearchBarCancelClicked = {},
            genderTabs = remember { GenderTab.entries.toImmutableList() },
            currentGenderTab = GenderTab.WOMEN,
            onGenderTabChanged = {},
            categoryListState = categoryListState,
            categoryListItemsState = remember {
                CategoryListStatePreviewParameterProvider.getCategoryListItemsStatePreview()
            },
            onCategoryListItemClicked = {},
            onCategoryListErrorRefreshClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
