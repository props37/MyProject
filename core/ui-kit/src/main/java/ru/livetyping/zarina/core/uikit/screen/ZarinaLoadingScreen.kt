package ru.livetyping.zarina.core.uikit.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.uikit.logo.ZarinaLogo
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaLoadingScreen(
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(R.string.loading),
    logoWidth: Dp = ZarinaLoadingScreenDefaults.LogoWidth,
    logoColor: Color = ZarinaLoadingScreenDefaults.LogoColor,
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
            modifier = Modifier.width(logoWidth),
        )
    }
}

public object ZarinaLoadingScreenDefaults {
    // Splash screen logo size according to https://developer.android.com/develop/ui/views/launch/splash-screen
    public val LogoWidth: Dp get() = 192.dp

    public val LogoColor: Color
        @Composable
        get() = UiKitTheme.colors.icon.regular.default
}
