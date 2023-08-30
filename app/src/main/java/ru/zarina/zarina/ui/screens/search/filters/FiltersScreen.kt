package ru.zarina.zarina.ui.screens.search.filters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.ui.common.components.FilterButtonMode
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.bottomNavigationPadding
import ru.zarina.zarina.ui.common.components.filters.ClearButton
import ru.zarina.zarina.ui.common.components.filters.FilterButton
import ru.zarina.zarina.ui.common.components.filters.FiltersDivider
import ru.zarina.zarina.ui.common.components.toolbar.CloseButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.screens.catalog.filters.FilterType
import ru.zarina.zarina.ui.screens.catalog.filters.components.items.ListItem
import ru.zarina.zarina.ui.screens.catalog.filters.components.items.PriceItem
import ru.zarina.zarina.ui.screens.catalog.filters.components.items.TreeItem
import ru.zarina.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreenContent(
    filtration: Filtration?,
    isClearButtonVisible: Boolean,
    onClearClick: () -> Unit,
    onFilterClick: (FilterType) -> Unit,
    onPriceChange: (min: Int, max: Int) -> Unit,
    filterButtonMode: FilterButtonMode,
    onCloseClick: () -> Unit,
    onFilterButtonClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(id = R.string.filters),
                startIcon = {
                    CloseButton(onClick = onCloseClick)
                },
                endIcon = {
                    AnimatedVisibility(
                        visible = isClearButtonVisible,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        ClearButton(onClick = onClearClick)
                    }
                },
                isElevated = scrollState.canScrollBackward,
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                if (filtration?.price != null && filtration.priceLimits != null) {
                    PriceItem(
                        minValue = filtration.priceLimits.min,
                        maxValue = filtration.priceLimits.max,
                        selectedMinValue = filtration.price.min,
                        selectedMaxValue = filtration.price.max,
                        onSelectedValueChange = onPriceChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                    FiltersDivider()
                }
                if (filtration?.categories != null) {
                    TreeItem(
                        filterName = stringResource(id = R.string.categories),
                        treeFilter = filtration.categories,
                        onClick = { onFilterClick(FilterType.CATEGORY) },
                    )
                    FiltersDivider()
                }
                if (filtration?.attributes != null) {
                    ListItem(
                        filterName = stringResource(id = R.string.attributes),
                        items = filtration.attributes.items.toPersistentList(),
                        onClick = { onFilterClick(FilterType.ATTRIBUTES) },
                    )
                    FiltersDivider()
                }
                if (filtration?.materials != null) {
                    ListItem(
                        filterName = stringResource(id = R.string.materials),
                        items = filtration.materials.items.toPersistentList(),
                        onClick = { onFilterClick(FilterType.MATERIALS) },
                    )
                    FiltersDivider()
                }
                if (filtration?.sizes != null) {
                    ListItem(
                        filterName = stringResource(id = R.string.size),
                        items = filtration.sizes.items.toPersistentList(),
                        onClick = { onFilterClick(FilterType.SIZE) },
                    )
                    FiltersDivider()
                }
                if (filtration?.colors != null) {
                    ListItem(
                        filterName = stringResource(id = R.string.color),
                        items = filtration.colors.items.toPersistentList(),
                        onClick = { onFilterClick(FilterType.COLOR) },
                    )
                    FiltersDivider()
                }
            }
            val elevation = if (scrollState.canScrollForward) 6.dp else 0.dp
            FilterButton(
                mode = filterButtonMode,
                onClick = onFilterButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation)
                    .background(UiKitTheme.colors.screenBackground)
                    .padding(16.dp)
                    .navigationBarsPadding()
                    .bottomNavigationPadding()
            )
        }
    }
}

@Composable
fun FiltersScreen(
    savedStateHandle: SavedStateHandle,
    searchSavedStateHandle: SavedStateHandle,
    showListFilter: (FilterType) -> Unit,
    showTreeFilter: (FilterType) -> Unit,
    goBack: () -> Unit,
) {
    val viewModel =
        koinViewModel<FiltersViewModel> { parametersOf(savedStateHandle, searchSavedStateHandle) }

    val filtration by viewModel.newFiltration.collectAsStateWithLifecycle()
    val isClearButtonVisible by viewModel.isClearButtonVisible.collectAsStateWithLifecycle()
    val filterButtonMode by viewModel.filterButtonMode.collectAsStateWithLifecycle()

    FiltersScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showListFilter = showListFilter,
        showTreeFilter = showTreeFilter,
        goBack = goBack,
    )

    FiltersScreenContent(
        filtration = filtration,
        isClearButtonVisible = isClearButtonVisible,
        onClearClick = viewModel::onClearClick,
        onPriceChange = viewModel::onPriceChange,
        onFilterClick = viewModel::onFilterClick,
        onCloseClick = viewModel::onCloseClick,
        filterButtonMode = filterButtonMode,
        onFilterButtonClick = viewModel::onFilterButtonClick,
    )
}

@Composable
fun FiltersScreenBehavior(
    sideEffects: Flow<FiltersViewModel.SideEffect>,
    showListFilter: (FilterType) -> Unit,
    showTreeFilter: (FilterType) -> Unit,
    goBack: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                is FiltersViewModel.SideEffect.ShowListFilter -> showListFilter(effect.type)
                is FiltersViewModel.SideEffect.ShowTreeFilter -> showTreeFilter(effect.type)
                FiltersViewModel.SideEffect.GoBack -> goBack()
            }
        }
    }
}
