package ru.zarina.zarina.ui.common.component.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.ZarinaLogo
import ru.zarina.zarina.ui.common.component.ZarinaLogoAspectRatio
import ru.zarina.zarina.ui.common.util.SplashScreenLogoSize
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.rework.ZarinaTheme

@Composable
fun ZarinaLoadingScreen(
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(R.string.loading),
    logoWidth: Dp = SplashScreenLogoSize,
    logoColor: Color = UiKitTheme.colorsReworked.icon.regular.default,
    animate: Boolean = true,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        ZarinaLogo(
            contentDescription = contentDescription,
            color = logoColor,
            animate = animate,
            modifier = Modifier
                .width(logoWidth)
                .aspectRatio(ZarinaLogoAspectRatio),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaTheme {
        ZarinaLoadingScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        )
    }
}
