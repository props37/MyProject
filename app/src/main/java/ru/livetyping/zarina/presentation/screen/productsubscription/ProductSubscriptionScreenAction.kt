package ru.livetyping.zarina.presentation.screen.productsubscription

sealed class ProductSubscriptionScreenAction {
    data object ScreenClosed : ProductSubscriptionScreenAction()

    data object SubscriptionCompleted : ProductSubscriptionScreenAction()
}
