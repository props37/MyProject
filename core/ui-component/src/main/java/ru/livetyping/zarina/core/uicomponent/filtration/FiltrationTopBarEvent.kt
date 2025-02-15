package ru.livetyping.zarina.core.uicomponent.filtration

public sealed interface FiltrationTopBarEvent {
    public data object BackClicked : FiltrationTopBarEvent

    public data object ResetFiltersClicked : FiltrationTopBarEvent
}
