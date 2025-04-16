package ru.livetyping.zarina.feature.payment.ui.impl.payment

internal sealed interface PaymentScreenAction {
    data object BackClicked : PaymentScreenAction
}
