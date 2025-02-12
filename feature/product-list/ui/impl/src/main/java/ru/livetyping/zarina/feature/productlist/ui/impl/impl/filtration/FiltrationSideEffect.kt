package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface FiltrationSideEffect : SideEffect {
    data class Navigate(val action: FiltrationScreenAction) : FiltrationSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : FiltrationSideEffect
}
