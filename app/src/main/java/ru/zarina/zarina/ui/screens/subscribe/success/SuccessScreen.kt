package ru.zarina.zarina.ui.screens.subscribe.success

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.common.components.toolbar.CloseButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.old.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreenContent(
    email: String,
    onContinueClick: () -> Unit,
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
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_envelope_96),
                contentDescription = null,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Text(
                text = stringResource(R.string.product_subscription_done),
                style = UiKitTheme.typography.circle1720bold,
                color = UiKitTheme.colorsOld.primaryContentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Text(
                text = stringResource(
                    R.string.product_availability_will_be_sent_to_mail_template,
                    email
                ),
                style = UiKitTheme.typography.circle1518,
                color = UiKitTheme.colorsOld.primaryContentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            ZarinaTextButton(
                text = stringResource(id = R.string.continue_shopping),
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .navigationBarsPadding(),
            )
        }
    }
}

@Composable
fun SuccessScreen(
    goBack: () -> Unit,
) {
    val viewModel = koinViewModel<SuccessViewModel>()

    val email by viewModel.email.collectAsStateWithLifecycle()

    SuccessScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
    )

    SuccessScreenContent(
        email = email,
        onContinueClick = viewModel::onContinueClick,
        onCloseClick = viewModel::onCloseClick,
    )
}

@Composable
fun SuccessScreenBehavior(
    sideEffects: Flow<SuccessViewModel.SideEffect>,
    goBack: () -> Unit,
) {
    NavigationBarState(isVisible = false, isAnimated = false)
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                SuccessViewModel.SideEffect.GoBack -> goBack()
            }
        }
    }
}

@Preview
@Composable
fun SuccessScreenContentPreview() {
    ZarinaTheme {
        SuccessScreenContent(
            email = "mikhail@gmail.com",
            onContinueClick = {},
            onCloseClick = {},
        )
    }
}
