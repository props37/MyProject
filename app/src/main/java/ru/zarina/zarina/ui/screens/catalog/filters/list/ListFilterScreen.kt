package ru.zarina.zarina.ui.screens.catalog.filters.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun ListFilterScreenContent() {

}

@Composable
fun ListFilterScreen() {
    val viewModel = koinViewModel<ListFilterViewModel>()

    ListFilterScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    ListFilterScreenContent()
}

@Composable
fun ListFilterScreenBehavior(
    sideEffects: Flow<ListFilterViewModel.SideEffect>,
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
fun ListFilterScreenContentPreview() {
    ZarinaTheme {
        ListFilterScreenContent()
    }
}

