package ru.livetyping.zarina.feature.catalog.ui.impl.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.logo.ZarinaLogo
import ru.livetyping.zarina.core.uikit.tab.ZarinaBracketTab
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun TopBar(
    genderPickerState: TabRowState<GenderTab>,
    onGenderSelected: (GenderTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        ZarinaLogo(modifier = Modifier.height(18.dp))

        Spacer(modifier = Modifier.height(12.dp))

        GenderPicker(
            genderPickerState = genderPickerState,
            onGenderSelected = onGenderSelected,
        )

        Spacer(modifier = Modifier.height(12.dp))

        ZarinaDivider(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun GenderPicker(
    genderPickerState: TabRowState<GenderTab>,
    onGenderSelected: (GenderTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        genderPickerState.tabs.fastForEach { gender ->
            key(gender) {
                val textResId = when (gender) {
                    GenderTab.WOMEN -> RCommon.string.res_for_women
                    GenderTab.MEN -> RCommon.string.res_for_men
                }

                ZarinaBracketTab(
                    text = stringResource(textResId).uppercase(),
                    isSelected = gender == genderPickerState.currentTab,
                    onClick = { onGenderSelected(gender) },
                    addBrackets = true,
                )
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    ZarinaTheme2 {
        var currentGender by remember { mutableStateOf(GenderTab.WOMEN) }
        val genderPickerState = remember(currentGender) {
            TabRowState(
                tabs = GenderTab.getTabs().toImmutableList(),
                currentTab = currentGender,
            )
        }

        TopBar(
            genderPickerState = genderPickerState,
            onGenderSelected = { currentGender = it },
            modifier = Modifier.background(Color.White),
        )
    }
}
