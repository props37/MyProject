package ru.livetyping.zarina.core.uimodel.tab

import kotlinx.collections.immutable.ImmutableList

// Marked as stable on config/compose/stability_config.txt
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
