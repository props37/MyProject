package ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.uikit.tab.ZarinaTab
import ru.livetyping.zarina.core.uikit.tab.ZarinaTabRow
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.model.BonusHistoryTab

@Composable
internal fun BonusHistoryTabRow(
    state: TabRowState<BonusHistoryTab>,
    onEvent: (TabRowEvent<BonusHistoryTab>) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTabRow(
        selectedTabIndex = state.currentTabIndex,
        modifier = modifier,
    ) {
        state.tabs.forEach { tab ->
            val textResId = when (tab) {
                BonusHistoryTab.BONUS_HISTORY -> R.string.profile_bonus_history
                BonusHistoryTab.EXPECTED_BONUSES -> R.string.profile_bonus_history_expected_bonuses
            }
            val isSelected = tab == state.currentTab

            ZarinaTab(
                text = stringResource(textResId),
                onClick = {
                    val event = if (!isSelected) {
                        TabRowEvent.TabChanged(tab)
                    } else {
                        TabRowEvent.TabReselected(tab)
                    }
                    onEvent(event)
                },
                isSelected = isSelected,
            )
        }
    }
}
