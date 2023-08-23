package ru.zarina.zarina.ui.common.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import ru.zarina.zarina.R

@Composable
fun CollapsibleContainer(
    header: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var isCollapsed by rememberSaveable { mutableStateOf(true) }
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { isCollapsed = !isCollapsed }
                )
        ) {
            header()
            CollapseButton(isCollapsed = isCollapsed)
        }
        if (!isCollapsed) content()
    }
}

@Composable
fun CollapseButton(
    isCollapsed: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
    ) {
        val iconRotationDegrees by animateFloatAsState(
            targetValue = if (isCollapsed) 90f else 0f,
            label = "icon rotation degrees"
        )
        Icon(
            painter = painterResource(R.drawable.ic_minus_24),
            contentDescription = null,
            modifier = Modifier.graphicsLayer {
                rotationZ = iconRotationDegrees * 2
            }
        )
        Icon(
            painter = painterResource(R.drawable.ic_minus_24),
            contentDescription = null,
            modifier = Modifier.graphicsLayer {
                rotationZ = iconRotationDegrees
            }
        )
    }
}
