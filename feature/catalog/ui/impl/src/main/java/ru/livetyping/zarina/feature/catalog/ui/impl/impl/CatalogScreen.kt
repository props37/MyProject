package ru.livetyping.zarina.feature.catalog.ui.impl.impl

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.core.uicompose.pager.rememberPagerStateWithTabRow
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.catalog.ui.CatalogNavActions
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.component.GenderCategoryPager
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.component.GenderSelector
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.component.SearchBar
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CategoryListEvent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CategoryListItemsState
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CategoryListState

@Composable
internal fun CatalogScreen(
    navActions: CatalogNavActions,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val genderSelectorState by viewModel.genderSelectorState.collectAsStateWithLifecycle()
    val categoryListState by viewModel.categoryListState.collectAsStateWithLifecycle()
    val categoryListItemsState by viewModel.categoryListItemsState.collectAsStateWithLifecycle()

    ScreenContent(
        genderSelectorState = genderSelectorState,
        onGenderSelectorEvent = viewModel::onGenderSelectorEvent,
        categoryListState = categoryListState,
        onCategoryListEvent = viewModel::onCategoryListEvent,
        categoryListItemsState = categoryListItemsState,
        onSearchBarClicked = viewModel::onSearchBarClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    genderSelectorState: TabRowState<GenderTab>,
    onGenderSelectorEvent: (TabRowEvent<GenderTab>) -> Unit,
    categoryListState: CategoryListState,
    onCategoryListEvent: (CategoryListEvent) -> Unit,
    categoryListItemsState: CategoryListItemsState,
    onSearchBarClicked: () -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<CatalogSideEffect>,
    navActions: CatalogNavActions,
) {
    CatalogScreenBehavior(
        onBackClicked = onBackClicked,
        sideEffects = sideEffects,
        navActions = navActions,
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
            val genderSelectorPagerState = rememberPagerStateWithTabRow(
                tabs = genderSelectorState.tabs,
                currentTab = genderSelectorState.currentTab,
                onTabChanged = { onGenderSelectorEvent(TabRowEvent.TabChanged(it)) },
                initialPage = remember { genderSelectorState.currentTabIndex },
                pageCount = { genderSelectorState.tabs.size },
            )

            GenderSelector(
                genderSelectorState = genderSelectorState,
                onGenderSelectorEvent = onGenderSelectorEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            GenderCategoryPager(
                genderSelectorState = genderSelectorState,
                categoryListState = categoryListState,
                onCategoryListEvent = onCategoryListEvent,
                categoryListItemsState = categoryListItemsState,
                pagerState = genderSelectorPagerState,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
