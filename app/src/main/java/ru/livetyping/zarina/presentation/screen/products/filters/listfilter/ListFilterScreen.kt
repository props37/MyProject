package ru.livetyping.zarina.presentation.screen.products.filters.listfilter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.filter.ListFilter
import ru.livetyping.zarina.domain.filter.ListFilterItem
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.common.util.domain.nameResId
import ru.livetyping.zarina.presentation.screen.products.filters.listfilter.ListFilterScreenComponents.ApplyButton
import ru.livetyping.zarina.presentation.screen.products.filters.listfilter.ListFilterScreenComponents.FilterItems
import ru.livetyping.zarina.presentation.screen.products.filters.listfilter.ListFilterScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.products.filters.listfilter.ListFilterScreenComponents.TopBarActions
import ru.livetyping.zarina.presentation.screen.products.filters.listfilter.ListFilterViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.plus

@Composable
fun ListFilterScreen(
    navigateBackward: (ListFilterScreenResult) -> Unit,
    viewModel: ListFilterViewModel = hiltViewModel(),
) {
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val isResetButtonVisible by viewModel.isResetButtonVisible.collectAsStateWithLifecycle()
    val isApplyButtonVisible by viewModel.isApplyButtonVisible.collectAsStateWithLifecycle()
    val city by viewModel.city.collectAsStateWithLifecycle()

    val topBarActions = remember(viewModel) {
        TopBarActions(
            onBackClicked = viewModel::onBackClicked,
            onResetClicked = viewModel::onResetClicked,
        )
    }

    ScreenContent(
        filter = filter,
        isResetButtonVisible = isResetButtonVisible,
        isApplyButtonVisible = isApplyButtonVisible,
        city = city,
        topBarActions = topBarActions,
        onItemClicked = viewModel::onItemClicked,
        onApplyClicked = viewModel::onApplyClicked,
        sideEffects = viewModel.sideEffects,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    filter: ListFilter<ListFilterItem>,
    isResetButtonVisible: Boolean,
    isApplyButtonVisible: Boolean,
    city: City?,
    topBarActions: TopBarActions,
    onItemClicked: (ListFilterItem) -> Unit,
    onApplyClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigateBackward: (ListFilterScreenResult) -> Unit,
) {
    ListFilterScreenBehavior(
        sideEffects = sideEffects,
        navigateBackward = navigateBackward,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            ),
    ) {
        TopBar(
            title = stringResource(filter.type.nameResId),
            isResetButtonVisible = isResetButtonVisible,
            actions = topBarActions,
        )

        val contentPadding = if (!isApplyButtonVisible) {
            WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom).asPaddingValues()
        } else {
            PaddingValues()
        }.plus(PaddingValues(bottom = 24.dp))

        FilterItems(
            filter = filter,
            onItemClicked = onItemClicked,
            city = city,
            contentPadding = contentPadding,
            modifier = Modifier.weight(1f),
        )

        ApplyButton(
            onClick = onApplyClicked,
            isVisible = isApplyButtonVisible,
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(
                    WindowInsets.navigationBars
                        .union(WindowInsets.displayCutout)
                ),
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
