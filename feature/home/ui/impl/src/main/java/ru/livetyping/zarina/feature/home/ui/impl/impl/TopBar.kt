package ru.livetyping.zarina.feature.home.ui.impl.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.logo.ZarinaLogo
import ru.livetyping.zarina.core.uikit.tab.ZarinaLooseTabRow
import ru.livetyping.zarina.core.uikit.tab.ZarinaTab
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.home.ui.impl.R
import ru.livetyping.zarina.feature.home.ui.impl.impl.gender.GenderSelectorEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.gender.GenderSelectorState
import ru.livetyping.zarina.feature.home.ui.impl.impl.gender.GenderTab

@Composable
internal fun TopBar(
    genderSelectorState: GenderSelectorState,
    onGenderSelectorEvent: (GenderSelectorEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = UiKitTheme.colors.text.general.inversed.default

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        ZarinaLogo(
            color = contentColor,
            modifier = Modifier.width(140.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        val selectedTabIndex = remember(genderSelectorState) {
            genderSelectorState.genders.indexOf(genderSelectorState.currentGender)
        }

        ZarinaLooseTabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = Color.Unspecified,
            contentColor = UiKitTheme.colors.background.general.regular.default,
        ) {
            for (i in genderSelectorState.genders.indices) {
                val gender = genderSelectorState.genders[i]
                val textResId = when (gender) {
                    GenderTab.WOMEN -> R.string.for_women
                    GenderTab.MEN -> R.string.for_men
                }

                ZarinaTab(
                    text = stringResource(textResId),
                    onClick = {
                        onGenderSelectorEvent(GenderSelectorEvent.GenderChanged(gender))
                    },
                    isSelected = gender == genderSelectorState.currentGender,
                )
            }
        }
    }
}

@Composable
internal fun rememberTopBarScrimBrush(): Brush {
    val scrimColor = UiKitTheme.colors.background.general.inversed.default
    return remember(scrimColor) {
        val colors = listOf(
            scrimColor.copy(alpha = TopBarScrimAlpha),
            Color.Transparent,
        )
        Brush.verticalGradient(colors)
    }
}

private const val TopBarScrimAlpha = 0.24f
