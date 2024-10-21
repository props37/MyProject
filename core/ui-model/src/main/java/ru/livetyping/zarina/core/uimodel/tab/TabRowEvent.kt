package ru.livetyping.zarina.core.uimodel.tab

public sealed interface TabRowEvent<T> {

    public data class TabChanged<T>(val tab: T) : TabRowEvent<T>

    public data class TabReselected<T>(val tab: T) : TabRowEvent<T>
}
