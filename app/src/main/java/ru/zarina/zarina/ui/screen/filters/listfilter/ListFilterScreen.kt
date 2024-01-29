package ru.zarina.zarina.ui.screen.filters.listfilter

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.filter.ListFilter
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.common.util.domain.nameResId
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterScreenComponents.TopBarActions
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ListFilterScreen(
    viewModel: ListFilterViewModel = hiltViewModel(),
) {
    val filter by viewModel.filter.collectAsStateWithLifecycle()

    val topBarActions = remember(viewModel) {
        TopBarActions(
            onBackClicked = viewModel::onBackClicked,
            onResetClicked = viewModel::onResetClicked,
        )
    }

    ScreenContent(
        filter = filter,
        topBarActions = topBarActions,
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    filter: ListFilter<*>,
    topBarActions: TopBarActions,
    sideEffects: Flow<ListFilterViewModel.SideEffect>,
) {
    ListFilterScreenBehavior(sideEffects = sideEffects)

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
            title = stringResource(filter.type.nameResId),
            isResetButtonVisible = false, // TODO: [High] Implement
            actions = topBarActions,
        )
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
