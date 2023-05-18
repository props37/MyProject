package ru.zarina.zarina.ui.screens.pickup.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun DetailsScreenContent() {

}

@Composable
fun DetailsScreen(
    parentEntry: NavBackStackEntry,
) {
    val viewModel = hiltViewModel<DetailsViewModel>()

    DetailsScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    DetailsScreenContent()
}

@Composable
fun DetailsScreenBehavior(
    sideEffects: Flow<DetailsViewModel.SideEffect>,
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
fun DetailsScreenContentPreview() {
    ZarinaTheme {
        DetailsScreenContent()
    }
}
