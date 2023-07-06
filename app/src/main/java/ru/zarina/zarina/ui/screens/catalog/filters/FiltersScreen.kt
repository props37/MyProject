package ru.zarina.zarina.ui.screens.catalog.filters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.PriceRange
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.common.components.toolbar.CloseButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.screens.catalog.filters.components.items.PriceItem
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreenContent(
    filtration: Filtration?,
    onPriceChange: (min: Int, max: Int) -> Unit,
    filterButtonMode: FiltersViewModel.FilterButtonMode,
    onCloseClick: () -> Unit,
    onFilterButtonClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(id = R.string.filters),
                endIcon = {
                    CloseButton(onClick = onCloseClick)
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
                    Divider(
                        thickness = 1.dp,
                        color = UiKitTheme.colors.listDivider,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            FilterButton(
                mode = filterButtonMode,
                onClick = onFilterButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun FilterButton(
    mode: FiltersViewModel.FilterButtonMode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = stringResource(
        when (mode) {
            FiltersViewModel.FilterButtonMode.APPLY -> R.string.apply
            FiltersViewModel.FilterButtonMode.CLOSE -> R.string.close
        }
    )
    ZarinaTextButton(
        text = text,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun FiltersScreen(
    productsSavedStateHandle: SavedStateHandle,
    goBack: () -> Unit,
) {
    val viewModel = koinViewModel<FiltersViewModel> { parametersOf(productsSavedStateHandle) }

    val filtration by viewModel.newFiltration.collectAsStateWithLifecycle()
    val filterButtonMode by viewModel.filterButtonMode.collectAsStateWithLifecycle()

    FiltersScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack
    )

    FiltersScreenContent(
        filtration = filtration,
        onPriceChange = viewModel::onPriceChange,
        onCloseClick = viewModel::onCloseClick,
        filterButtonMode = filterButtonMode,
        onFilterButtonClick = viewModel::onFilterButtonClick
    )
}

@Composable
fun FiltersScreenBehavior(
    sideEffects: Flow<FiltersViewModel.SideEffect>,
    goBack: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                FiltersViewModel.SideEffect.GoBack -> goBack()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun FiltersScreenContentPreview() {
    ZarinaTheme {
        FiltersScreenContent(
            filtration = Filtration(
                priceLimits = PriceRange(
                    min = 200,
                    max = 4999
                ),
            ),
            onPriceChange = { _, _ -> },
            onCloseClick = {},
            filterButtonMode = FiltersViewModel.FilterButtonMode.CLOSE,
            onFilterButtonClick = {},
        )
    }
}
