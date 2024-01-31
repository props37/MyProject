package ru.zarina.zarina.ui.screen.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.filter.Filter
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.FilterList
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.TopBarActions
import ru.zarina.zarina.ui.screen.filters.FiltersViewModel.FilterListState
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun FiltersScreen(
    navigateForward: (FiltersScreenAction) -> Unit,
    navigateBackward: (FiltersScreenResult) -> Unit,
    viewModel: FiltersViewModel = hiltViewModel(),
) {
    val filterListState by viewModel.filterListState.collectAsStateWithLifecycle()
    val productCount by viewModel.productCount.collectAsStateWithLifecycle()
    val topBarActions = remember(viewModel) {
        TopBarActions(
            onBackClicked = viewModel::onBackClicked,
            onResetClicked = { /* TODO */ },
        )
    }

    ScreenContent(
        filterListState = filterListState,
        topBarActions = topBarActions,
        onFilterChanged = viewModel::onFilterChanged,
        onFilterClicked = viewModel::onFilterClicked,
        productCount = productCount,
        onShowProductsClicked = viewModel::onShowProductsClicked,
        sideEffects = viewModel.sideEffects,
        navigateForward = navigateForward,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    filterListState: FilterListState,
    topBarActions: TopBarActions,
    onFilterChanged: (Filter) -> Unit,
    onFilterClicked: (Filter) -> Unit,
    productCount: Int?,
    onShowProductsClicked: () -> Unit,
    sideEffects: Flow<FiltersViewModel.SideEffect>,
    navigateForward: (FiltersScreenAction) -> Unit,
    navigateBackward: (FiltersScreenResult) -> Unit,
) {
    FiltersScreenBehavior(
        sideEffects = sideEffects,
        navigateForward = navigateForward,
        navigateBackward = navigateBackward,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsReworked.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.systemBars
                    .union(WindowInsets.displayCutout),
            ),
    ) {
        TopBar(
            isResetButtonVisible = false, // TODO: [High] Implement
            actions = topBarActions,
        )

        FilterList(
            state = filterListState,
            onFilterChanged = onFilterChanged,
            onFilterClicked = onFilterClicked,
            onShowProductsClicked = onShowProductsClicked,
            productCount = productCount,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@DensityPreviews
@FontScalePreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}

