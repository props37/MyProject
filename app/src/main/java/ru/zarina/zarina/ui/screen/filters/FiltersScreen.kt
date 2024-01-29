package ru.zarina.zarina.ui.screen.filters

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.filter.Filter
import ru.zarina.zarina.domain.rework.filter.ListFilter
import ru.zarina.zarina.domain.rework.filter.PriceFilter
import ru.zarina.zarina.domain.rework.filter.ToggleFilter
import ru.zarina.zarina.ui.common.component.PriceFilter
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.component.skeleton.Skeleton
import ru.zarina.zarina.ui.common.component.skeleton.rememberSkeletonShimmer
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.MultiSelectionFilterItem
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.SingleSelectionFilterItem
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.ToggleFilterItem
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.filters.FiltersScreenComponents.TopBarActions
import ru.zarina.zarina.ui.screen.filters.FiltersViewModel.FilterListState
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultTransitionSpec
import ru.zarina.zarina.util.compose.Crossfade

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
        onFilterChanged = viewModel::onFilterChanged,
        onFilterClicked = viewModel::onFilterClicked,
        productCount = productCount,
        topBarActions = topBarActions,
        sideEffects = viewModel.sideEffects,
        navigateForward = navigateForward,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    filterListState: FilterListState,
    onFilterChanged: (Filter) -> Unit,
    onFilterClicked: (Filter) -> Unit,
    productCount: Int?,
    topBarActions: TopBarActions,
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
                    Column(modifier = Modifier.fillMaxSize()) {
                        val scrollState = rememberScrollState()
                        val isDragged = scrollState.interactionSource.collectIsDraggedAsState()
                        val keyboardController = LocalSoftwareKeyboardController.current

                        LaunchedEffect(scrollState, keyboardController) {
                            snapshotFlow { isDragged.value }
                                .collect { keyboardController?.hide() }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .verticalScroll(scrollState),
                        ) {
                            state.filters.forEachIndexed { index, filter ->
                                key(filter.type) {
                                    when (filter) {
                                        is PriceFilter -> {
                                            PriceFilter(
                                                priceFilter = filter,
                                                onPriceFilterChanged = { onFilterChanged(it) },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        start = 16.dp,
                                                        top = 16.dp,
                                                        end = 16.dp,
                                                        bottom = 8.dp,
                                                    ),
                                            )
                                        }

                                        is ListFilter<*> -> {
                                            if (filter.isSingleSelection) {
                                                SingleSelectionFilterItem(
                                                    type = filter.type,
                                                    selected = remember(filter.selectedItems) {
                                                        filter.selectedItems.firstOrNull()
                                                    },
                                                    onClick = { onFilterClicked(filter) },
                                                )
                                            } else {
                                                MultiSelectionFilterItem(
                                                    type = filter.type,
                                                    selectedCount = remember(filter.selectedItems) {
                                                        filter.selectedItems.size
                                                    },
                                                    onClick = { onFilterClicked(filter) },
                                                )
                                            }
                                        }

                                        is ToggleFilter -> {
                                            ToggleFilterItem(
                                                type = filter.type,
                                                isChecked = filter.isEnabled,
                                                onCheckedChanged = {
                                                    val updatedFilter = filter.copy(isEnabled = it)
                                                    onFilterChanged(updatedFilter)
                                                },
                                            )
                                        }
                                    }

                                    val filterCount = remember(state.filters) {
                                        state.filters.iterator().asSequence().count()
                                    }
                                    if (filter !is PriceFilter && index < filterCount - 1) {
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

                        Divider(
                            color = UiKitTheme.colorsReworked.background.skeleton,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        // TODO: [High] Extract
                        ZarinaButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        ) {
                            AnimatedContent(
                                targetState = productCount,
                                transitionSpec = {
                                    AnimatedContentDefaultTransitionSpec().using(sizeTransform = null)
                                },
                                contentAlignment = Alignment.Center,
                                label = "Show Products button",
                            ) { productCount ->
                                val text = if (productCount != null && productCount > 0) {
                                    pluralStringResource(
                                        R.plurals.show_products,
                                        productCount,
                                        productCount,
                                    )
                                } else {
                                    stringResource(R.string.show_products)
                                }

                                Text(text = text.uppercase())
                            }
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

