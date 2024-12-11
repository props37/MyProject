package ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.model

internal sealed interface ProductSubscriptionEvent {
    data object BackClicked : ProductSubscriptionEvent

    data class PoliciesAcceptedChanged(val accepted: Boolean) : ProductSubscriptionEvent

    data object SubscribeClicked : ProductSubscriptionEvent
}
