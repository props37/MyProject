package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert.GiftCertificateNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert.GiftCertificateNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert.GiftCertificateScreen

internal fun NavGraphBuilder.giftCertificateScreen(actions: GiftCertificateNavActions) {
    composable<GiftCertificateNavEntry>(typeMap = GiftCertificateNavEntry.typeMap()) {
        GiftCertificateScreen(actions)
    }
}
