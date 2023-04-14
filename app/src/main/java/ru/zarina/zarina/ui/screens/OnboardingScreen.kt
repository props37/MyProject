package ru.zarina.zarina.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ru.zarina.zarina.R
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
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Image(
            painter = painterResource(id = R.drawable.onboarding_default_banner),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth(),
        )
        val gradientBrush = Brush.verticalGradient(
            0f to Color.Transparent,
            1f to Color.Black.copy(alpha = 0.5f),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
        )
    }
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
    // TODO vertical align
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = modifier.fillMaxWidth(0.65f)
        )
    }
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
