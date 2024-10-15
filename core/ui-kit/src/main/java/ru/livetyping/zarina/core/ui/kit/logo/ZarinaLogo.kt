package ru.livetyping.zarina.core.ui.kit.logo

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.ui.kit.R
import ru.livetyping.zarina.core.ui.kit.theme.UiKitTheme

@Composable
public fun ZarinaLogo(
    modifier: Modifier = Modifier,
    contentDescription: String? = stringResource(R.string.zarina),
    color: Color = ZarinaLogoDefaults.Color,
    animate: Boolean = false,
) {
    // TODO: [Top] Add shimmer

    Icon(
        painter = painterResource(R.drawable.zarina_logo),
        tint = color,
        contentDescription = contentDescription,
        modifier = modifier
            .aspectRatio(ZarinaLogoDefaults.ZarinaLogoAspectRatio),
    )
}

public object ZarinaLogoDefaults {
    internal val Color: Color
        @Composable
        get() = UiKitTheme.colors.icon.regular.default

    // According to R.drawable.zarina_logo size
    internal const val ZarinaLogoAspectRatio = 100f / 10
}
