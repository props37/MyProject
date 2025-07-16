package ru.livetyping.zarina.feature.payment.ui.impl.screen

internal sealed interface PaymentScreenAction {
    data object BackClicked : PaymentScreenAction
}
