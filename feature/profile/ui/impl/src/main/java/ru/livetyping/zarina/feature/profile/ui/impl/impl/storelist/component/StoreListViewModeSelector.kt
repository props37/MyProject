package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.uikit.tab.ZarinaTab
import ru.livetyping.zarina.core.uikit.tab.ZarinaTabRow
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListViewMode
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun StoreListViewModeSelector(
    state: TabRowState<StoreListViewMode>,
    onEvent: (TabRowEvent<StoreListViewMode>) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTabRow(
        selectedTabIndex = state.currentTabIndex,
        modifier = modifier,
    ) {
        state.tabs.forEach { mode ->
            val textResId = when (mode) {
                StoreListViewMode.MAP -> RCommon.string.res_map
                StoreListViewMode.LIST -> RCommon.string.res_list
            }
            val isSelected = mode == state.currentTab

            ZarinaTab(
                text = stringResource(textResId),
                onClick = {
                    val event = if (!isSelected) {
                        TabRowEvent.TabChanged(mode)
                    } else {
                        TabRowEvent.TabReselected(mode)
                    }
                    onEvent(event)
                },
                isSelected = isSelected,
            )
        }
    }
}
