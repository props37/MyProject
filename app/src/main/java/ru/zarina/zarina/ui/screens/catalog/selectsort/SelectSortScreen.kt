package ru.zarina.zarina.ui.screens.catalog.selectsort

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.old.ProductSort
import ru.zarina.zarina.ui.common.components.SortDivider
import ru.zarina.zarina.ui.common.components.SortItem
import ru.zarina.zarina.ui.common.components.bottomsheet.Header
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun SelectSortScreenContent(
    options: ImmutableList<ProductSort>,
    selectedOption: ProductSort?,
    onOptionClick: (ProductSort) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(bottom = 16.dp),
    ) {
        Header(
            text = stringResource(R.string.sorting),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        SortDivider()
        options.forEach { option ->
            SortItem(
                item = option,
                isSelected = option == selectedOption,
                onClick = { onOptionClick(option) },
                modifier = Modifier.fillMaxWidth()
            )
            SortDivider()
        }
    }
}

@Composable
fun SelectSortScreen(
    productSavedStateHandle: SavedStateHandle,
    goBack: () -> Unit,
) {
    val viewModel = koinViewModel<SelectSortViewModel> { parametersOf(productSavedStateHandle) }

    val options by viewModel.options.collectAsStateWithLifecycle()
    val selectedOption by viewModel.selectedOption.collectAsStateWithLifecycle()

    SelectSortScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
    )

    SelectSortScreenContent(
        options = options,
        selectedOption = selectedOption,
        onOptionClick = viewModel::onOptionClick,
    )
}

@Composable
fun SelectSortScreenBehavior(
    sideEffects: Flow<SelectSortViewModel.SideEffect>,
    goBack: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                is SelectSortViewModel.SideEffect.GoBack -> goBack()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun SelectSortScreenContentPreview() {
    ZarinaTheme {
        SelectSortScreenContent(
            options = ProductSort.values().toList().toPersistentList(),
            selectedOption = ProductSort.values().first(),
            onOptionClick = {},
        )
    }
}

