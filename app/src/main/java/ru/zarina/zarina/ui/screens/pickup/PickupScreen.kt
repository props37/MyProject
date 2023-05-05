package ru.zarina.zarina.ui.screens.pickup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun PickupScreenContent() {

}

@Composable
fun PickupScreen() {
    val viewModel = hiltViewModel<PickupViewModel>()

    PickupScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    PickupScreenContent()
}

@Composable
fun PickupScreenBehavior(
    sideEffects: Flow<PickupViewModel.SideEffect>,
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
fun PickupScreenContentPreview() {
    ZarinaTheme {
        PickupScreenContent()
    }
}
