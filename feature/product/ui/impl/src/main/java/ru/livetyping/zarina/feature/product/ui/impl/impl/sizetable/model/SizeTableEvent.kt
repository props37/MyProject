package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model

internal sealed interface SizeTableEvent {
    data object CloseClicked : SizeTableEvent

    data class ViewModeSelected(val mode: ViewMode) : SizeTableEvent
}
