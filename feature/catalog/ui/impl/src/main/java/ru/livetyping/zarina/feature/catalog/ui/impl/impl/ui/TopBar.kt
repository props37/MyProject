package ru.livetyping.zarina.feature.catalog.ui.impl.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.gender.GenderPicker
import ru.livetyping.zarina.core.uikit.logo.ZarinaLogo
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowState

@Composable
internal fun TopBar(
    genderPickerState: TabRowState<GenderTab>,
    onGenderSelected: (GenderTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentPadding = PaddingValues(
        start = 0.dp,
        top = 16.dp,
        end = 0.dp,
        bottom = 4.dp,
    )

    ZarinaTopBar(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ZarinaLogo(modifier = Modifier.height(18.dp))

            Spacer(modifier = Modifier.height(12.dp))

            GenderPicker(
                genders = genderPickerState.tabs,
                selectedGender = genderPickerState.currentTab,
                onGenderSelected = onGenderSelected,
            )

            Spacer(modifier = Modifier.height(12.dp))

            ZarinaDivider(modifier = Modifier.fillMaxWidth())
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
