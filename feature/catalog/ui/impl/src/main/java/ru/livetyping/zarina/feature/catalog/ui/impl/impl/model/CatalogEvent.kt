package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import ru.livetyping.zarina.core.uimodel.tab.GenderTab

internal sealed interface CatalogEvent {
    data object BackClicked : CatalogEvent

    data class GenderSelected(val tab: GenderTab) : CatalogEvent

    data object SearchClicked : CatalogEvent
}
