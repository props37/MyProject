package ru.zarina.zarina.ui.screen.productsubscription

sealed class ProductSubscriptionScreenResult {
    data object ScreenClosed : ProductSubscriptionScreenResult()

    data object SubscriptionCompleted : ProductSubscriptionScreenResult()
}
