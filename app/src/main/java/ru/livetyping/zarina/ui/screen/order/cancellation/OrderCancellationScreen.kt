package ru.livetyping.zarina.ui.screen.order.cancellation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.ui.common.component.dialog.ZarinaDialogContainer
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.order.cancellation.OrderCancellationViewModel.SideEffect

@Composable
fun OrderCancellationScreen(
    navigate: (OrderCancellationScreenAction) -> Unit,
    viewModel: OrderCancellationViewModel = hiltViewModel(),
) {
    val isCancelButtonLoading by viewModel.isCancelButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        isCancelButtonLoading = isCancelButtonLoading,
        onBackClicked = viewModel::onBackClicked,
        onCancelClicked = viewModel::onCancelClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    isCancelButtonLoading: Boolean,
    onBackClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (OrderCancellationScreenAction) -> Unit,
) {
    OrderCancellationScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    ZarinaDialogContainer(
        title = {
            Text(text = stringResource(R.string.cancel_order_question))
        },
        body = {
            Text(text = stringResource(R.string.order_cancellation_consequences))
        },
        buttons = {
            ZarinaButton(
                onClick = onBackClicked,
                colors = ZarinaButtonDefaults.tertiaryColors(),
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.return_).uppercase())
            }
            Spacer(modifier = Modifier.width(8.dp))
            ZarinaButton(
                onClick = onCancelClicked,
                isLoading = isCancelButtonLoading,
                colors = ZarinaButtonDefaults.outlineErrorColors(),
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.cancel).uppercase())
            }
        },
    )
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            isCancelButtonLoading = false,
            onBackClicked = {},
            onCancelClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
