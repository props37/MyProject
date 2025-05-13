package ru.livetyping.zarina.core.uikit.gender

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.uikit.tab.ZarinaBracketTab
import ru.livetyping.zarina.core.uikit.tab.ZarinaBracketTabDefaults
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowState

@Composable
public fun GenderPicker(
    state: TabRowState<GenderTab>,
    onGenderSelected: (GenderTab) -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = ZarinaBracketTabDefaults.SelectedColor,
    unselectedColor: Color = ZarinaBracketTabDefaults.UnselectedColor,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        state.tabs.fastForEach { gender ->
            key(gender) {
                val textResId = when (gender) {
                    GenderTab.WOMEN -> R.string.res_for_women
                    GenderTab.MEN -> R.string.res_for_men
                }

                ZarinaBracketTab(
                    text = stringResource(textResId).uppercase(),
                    isSelected = gender == state.currentTab,
                    onClick = { onGenderSelected(gender) },
                    addBrackets = true,
                    selectedColor = selectedColor,
                    unselectedColor = unselectedColor,
                )
            }
        }
    }
}
