package ru.zarina.zarina.ui.screens.subscribe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun SubscribeScreenContent() {

}

@Composable
fun SubscribeScreen() {
    val viewModel = hiltViewModel<SubscribeViewModel>()

    SubscribeScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    SubscribeScreenContent()
}

@Composable
fun SubscribeScreenBehavior(
    sideEffects: Flow<SubscribeViewModel.SideEffect>,
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
fun SubscribeScreenContentPreview() {
    ZarinaTheme {
        SubscribeScreenContent()
    }
}
