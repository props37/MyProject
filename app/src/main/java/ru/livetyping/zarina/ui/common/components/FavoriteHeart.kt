package ru.livetyping.zarina.ui.common.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.common.animations.shake
import ru.livetyping.zarina.utils.compose.HapticType
import ru.livetyping.zarina.utils.compose.performHaptic


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FavoriteHeart(
    isFavorite: Boolean,
    isShaking: Boolean,
    onFavoriteChange: (isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentDescriptionRes = if (isFavorite)
        R.string.remove_from_favorites
    else
        R.string.add_to_favorites

    val view = LocalView.current
    val xOffset = remember { Animatable(0f) }

    LaunchedEffect(view, isShaking) {
        if (isShaking) {
            view.performHaptic(HapticType.ERROR)
            xOffset.animateTo(0f, shake)
        }
    }

    AnimatedContent(
        targetState = isFavorite,
        transitionSpec = {
            fadeIn() with fadeOut(targetAlpha = 0f)
        },
        label = "favorite heart crossfade",
        modifier = modifier
            .graphicsLayer {
                translationX = xOffset.value
            }
    ) {
        val iconRes = if (it)
            R.drawable.old_ic_heart_filled_24
        else
            R.drawable.ic_heart_24
        Box(
            modifier = Modifier
                .clickable(
                    onClick = { onFavoriteChange(!it) },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                )
                .minimumInteractiveComponentSize()
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = stringResource(contentDescriptionRes),
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}
