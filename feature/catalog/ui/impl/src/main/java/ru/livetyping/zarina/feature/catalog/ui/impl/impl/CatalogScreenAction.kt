package ru.livetyping.zarina.feature.catalog.ui.impl.impl

internal sealed interface CatalogScreenAction {
    data object ScreenClosed : CatalogScreenAction
}
