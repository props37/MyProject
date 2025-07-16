package ru.livetyping.zarina.feature.productsubscription.ui.impl.screen

internal sealed interface ProductSubscriptionScreenAction {
    data object BackClicked : ProductSubscriptionScreenAction

    data object SubscriptionCompleted : ProductSubscriptionScreenAction
}
