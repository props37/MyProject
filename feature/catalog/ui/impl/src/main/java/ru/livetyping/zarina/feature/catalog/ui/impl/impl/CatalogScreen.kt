package ru.livetyping.zarina.feature.catalog.ui.impl.impl

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CatalogEvent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CatalogState
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.ui.SearchButton
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.ui.TopBar

@Composable
internal fun CatalogScreen(
    navActions: CatalogFeature.NavActions,
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    val catalogState by viewModel.catalogState.collectAsStateWithLifecycle()

    BackHandler(onBack = { viewModel.onCatalogEvent(CatalogEvent.BackClicked) })

    ScreenContent(
        catalogState = catalogState,
        onCatalogEvent = viewModel::onCatalogEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    catalogState: CatalogState,
    onCatalogEvent: (CatalogEvent) -> Unit,
    sideEffects: Flow<CatalogSideEffect>,
    navActions: CatalogFeature.NavActions,
) {
    CatalogScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    val topBarScrollBehavior = CollapsingTopBarDefaults.rememberExitUntilCollapsedScrollBehavior()

    CollapsingTopBarLayout(
        topBar = {
            TopBar(
                genderPickerState = catalogState.genderPickerState,
                onGenderSelected = { onCatalogEvent(CatalogEvent.GenderSelected(it)) },
                modifier = Modifier
                    .graphicsLayer {
                        alpha = 1f - topBarScrollBehavior.state.collapsedFraction
                    },
            )
        },
        scrollBehavior = topBarScrollBehavior,
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme2.colors.white)
            .statusBarsPadding()
            .displayCutoutPadding(),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            SearchButton(
                onClick = { onCatalogEvent(CatalogEvent.SearchClicked) },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            // TODO: [Top] Implement
        }
    }
}
