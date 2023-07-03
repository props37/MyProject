package ru.zarina.zarina.ui.screens.catalog.selectsort

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import ru.zarina.zarina.domain.ProductSort
import ru.zarina.zarina.ui.common.components.SelectionCircle
import ru.zarina.zarina.ui.common.components.bottomsheet.Header
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.utils.domain.getStringResource
import ru.zarina.zarina.ui.theme.UiKitTheme
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
private fun SortItem(
    item: ProductSort,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(item.getStringResource()),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        SelectionCircle(isSelected = isSelected)
    }
}

@Composable
private fun SortDivider() {
    Divider(
        thickness = 1.dp,
        color = UiKitTheme.colors.listDivider,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
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

