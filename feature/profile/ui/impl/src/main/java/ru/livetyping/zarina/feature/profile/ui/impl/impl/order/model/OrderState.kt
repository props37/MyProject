package ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class OrderState {
    abstract val isRefreshing: Boolean

    @Immutable
    data class Success(
        val order: OrderDetailed,
        override val isRefreshing: Boolean,
    ) : OrderState()

    data object Loading : OrderState() {
        override val isRefreshing: Boolean get() = false
    }

    @Immutable
    data class Error(
        val state: ZarinaErrorScreenState,
        override val isRefreshing: Boolean,
    ) : OrderState()
}
