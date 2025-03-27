package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.uikit.tab.ZarinaTab
import ru.livetyping.zarina.core.uikit.tab.ZarinaTabRow
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.ViewMode
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun ViewModeSelector(
    state: TabRowState<ViewMode>,
    onEvent: (TabRowEvent<ViewMode>) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTabRow(
        selectedTabIndex = state.currentTabIndex,
        modifier = modifier,
    ) {
        state.tabs.forEach { viewMode ->
            key(viewMode) {
                val textResId = when (viewMode) {
                    ViewMode.MAP -> RCommon.string.res_map
                    ViewMode.LIST -> RCommon.string.res_list
                }

                ZarinaTab(
                    text = stringResource(textResId),
                    isSelected = viewMode == state.currentTab,
                    onClick = {
                        if (viewMode == state.currentTab) {
                            onEvent(TabRowEvent.TabReselected(viewMode))
                        } else {
                            onEvent(TabRowEvent.TabChanged(viewMode))
                        }
                    },
                )
            }
        }
    }
}
