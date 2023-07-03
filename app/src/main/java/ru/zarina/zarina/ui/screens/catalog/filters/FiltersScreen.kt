package ru.zarina.zarina.ui.screens.catalog.filters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
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
    onCloseClick: () -> Unit,
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
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            // TODO actual values
            var minValue by remember { mutableStateOf(200) }
            var maxValue by remember { mutableStateOf(4999) }
            PriceItem(
                minValue = 200,
                maxValue = 4999,
                selectedMinValue = minValue,
                selectedMaxValue = maxValue,
                onSelectedValueChange = { min, max ->
                    minValue = min
                    maxValue = max
                },
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
}

@Composable
fun FiltersScreen(
    goBack: () -> Unit,
) {
    val viewModel = koinViewModel<FiltersViewModel>()

    FiltersScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack
    )

    FiltersScreenContent(
        onCloseClick = viewModel::onCloseClick
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
            onCloseClick = {}
        )
    }
}
