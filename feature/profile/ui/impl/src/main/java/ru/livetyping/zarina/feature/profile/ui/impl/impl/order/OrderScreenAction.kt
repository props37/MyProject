package ru.livetyping.zarina.feature.profile.ui.impl.impl.order

import ru.livetyping.zarina.core.domain.model.common.Url

internal sealed interface OrderScreenAction {
    data object BackClicked : OrderScreenAction

    data class PayClicked(val paymentUrl: Url) : OrderScreenAction
}
