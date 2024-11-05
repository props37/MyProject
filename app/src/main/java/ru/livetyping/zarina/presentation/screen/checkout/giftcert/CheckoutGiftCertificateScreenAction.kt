package ru.livetyping.zarina.presentation.screen.checkout.giftcert

sealed class CheckoutGiftCertificateScreenAction {
    data class ScreenClosed(val isGiftCertificateApplied: Boolean) :
        CheckoutGiftCertificateScreenAction()
}
