package ru.livetyping.zarina.core.uikit.indicator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import ru.livetyping.zarina.core.uikit.text.ZARINA_BRACKET_END
import ru.livetyping.zarina.core.uikit.text.ZARINA_BRACKET_START
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2

@Composable
public fun ZarinaExpandableIndicator(
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = UiKitTheme2.typography.body,
    color: Color = UiKitTheme2.colors.mainBlack,
    bracketPadding: Dp = 5.dp,
) {
    Row(modifier = modifier) {
        CompositionLocalProvider(
            LocalTextStyle provides textStyle,
            LocalContentColor provides color,
        ) {
            Text(
                text = ZARINA_BRACKET_START.toString(),
                modifier = Modifier.padding(end = bracketPadding),
            )

            Box(contentAlignment = Alignment.Center) {
                Text(text = MINUS.toString())

                val plusAnimationProgress by animateFloatAsState(
                    targetValue = if (isExpanded) 1f else 0f
                )
                val plusRotation by remember {
                    derivedStateOf { lerp(0f, 90f, plusAnimationProgress) }
                }
                val plusAlpha by remember {
                    derivedStateOf { lerp(1f, 0f, plusAnimationProgress) }
                }

                Text(
                    text = PLUS.toString(),
                    modifier = Modifier.graphicsLayer {
                        rotationX = plusRotation
                        alpha = plusAlpha
                    },
                )
            }

            Text(
                text = ZARINA_BRACKET_END.toString(),
                modifier = Modifier.padding(start = bracketPadding),
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    ZarinaTheme2 {
        var isExpanded by remember { mutableStateOf(false) }

        ZarinaExpandableIndicator(
            isExpanded = isExpanded,
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .clickable { isExpanded = !isExpanded },
        )
    }
}

private const val PLUS = '+'
private const val MINUS = '−'
