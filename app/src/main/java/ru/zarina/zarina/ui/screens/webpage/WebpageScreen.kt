package ru.zarina.zarina.ui.screens.webpage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun WebpageScreenContent() {

}

@Composable
fun WebpageScreen() {
    val viewModel = hiltViewModel<WebpageViewModel>()

    WebpageScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    WebpageScreenContent()
}

@Composable
fun WebpageScreenBehavior(
    sideEffects: Flow<WebpageViewModel.SideEffect>,
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
fun WebpageScreenContentPreview() {
    ZarinaTheme {
        WebpageScreenContent()
    }
}
