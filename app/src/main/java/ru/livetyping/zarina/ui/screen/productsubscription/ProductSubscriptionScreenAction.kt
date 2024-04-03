package ru.livetyping.zarina.ui.screen.productsubscription

sealed class ProductSubscriptionScreenAction {
    data object ScreenClosed : ProductSubscriptionScreenAction()

    data object SubscriptionCompleted : ProductSubscriptionScreenAction()
}
