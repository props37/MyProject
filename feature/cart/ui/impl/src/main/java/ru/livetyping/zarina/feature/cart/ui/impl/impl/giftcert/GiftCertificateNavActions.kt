package ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert

import ru.livetyping.zarina.core.navigation.NavigationActions

internal class GiftCertificateNavActions(
    val onBackClicked: () -> Unit,
    val onGiftCertificateApplied: () -> Unit,
) : NavigationActions
