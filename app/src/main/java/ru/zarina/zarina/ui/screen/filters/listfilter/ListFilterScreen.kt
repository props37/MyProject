package ru.zarina.zarina.ui.screen.filters.listfilter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.filter.ColorFilterItem
import ru.zarina.zarina.domain.rework.filter.ListFilter
import ru.zarina.zarina.domain.rework.filter.ListFilterItem
import ru.zarina.zarina.domain.rework.filter.SortFilterItem
import ru.zarina.zarina.domain.rework.filter.sorting
import ru.zarina.zarina.ui.common.component.ColorIcon
import ru.zarina.zarina.ui.common.component.icon.CheckmarkAnimatedIcon
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.common.util.domain.nameResId
import ru.zarina.zarina.ui.common.util.domain.toComposeColor
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterScreenComponents.TopBarActions
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ListFilterScreen(
    navigateBackward: (ListFilterScreenResult) -> Unit,
    viewModel: ListFilterViewModel = hiltViewModel(),
) {
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val isResetButtonVisible by viewModel.isResetButtonVisible.collectAsStateWithLifecycle()

    val topBarActions = remember(viewModel) {
        TopBarActions(
            onBackClicked = viewModel::onBackClicked,
            onResetClicked = viewModel::onResetClicked,
        )
    }

    ScreenContent(
        filter = filter,
        isResetButtonVisible = isResetButtonVisible,
        topBarActions = topBarActions,
        onItemClicked = viewModel::onItemClicked,
        sideEffects = viewModel.sideEffects,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    filter: ListFilter<ListFilterItem>,
    isResetButtonVisible: Boolean,
    topBarActions: TopBarActions,
    onItemClicked: (ListFilterItem) -> Unit,
    sideEffects: Flow<ListFilterViewModel.SideEffect>,
    navigateBackward: (ListFilterScreenResult) -> Unit,
) {
    ListFilterScreenBehavior(
        sideEffects = sideEffects,
        navigateBackward = navigateBackward,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsReworked.background.general.regular.default)
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

        // TODO: [High] Extract
        val contentPadding =
            WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom).asPaddingValues()
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize(),
        ) {
            itemsIndexed(
                items = filter.items,
                key = { _, item -> item.id.value },
            ) { index, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .clickable { onItemClicked(item) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    if (item is ColorFilterItem) {
                        ColorIcon(
                            color = item.color.toComposeColor(),
                            size = 20.dp,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    val name = if (item is SortFilterItem) {
                        stringResource(item.sorting.nameResId)
                    } else {
                        item.name
                    }

                    Text(
                        text = name,
                        style = UiKitTheme.typographyReworked.secondary.light,
                        color = UiKitTheme.colorsReworked.text.general.regular.default,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(16.dp))

                    CheckmarkAnimatedIcon(
                        isVisible = item.isSelected,
                        iconSize = 16.dp,
                        modifier = Modifier.padding(start = if (item.isSelected) 16.dp else 0.dp),
                    )
                }

                if (index < filter.items.size - 1) {
                    Divider(
                        color = UiKitTheme.colorsReworked.background.skeleton,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
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
