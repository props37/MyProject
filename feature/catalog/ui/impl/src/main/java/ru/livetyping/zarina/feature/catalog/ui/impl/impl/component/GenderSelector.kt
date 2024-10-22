package ru.livetyping.zarina.feature.catalog.ui.impl.impl.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.uikit.tab.ZarinaTab
import ru.livetyping.zarina.core.uikit.tab.ZarinaTabRow
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.core.ui.kit.R as RUiKit

@Composable
internal fun GenderSelector(
    genderSelectorState: TabRowState<GenderTab>,
    onGenderSelectorEvent: (TabRowEvent<GenderTab>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedTabIndex = remember(genderSelectorState) {
        genderSelectorState.currentTabIndex
    }

    ZarinaTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
    ) {
        for (i in genderSelectorState.tabs.indices) {
            val gender = genderSelectorState.tabs[i]
            key(gender) {
                val textResId = when (gender) {
                    GenderTab.WOMEN -> RUiKit.string.for_women
                    GenderTab.MEN -> RUiKit.string.for_men
                }

                val isSelected = gender == genderSelectorState.currentTab
                ZarinaTab(
                    text = stringResource(textResId).uppercase(),
                    onClick = {
                        if (!isSelected) {
                            onGenderSelectorEvent(TabRowEvent.TabChanged(gender))
                        } else {
                            onGenderSelectorEvent(TabRowEvent.TabReselected(gender))
                        }
                    },
                    isSelected = isSelected,
                    selectedTextStyle = UiKitTheme.typography.tertiary.regular,
                    unselectedTextStyle = UiKitTheme.typography.tertiary.light,
                )
            }
        }
    }
}
