package ru.zarina.zarina.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun HomeScreenContent() {

}

@Composable
fun HomeScreen() {
    val viewModel = hiltViewModel<HomeViewModel>()

    HomeScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    HomeScreenContent()
}

@Composable
fun HomeScreenBehavior(
    sideEffects: Flow<HomeViewModel.SideEffect>,
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
fun HomeScreenContentPreview() {
    ZarinaTheme {
        HomeScreenContent()
    }
}
