package ru.livetyping.zarina.feature.profile.ui.impl.impl.order.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.dialog.ZarinaDialogContainer
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model.OrderCancellationDialogEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model.OrderCancellationDialogState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun OrderCancellationDialog(
    state: OrderCancellationDialogState,
    onEvent: (OrderCancellationDialogEvent) -> Unit,
) {
    if (state is OrderCancellationDialogState.Visible) {
        val onCloseClicked = { onEvent(OrderCancellationDialogEvent.CloseClicked) }

        Dialog(
            onDismissRequest = onCloseClicked,
            properties = remember { DialogProperties(usePlatformDefaultWidth = false) },
        ) {
            ZarinaDialogContainer(
                title = {
                    Text(text = stringResource(R.string.profile_order_cancellation_title))
                },
                body = {
                    Text(text = stringResource(R.string.profile_order_cancellation_body))
                },
                buttons = {
                    ZarinaButton(
                        onClick = onCloseClicked,
                        colors = ZarinaButtonDefaults.tertiaryColors(),
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = stringResource(RCommon.string.res_return).uppercase())
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    ZarinaButton(
                        onClick = { onEvent(OrderCancellationDialogEvent.CancelOrderClicked) },
                        isLoading = state.isCancelOrderButtonLoading,
                        colors = ZarinaButtonDefaults.outlineErrorColors(),
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = stringResource(RCommon.string.res_cancel).uppercase())
                    }
                },
            )
        }
    }
}
