package ru.livetyping.zarina.ui.common.components.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.ui.theme.old.ZarinaTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ZarinaTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    colors: ZarinaButtonColors = ZarinaButtonDefaults.primaryColors(),
) {
    ZarinaButton(
        onClick = onClick,
        colors = colors,
        isLoading = isLoading,
        isEnabled = isEnabled,
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = text,
            label = "text button text",
            transitionSpec = { fadeIn() with fadeOut() }
        ) { text ->
            Text(
                text = text,
                color = LocalContentColor.current,
                style = UiKitTheme.typographyOld.circle1720bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun ZarinaTextButtonPreview() {
    ZarinaTheme {
        ZarinaTextButton(
            text = "ZarinaTextButtonLongPreview",
            onClick = {},
            colors = ZarinaButtonDefaults.primaryColors(),
        )
    }
}
