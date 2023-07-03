package ru.zarina.zarina.ui.screens.catalog.filters

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.toolbar.CloseButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreenContent(
    onCloseClick: () -> Unit,
) {
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(id = R.string.filters),
                endIcon = {
                    CloseButton(onClick = onCloseClick)
                }
            )
        }
    ) {

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
