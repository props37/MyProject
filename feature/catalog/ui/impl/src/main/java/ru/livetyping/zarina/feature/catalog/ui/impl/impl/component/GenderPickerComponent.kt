package ru.livetyping.zarina.feature.catalog.ui.impl.impl.component

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowState

internal class GenderPickerComponent(viewModelScope: CoroutineScope) {
    private val tabs = GenderTab.getTabs().toImmutableList()
    private val currentTab = MutableStateFlow(GenderTab.WOMEN)

    val genderPickerState: StateFlow<TabRowState<GenderTab>> = currentTab.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        transform = { currentTab -> TabRowState(tabs, currentTab) },
    )

    fun onGenderSelected(tab: GenderTab) {
        currentTab.value = tab
    }
}
