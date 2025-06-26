package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.uikit.tab.ZarinaTab
import ru.livetyping.zarina.core.uikit.tab.ZarinaTabRow
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.product.ui.impl.R
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.ViewMode

@Composable
internal fun ViewModeSelector(
    state: TabRowState<ViewMode>,
    onViewModeSelected: (ViewMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTabRow(
        selectedTabIndex = state.currentTabIndex,
        modifier = modifier,
    ) {
        state.tabs.forEach { mode ->
            val textResId = when (mode) {
                ViewMode.PRODUCT_MEASUREMENTS -> R.string.product_measurements
                ViewMode.SIZE_GUIDE -> R.string.product_size_guide
            }

            ZarinaTab(
                text = stringResource(textResId).uppercase(),
                onClick = { onViewModeSelected(mode) },
                isSelected = mode == state.currentTab,
            )
        }
    }
}
