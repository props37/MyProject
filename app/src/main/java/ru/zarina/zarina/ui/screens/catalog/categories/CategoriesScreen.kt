package ru.zarina.zarina.ui.screens.catalog.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun CategoriesScreenContent() {

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
