package ru.zarina.zarina.ui.screens.pickup.selectshopcity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun SelectPickupCityScreenContent() {

}

@Composable
fun SelectPickupCityScreen() {
    val viewModel = hiltViewModel<SelectPickupCityViewModel>()

    SelectPickupCityScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    SelectPickupCityScreenContent()
}

@Composable
fun SelectPickupCityScreenBehavior(
    sideEffects: Flow<SelectPickupCityViewModel.SideEffect>,
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
fun SelectPickupCityScreenContentPreview() {
    ZarinaTheme {
        SelectPickupCityScreenContent()
    }
}
