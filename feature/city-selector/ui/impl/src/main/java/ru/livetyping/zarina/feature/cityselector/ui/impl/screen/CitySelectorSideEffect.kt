package ru.livetyping.zarina.feature.cityselector.ui.impl.screen

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage2

internal sealed interface CitySelectorSideEffect : SideEffect {
    data class Navigate(val action: CitySelectorScreenAction) : CitySelectorSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage2) : CitySelectorSideEffect
}
