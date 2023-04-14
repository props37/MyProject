package ru.zarina.zarina.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.zarina.zarina.ui.common.tooling.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.FontScalePreviews
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
private fun OnboardingScreenContent() {

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
