package ru.zarina.zarina.ui.common.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.FavoriteState


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FavoriteHeart(
    state: FavoriteState,
    onFavoriteChange: (isSelected: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentDescriptionRes = if (state.isFavorite)
        R.string.remove_from_favorites
    else
        R.string.add_to_favorites
    // TODO shake heart when error occurs
    // TODO add haptic
    AnimatedContent(
        targetState = state.isFavorite,
        transitionSpec = {
            fadeIn() with fadeOut(targetAlpha = 0f)
        },
        label = "favorite heart crossfade",
    ) {
        val iconRes = if (it)
            R.drawable.ic_heart_filled_24
        else
            R.drawable.ic_heart_24
        Box(
            modifier = modifier
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
