package ru.zarina.zarina.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun HomeScreenContent() {

}

@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()

    HomeScreenBehavior(
        sideEffects = viewModel.sideEffects,
    )

    HomeScreenContent()
}

@Composable
fun HomeScreenBehavior(
    sideEffects: Flow<HomeViewModel.SideEffect>,
) {
    NavigationBarState(isVisible = true, isAnimated = true)
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> Unit
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
