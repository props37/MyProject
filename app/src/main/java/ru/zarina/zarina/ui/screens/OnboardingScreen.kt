package ru.zarina.zarina.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.zarina.zarina.ui.common.tooling.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
private fun OnboardingScreenContent() {
    Banner()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        TopBar()
        Logo(
            modifier = Modifier.weight(1f),
        )
        CitySelection()
    }
}

@Composable
fun Banner(
    modifier: Modifier = Modifier,
) {
    // TODO display local banner
    // TODO load current banner from backend
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
) {
    // TODO close button
}

@Composable
fun Logo(
    modifier: Modifier = Modifier,
) {
    // TODO add logo
}

@Composable
fun CitySelection(
    modifier: Modifier = Modifier,
) {
    // TODO text
    // TODO buttons
}

@Composable
fun OnboardingScreen() {
    OnboardingScreenContent()
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun OnboardingScreenContentPreview() {
    ZarinaTheme {
        OnboardingScreenContent()
    }
}
