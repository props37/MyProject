package ru.livetyping.zarina.core.uikit.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaExpandableItem(
    header: @Composable RowScope.() -> Unit,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onExpandedChanged: ((Boolean) -> Unit)? = null,
    confirmExpandedChange: (Boolean) -> Boolean = { true },
    backgroundColor: Color = ZarinaItemDefaults.BackgroundColor,
    contentColor: Color = ZarinaItemDefaults.ContentColor,
    titleContentPadding: PaddingValues = ZarinaExpandableItemDefaults.TitleContentPadding,
    contentPadding: PaddingValues = ZarinaExpandableItemDefaults.ContentPadding,
) {
    @Suppress("NAME_SHADOWING")
    var isExpanded by rememberSaveable(isExpanded) { mutableStateOf(isExpanded) }

    val updatedOnExpandedChanged by rememberUpdatedState(onExpandedChanged)
    LaunchedEffect(Unit) {
        snapshotFlow { isExpanded }.collect {
            updatedOnExpandedChanged?.invoke(it)
        }
    }

    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides UiKitTheme.typography.secondary.light,
    ) {
        Column(
            modifier = modifier.drawBehind { drawRect(backgroundColor) },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .defaultMinSize(minHeight = ZarinaItemDefaults.MinHeight)
                    .clickable {
                        val newValue = !isExpanded
                        if (confirmExpandedChange(newValue)) {
                            isExpanded = newValue
                        }
                    }
                    .padding(titleContentPadding),
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    content = header,
                )

                Spacer(modifier = Modifier.width(8.dp))

                val rotation = animateFloatAsState(
                    targetValue = if (isExpanded) 0f else 180f,
                    label = "rotation",
                )
                val contentDescResId = if (isExpanded) R.string.collapse else R.string.expand
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
                    contentDescription = stringResource(contentDescResId),
                    modifier = Modifier
                        .size(16.dp)
                        .graphicsLayer {
                            rotationZ = rotation.value
                        },
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Box(modifier = Modifier.padding(contentPadding)) {
                    content()
                }
            }
        }
    }
}

public object ZarinaExpandableItemDefaults {
    public val TitleContentPadding: PaddingValues
        get() = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    public val ContentPadding: PaddingValues
        get() = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
}
