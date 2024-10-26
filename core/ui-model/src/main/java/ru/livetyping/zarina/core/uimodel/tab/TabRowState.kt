package ru.livetyping.zarina.core.uimodel.tab

import kotlinx.collections.immutable.ImmutableList

public data class TabRowState<T>(
    val tabs: ImmutableList<T>,
    val currentTab: T,
) {
    public val currentTabIndex: Int by lazy {
        val index = tabs.indexOf(currentTab)
        check(index >= 0) { "There is no current tab $currentTab in tabs $tabs" }
        index
    }
}
