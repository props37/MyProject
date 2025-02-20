package ru.livetyping.zarina.core.uicomponent.filtration.model

public sealed interface ProductFiltrationTopBarEvent {
    public data object BackClicked : ProductFiltrationTopBarEvent

    public data object ResetFiltersClicked : ProductFiltrationTopBarEvent
}
