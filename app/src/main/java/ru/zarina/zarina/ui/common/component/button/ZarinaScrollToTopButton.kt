package ru.zarina.zarina.ui.common.component.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.util.compose.animation.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.animation.AnimatedContentDefaultExitTransition

@Composable
fun ZarinaScrollToTopButton(
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = AnimatedContentDefaultEnterTransition,
        exit = AnimatedContentDefaultExitTransition,
        modifier = modifier,
    ) {
        ZarinaButton(
            onClick = onClick,
            size = ZarinaButtonSize.Medium,
            shape = CircleShape,
            contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_left_24),
                contentDescription = stringResource(R.string.scroll_to_top),
                modifier = Modifier
                    .size(16.dp)
                    .rotate(degrees = 90f),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        ZarinaScrollToTopButton(
            isVisible = true,
            onClick = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}
