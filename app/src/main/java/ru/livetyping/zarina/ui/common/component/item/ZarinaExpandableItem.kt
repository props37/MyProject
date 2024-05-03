package ru.livetyping.zarina.ui.common.component.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun ZarinaExpandableItem(
    title: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onExpandedChanged: ((Boolean) -> Unit)? = null,
    confirmExpandedChange: (Boolean) -> Boolean = { true },
    backgroundColor: Color = BackgroundColor,
    contentColor: Color = ContentColor,
    titleContentPadding: PaddingValues = TitleContentPadding,
    contentPadding: PaddingValues = ContentPadding,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    @Suppress("NAME_SHADOWING")
    var isExpanded by remember(isExpanded) { mutableStateOf(isExpanded) }

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
        Column(modifier = modifier.background(backgroundColor)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .defaultMinSize(minHeight = MinHeight)
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
                    content = title,
                )

                Spacer(modifier = Modifier.width(8.dp))

                val rotation = animateFloatAsState(
                    targetValue = if (isExpanded) 0f else 180f,
                    label = "rotation",
                )
                val contentDescResId = if (isExpanded) R.string.collapse else R.string.expand
                Icon(
                    painter = painterResource(R.drawable.ic_small_arrow_up_24),
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

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        ZarinaExpandableItem(
            title = {
                Text(text = "Подробнее об изделии")
            },
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.Blue),
            )
        }
    }
}

private val BackgroundColor: Color
    @Composable
    get() = UiKitTheme.colors.background.general.regular.default

private val ContentColor: Color
    @Composable
    get() = UiKitTheme.colors.text.general.regular.default

private val TitleContentPadding: PaddingValues
    get() = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

private val ContentPadding: PaddingValues
    get() = PaddingValues(horizontal = 16.dp, vertical = 12.dp)

private val MinHeight: Dp get() = 56.dp
