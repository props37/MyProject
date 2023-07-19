package ru.zarina.zarina.ui.common.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.utils.compose.minInteractionSize

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FavoriteHeart(
    state: FavoriteState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentDescriptionRes = if (state.isSelected)
        R.string.remove_from_favorites
    else
        R.string.add_to_favorites
    // TODO shake heart when error occurs
    // TODO add haptic
    AnimatedContent(
        targetState = state.isSelected,
        transitionSpec = {
            fadeIn() with fadeOut(targetAlpha = 0f)
        },
        label = "favorite heart crossfade",
    ) { isSelected ->
        val iconRes = if (isSelected)
            R.drawable.ic_heart_filled_24
        else
            R.drawable.ic_heart_24
        Image(
            painter = painterResource(iconRes),
            contentDescription = stringResource(contentDescriptionRes),
            modifier = modifier
                .minInteractionSize()
                .clickable(
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                )
                .size(24.dp),
        )
    }
}

sealed class FavoriteState(open val isSelected: Boolean) {
    data class Success(override val isSelected: Boolean) : FavoriteState(isSelected)
    data class Error(override val isSelected: Boolean) : FavoriteState(isSelected)
}
