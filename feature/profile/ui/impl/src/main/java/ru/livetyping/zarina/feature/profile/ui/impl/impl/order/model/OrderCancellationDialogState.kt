package ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model

import androidx.compose.runtime.Stable

@Stable
internal sealed class OrderCancellationDialogState {
    data class Visible(val isCancelOrderButtonLoading: Boolean) : OrderCancellationDialogState()

    data object Hidden : OrderCancellationDialogState()
}
