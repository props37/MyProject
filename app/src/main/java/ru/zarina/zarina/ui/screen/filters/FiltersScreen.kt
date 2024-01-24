package ru.zarina.zarina.ui.screen.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.TopBarActions
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun FiltersScreen(
    navigateBackward: (FiltersScreenResult) -> Unit,
    viewModel: FiltersViewModel = hiltViewModel(),
) {
    val topBarActions = remember(viewModel) {
        TopBarActions(
            onBackClicked = viewModel::onBackClicked,
            onResetClicked = { /* TODO */ },
        )
    }

    ScreenContent(
        topBarActions = topBarActions,
        sideEffects = viewModel.sideEffects,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    topBarActions: TopBarActions,
    sideEffects: Flow<FiltersViewModel.SideEffect>,
    navigateBackward: (FiltersScreenResult) -> Unit,
) {
    FiltersScreenBehavior(
        sideEffects = sideEffects,
        navigateBackward = navigateBackward,
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
            .bottomNavBarPadding(),
    ) {
        TopBar(
            isResetButtonVisible = false, // TODO: [High] Implement
            actions = topBarActions,
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

