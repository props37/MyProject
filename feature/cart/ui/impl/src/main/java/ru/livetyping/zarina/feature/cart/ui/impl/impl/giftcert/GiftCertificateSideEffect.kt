package ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface GiftCertificateSideEffect : SideEffect {
    data class Navigate(val action: GiftCertificateScreenAction) : GiftCertificateSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : GiftCertificateSideEffect
}
