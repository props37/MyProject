package ru.livetyping.zarina.feature.payment.ui.impl.payment

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface PaymentSideEffect : SideEffect {
    data class Navigate(val action: PaymentScreenAction) : PaymentSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : PaymentSideEffect
}
