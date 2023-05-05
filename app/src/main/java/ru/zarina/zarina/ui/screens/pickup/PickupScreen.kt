package ru.zarina.zarina.ui.screens.pickup

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.toolbar.CloseButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupScreenContent() {
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(R.string.find_and_pickup),
                endIcon = {
                    CloseButton(onClick = { /*TODO*/ })
                }
            )
        }
    ) {

    }
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
