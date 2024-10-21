package ru.livetyping.zarina.core.uimodel.tab

import kotlinx.collections.immutable.ImmutableList

public data class TabRowState<T>(
    val tabs: ImmutableList<T>,
    val currentTab: T,
) {
    public val currentTabIndex: Int
        get() = tabs.indexOf(currentTab)
}
