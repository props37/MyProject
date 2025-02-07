package ru.livetyping.zarina.presentation.screen.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.catalog.CatalogScreenComponents.GenderCategoryPager
import ru.livetyping.zarina.presentation.screen.catalog.CatalogScreenComponents.GenderPicker
import ru.livetyping.zarina.presentation.screen.catalog.CatalogScreenComponents.SearchBar
import ru.livetyping.zarina.presentation.screen.catalog.CatalogViewModel.CategoryListItem
import ru.livetyping.zarina.presentation.screen.catalog.CatalogViewModel.CategoryListItemsState
import ru.livetyping.zarina.presentation.screen.catalog.CatalogViewModel.CategoryListState
import ru.livetyping.zarina.presentation.screen.catalog.CatalogViewModel.GenderTab
import ru.livetyping.zarina.presentation.screen.catalog.CatalogViewModel.SideEffect
import ru.livetyping.zarina.presentation.screen.catalog.tooling.preview.CategoryListStatePreviewParameterProvider
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.util.compose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.util.compose.pager.PagerTabRowIntegration

@Composable
fun CatalogScreen(
    navigate: (CatalogScreenAction) -> Unit,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val genderTabs by viewModel.genderTabs.collectAsStateWithLifecycle()
    val currentGenderTab by viewModel.currentGenderTab.collectAsStateWithLifecycle()
    val categoryListState by viewModel.categoryListState.collectAsStateWithLifecycle()
    val categoryListItemsState by viewModel.categoryListItemsState.collectAsStateWithLifecycle()

    ScreenContent(
        onSearchBarClicked = viewModel::onSearchBarClicked,
        genderTabs = genderTabs,
        currentGenderTab = currentGenderTab,
        onGenderTabChanged = viewModel::onGenderTabChanged,
        categoryListState = categoryListState,
        categoryListItemsState = categoryListItemsState,
        onCategoryListItemClicked = viewModel::onCategoryListItemClicked,
        onCategoryListErrorRefreshClicked = viewModel::onCategoryListErrorRefreshClicked,
        onScreenCreated = viewModel::onScreenCreated,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    onSearchBarClicked: () -> Unit,
    genderTabs: ImmutableList<GenderTab>,
    currentGenderTab: GenderTab,
    onGenderTabChanged: (GenderTab) -> Unit,
    categoryListState: CategoryListState,
    categoryListItemsState: CategoryListItemsState,
    onCategoryListItemClicked: (CategoryListItem) -> Unit,
    onCategoryListErrorRefreshClicked: () -> Unit,
    onScreenCreated: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CatalogScreenAction) -> Unit,
) {
    CatalogScreenBehavior(
        onScreenCreated = onScreenCreated,
        sideEffects = sideEffects,
        navigate = navigate,
    )

    val topBarScrollBehavior = CollapsingTopBarDefaults.rememberEnterAlwaysScrollBehavior()
    CollapsingTopBarLayout(
        topBar = {
            SearchBar(
                onClick = onSearchBarClicked,
                modifier = Modifier
                    .fillMaxWidth()
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
            .bottomNavBarPadding()
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
                onGenderChanged = onGenderTabChanged,
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
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview(
    @PreviewParameter(CategoryListStatePreviewParameterProvider::class)
    categoryListState: CategoryListState,
) {
    ZarinaPreview {
        ScreenContent(
            onSearchBarClicked = {},
            genderTabs = remember { GenderTab.entries.toImmutableList() },
            currentGenderTab = GenderTab.WOMEN,
            onGenderTabChanged = {},
            categoryListState = categoryListState,
            categoryListItemsState = remember {
                CategoryListStatePreviewParameterProvider.getCategoryListItemsStatePreview()
            },
            onCategoryListItemClicked = {},
            onCategoryListErrorRefreshClicked = {},
            onScreenCreated = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
