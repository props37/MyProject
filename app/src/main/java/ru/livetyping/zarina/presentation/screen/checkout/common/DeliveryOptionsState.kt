package ru.livetyping.zarina.presentation.screen.checkout.common

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.presentation.common.error.ErrorState

@Stable
sealed class DeliveryOptionsState {
    data object Loading : DeliveryOptionsState()

    @Immutable
    data class Success(val options: List<DeliveryOptionState>) : DeliveryOptionsState()

    @Immutable
    data class Error(val state: ErrorState) : DeliveryOptionsState()
}
