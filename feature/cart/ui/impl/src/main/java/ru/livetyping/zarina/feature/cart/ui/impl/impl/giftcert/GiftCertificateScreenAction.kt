package ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert

internal sealed interface GiftCertificateScreenAction {
    data object BackClicked : GiftCertificateScreenAction

    data object GiftCertificateApplied : GiftCertificateScreenAction
}
