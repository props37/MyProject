package ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model

public sealed interface ListFilterTopBarEvent {
    public data object BackClicked : ListFilterTopBarEvent

    public data object ResetFilterClicked : ListFilterTopBarEvent
}
