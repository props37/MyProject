package ru.livetyping.zarina.feature.catalog.ui.impl.screen

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface CatalogSideEffect : SideEffect {
    data class Navigate(val action: CatalogScreenAction) : CatalogSideEffect
}
