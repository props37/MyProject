package ru.zarina.zarina.ui.screens.pickup.success

import androidx.compose.foundation.Image
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
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.common.components.toolbar.CloseButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.screens.pickup.PickupViewModel
import ru.zarina.zarina.ui.theme.old.UiKitTheme
import ru.zarina.zarina.ui.theme.old.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreenContent(
    email: String,
    onContinueShoppingClick: () -> Unit,
    onCloseClick: () -> Unit,
) {
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(R.string.pickup_at_shop),
                endIcon = {
                    CloseButton(onClick = onCloseClick)
                }
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .navigationBarsPadding(),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.ic_box_96),
                contentDescription = null,
            )
            Text(
                text = stringResource(R.string.reservation_is_successful),
                style = UiKitTheme.typography.circle1720bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
            )
            Text(
                text = stringResource(
                    R.string.reservation_confirmation_will_be_sent_to_email_template,
                    email
                ),
                style = UiKitTheme.typography.circle1518,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            ZarinaTextButton(
                text = stringResource(id = R.string.continue_shopping),
                onClick = onContinueShoppingClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
            )
        }
    }
}

@Composable
fun SuccessScreen(
    parentEntry: NavBackStackEntry,
    goBack: () -> Unit,
) {
    val parentViewModel = koinViewModel<PickupViewModel>(viewModelStoreOwner = parentEntry)
    val viewModel = koinViewModel<SuccessViewModel>()

    val email by parentViewModel.email.collectAsStateWithLifecycle()

    SuccessScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
    )

    SuccessScreenContent(
        email = email,
        onContinueShoppingClick = viewModel::onContinueShoppingClick,
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
            email = "zarina@melonfashion.com",
            onContinueShoppingClick = {},
            onCloseClick = {},
        )
    }
}
