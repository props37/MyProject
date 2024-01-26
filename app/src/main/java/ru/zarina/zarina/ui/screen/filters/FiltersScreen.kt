package ru.zarina.zarina.ui.screen.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.component.PriceFilter
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.component.skeleton.Skeleton
import ru.zarina.zarina.ui.common.component.skeleton.rememberSkeletonShimmer
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.MultiSelectionFilterItem
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.SingleSelectionFilterItem
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.TopBarActions
import ru.zarina.zarina.ui.screen.filters.FiltersViewModel.FilterListState
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.Crossfade

@Composable
fun FiltersScreen(
    navigateBackward: (FiltersScreenResult) -> Unit,
    viewModel: FiltersViewModel = hiltViewModel(),
) {
    val filterListState by viewModel.filterListState.collectAsStateWithLifecycle()
    val topBarActions = remember(viewModel) {
        TopBarActions(
            onBackClicked = viewModel::onBackClicked,
            onResetClicked = { /* TODO */ },
        )
    }

    ScreenContent(
        filterListState = filterListState,
        topBarActions = topBarActions,
        sideEffects = viewModel.sideEffects,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    filterListState: FilterListState,
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

        // TODO: [High] Extract
        Crossfade(
            targetState = filterListState,
            contentKey = {
                // TODO: [High] Extract
                when (it) {
                    is FilterListState.FilterList -> "FilterList"
                    FilterListState.Loading, is FilterListState.Error -> it
                }
            },
            modifier = Modifier.fillMaxSize(),
        ) { state ->
            when (state) {
                is FilterListState.FilterList -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        if (state.filters.sorting != null) {
                            SingleSelectionFilterItem(
                                selected = remember(state.filters.sorting) {
                                    state.filters.sorting.selectedItems.firstOrNull()
                                },
                                onClick = { /* TODO */ },
                            )
                        }

                        if (state.filters.price != null) {
                            PriceFilter(
                                priceFilter = state.filters.price,
                                onPriceFilterChanged = { /* TODO */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                            )
                        }

                        if (state.filters.materials != null) {
                            MultiSelectionFilterItem(
                                title = stringResource(R.string.composition),
                                selectedCount = remember(state.filters.materials) {
                                    state.filters.materials.selectedItems.size
                                },
                                onClick = { /*TODO*/ },
                            )
                        }

                        if (state.filters.sizes != null) {
                            MultiSelectionFilterItem(
                                title = stringResource(R.string.size),
                                selectedCount = remember(state.filters.sizes) {
                                    state.filters.sizes.selectedItems.size
                                },
                                onClick = { /*TODO*/ },
                            )
                        }

                        if (state.filters.colors != null) {
                            MultiSelectionFilterItem(
                                title = stringResource(R.string.color),
                                selectedCount = remember(state.filters.colors) {
                                    state.filters.colors.selectedItems.size
                                },
                                onClick = { /*TODO*/ },
                            )
                        }
                    }
                }

                FilterListState.Loading -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        val shimmer = rememberSkeletonShimmer(ShimmerBounds.Window)
                        // TODO: [High] Extract
                        // TODO: [High] Do not hardcode
                        repeat(10) { index ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 56.dp) // TODO: [High] Extract
                                    .padding(horizontal = 16.dp),
                            ) {
                                val height = 16.dp
                                Skeleton(
                                    shimmer = shimmer,
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .fillMaxWidth(fraction = 0.55f)
                                        .height(height),
                                )

                                Skeleton(
                                    shimmer = shimmer,
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .size(height),
                                )
                            }

                            // TODO: [High] Do not hardcode
                            if (index < 10 - 1) {
                                Divider(
                                    color = UiKitTheme.colorsReworked.background.skeleton,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }
                }

                is FilterListState.Error -> {
                    ZarinaErrorScreen(
                        state = state.errorState,
                        onRefreshClicked = { /*TODO*/ },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
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

