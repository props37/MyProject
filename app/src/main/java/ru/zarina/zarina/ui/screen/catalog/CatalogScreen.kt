package ru.zarina.zarina.ui.screen.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.common.Category
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.catalog.CatalogScreenComponents.GenderCategoryPager
import ru.zarina.zarina.ui.screen.catalog.CatalogScreenComponents.GenderPicker
import ru.zarina.zarina.ui.screen.catalog.CatalogScreenComponents.SearchBar
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel.CategoryListState
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel.GenderTab
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel.SideEffect
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val genderTabs by viewModel.genderTabs.collectAsStateWithLifecycle()
    val currentGenderTab by viewModel.currentGenderTab.collectAsStateWithLifecycle()
    val categoryListState by viewModel.categoryListState.collectAsStateWithLifecycle()

    ScreenContent(
        searchQuery = searchQuery,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSearchBarClearClicked = viewModel::onSearchBarClearClicked,
        onSearchBarCancelClicked = viewModel::onSearchBarCancelClicked,
        genderTabs = genderTabs,
        currentGenderTab = currentGenderTab,
        onGenderTabClicked = viewModel::onGenderTabClicked,
        categoryListState = categoryListState,
        onCategoryClicked = viewModel::onCategoryClicked,
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchBarClearClicked: () -> Unit,
    onSearchBarCancelClicked: () -> Unit,
    genderTabs: List<GenderTab>,
    currentGenderTab: GenderTab,
    onGenderTabClicked: (GenderTab) -> Unit,
    categoryListState: CategoryListState,
    onCategoryClicked: (Category) -> Unit,
    sideEffects: Flow<SideEffect>,
) {
    CatalogScreenBehavior(
        sideEffects = sideEffects,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsReworked.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout)
                    .only(WindowInsetsSides.Top),
            )
            .imePadding()
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            onClearClicked = onSearchBarClearClicked,
            onCancelClicked = onSearchBarCancelClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(4.dp))

        GenderPicker(
            genders = genderTabs,
            currentGender = currentGenderTab,
            onGenderClicked = onGenderTabClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        GenderCategoryPager(
            genders = genderTabs,
            currentGender = currentGenderTab,
            categoryListState = categoryListState,
            onCategoryClicked = onCategoryClicked,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [High] Add preview
    }
}
