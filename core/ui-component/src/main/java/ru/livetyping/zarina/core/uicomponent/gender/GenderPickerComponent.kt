package ru.livetyping.zarina.core.uicomponent.gender

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowState

public class GenderPickerComponent(coroutineScope: CoroutineScope) {
    private val tabs = GenderTab.getTabs().toImmutableList()
    private val currentTab = MutableStateFlow(GenderTab.WOMEN)

    public val genderPickerState: StateFlow<TabRowState<GenderTab>> = currentTab.mapState(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        transform = { currentTab -> TabRowState(tabs, currentTab) },
    )

    public fun onGenderSelected(tab: GenderTab) {
        currentTab.value = tab
    }
}
