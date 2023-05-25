package ru.zarina.zarina.ui.screens.subscribe.success

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
fun SuccessScreenContent(
    email: String,
    onCloseClick: () -> Unit,
) {
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(R.string.subscription_to_product),
                endIcon = {
                    CloseButton(
                        onClick = onCloseClick,
                    )
                },
            )
        }
    ) {
        // TODO
    }
}

@Composable
fun SuccessScreen(
    goBack: () -> Unit,
) {
    val viewModel = hiltViewModel<SuccessViewModel>()

    val email by viewModel.email.collectAsStateWithLifecycle()

    SuccessScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
    )

    SuccessScreenContent(
        email = email,
        onCloseClick = viewModel::onCloseClick,
    )
}

@Composable
fun SuccessScreenBehavior(
    sideEffects: Flow<SuccessViewModel.SideEffect>,
    goBack: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                SuccessViewModel.SideEffect.GoBack -> goBack()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun SuccessScreenContentPreview() {
    ZarinaTheme {
        SuccessScreenContent(
            email = "mikhail@gmail.com",
            onCloseClick = {},
        )
    }
}
