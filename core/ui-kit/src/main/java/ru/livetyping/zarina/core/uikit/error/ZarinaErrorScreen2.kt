package ru.livetyping.zarina.core.uikit.error

import androidx.annotation.OptIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
import ru.livetyping.zarina.core.mediacompose.SimpleVideoPlayer
import ru.livetyping.zarina.core.uicompose.textString
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.resource.R as RCommon

// TODO: [Top] Rename after full migration

@OptIn(UnstableApi::class)
@Composable
public fun ZarinaErrorScreen2(
    state: ZarinaErrorScreenState2,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    bottomPaddingProvider: @Composable () -> Dp = { 0.dp },
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            val isVideoVisible = (maxHeight / maxWidth) >= 0.9f

            androidx.compose.animation.AnimatedVisibility(
                visible = isVideoVisible,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
                modifier = Modifier.matchParentSize(),
            ) {
                SimpleVideoPlayer(
                    resId = RCommon.raw.zarina_error_screen_video,
                    contentScale = ContentScale.FillWidth,
                    surfaceType = SURFACE_TYPE_TEXTURE_VIEW,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = textString(state.title).uppercase(),
                style = UiKitTheme2.typography.h2Regular,
                color = UiKitTheme2.colors.mainBlack,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = textString(state.body).uppercase(),
                style = UiKitTheme2.typography.body,
                color = UiKitTheme2.colors.mainBlack,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
            )

            if (state.isButtonVisible) {
                Spacer(modifier = Modifier.height(32.dp))

                ZarinaButton(
                    onClick = onButtonClick,
                    contentPadding = PaddingValues(horizontal = 60.dp, vertical = 8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    Text(text = textString(state.buttonText).uppercase())
                }
            }

            Spacer(
                modifier = Modifier
                    .heightIn(min = 24.dp + bottomPaddingProvider())
                    .fillMaxHeight(0.2f)
            )
        }
    }
}
