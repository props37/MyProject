package ru.livetyping.zarina.presentation.screen.payment

sealed class PaymentScreenAction {
    data object ScreenClosed : PaymentScreenAction()
}
