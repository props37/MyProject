package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist

import ru.livetyping.zarina.core.platform.settings.SystemSettings
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface StoreListSideEffect : SideEffect {
    data class Navigate(val action: StoreListScreenAction) : StoreListSideEffect

    data class OpenSystemSettings(val settings: SystemSettings) : StoreListSideEffect
}
