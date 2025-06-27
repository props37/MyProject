package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface SizeTableSideEffect : SideEffect {
    data class Navigate(val action: SizeTableScreenAction) : SizeTableSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : SizeTableSideEffect
}
