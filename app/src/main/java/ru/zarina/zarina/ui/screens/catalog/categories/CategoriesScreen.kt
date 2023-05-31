package ru.zarina.zarina.ui.screens.catalog.categories

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreenContent() {
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(id = R.string.catalog)
            )
        }
    ) {

    }
}

@Composable
fun CategoriesScreen() {
    val viewModel = hiltViewModel<CategoriesViewModel>()

    CategoriesScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    CategoriesScreenContent()
}

@Composable
fun CategoriesScreenBehavior(
    sideEffects: Flow<CategoriesViewModel.SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun CategoriesScreenContentPreview() {
    ZarinaTheme {
        CategoriesScreenContent()
    }
}
