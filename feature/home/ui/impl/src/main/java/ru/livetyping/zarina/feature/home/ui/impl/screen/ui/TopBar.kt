package ru.livetyping.zarina.feature.home.ui.impl.screen.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.LocalRippleConfiguration
import androidx.compose.material.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.gender.GenderPicker
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowState

@Composable
internal fun TopBar(
    genderPickerState: TabRowState<GenderTab>,
    onGenderSelected: (GenderTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.heightIn(min = 56.dp),
    ) {
        val rippleConfiguration = remember { RippleConfiguration(Color.White) }

        CompositionLocalProvider(LocalRippleConfiguration provides rippleConfiguration) {
            GenderPicker(
                genders = genderPickerState.tabs,
                selectedGender = genderPickerState.currentTab,
                onGenderSelected = onGenderSelected,
                selectedColor = UiKitTheme2.colors.white,
                unselectedColor = UiKitTheme2.colors.white.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
internal fun rememberTopBarScrimBrush(): Brush {
    val scrimColor = UiKitTheme2.colors.mainBlack
    return remember(scrimColor) {
        val colors = listOf(
            scrimColor.copy(alpha = TopBarScrimAlpha),
            Color.Transparent,
        )
        Brush.verticalGradient(colors)
    }
}

private const val TopBarScrimAlpha = 0.24f
