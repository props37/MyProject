package ru.zarina.zarina.ui.screens.catalog.selectshop

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.toolbar.BackButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectShopScreenContent(
    onBackClick: () -> Unit,
) {
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(id = R.string.selection_of_store),
                startIcon = {
                    BackButton(onClick = onBackClick)
                }
            )
        }
    ) {

    }
}

@Composable
fun SelectShopScreen(
    filtersSavedStateHandle: SavedStateHandle,
    goBack: () -> Unit
) {
    val viewModel = koinViewModel<SelectShopViewModel> { parametersOf(filtersSavedStateHandle) }

    SelectShopScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
    )

    SelectShopScreenContent(
        onBackClick = viewModel::onBackClick
    )
}

@Composable
fun SelectShopScreenBehavior(
    sideEffects: Flow<SelectShopViewModel.SideEffect>,
    goBack: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                SelectShopViewModel.SideEffect.GoBack -> goBack()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun SelectShopScreenContentPreview() {
    ZarinaTheme {
        SelectShopScreenContent(
            onBackClick = {}
        )
    }
}

