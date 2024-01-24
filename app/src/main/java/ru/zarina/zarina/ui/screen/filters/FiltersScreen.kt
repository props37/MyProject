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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.TopBar
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun FiltersScreen(
    viewModel: FiltersViewModel = hiltViewModel(),
) {
    ScreenContent(
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    sideEffects: Flow<FiltersViewModel.SideEffect>,
) {
    FiltersScreenBehavior(sideEffects = sideEffects)

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
            onBackClicked = { /*TODO*/ },
            isResetButtonVisible = false, // TODO: [High] Implement
            onResetClicked = { /*TODO*/ },
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

