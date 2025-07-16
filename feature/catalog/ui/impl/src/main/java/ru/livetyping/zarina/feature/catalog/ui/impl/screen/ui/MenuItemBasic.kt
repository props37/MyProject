package ru.livetyping.zarina.feature.catalog.ui.impl.screen.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.util.lerp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.uicompose.toComposeColor
import ru.livetyping.zarina.core.uikit.indicator.ZarinaExpandableIndicator
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.text.withZarinaBrackets
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.catalog.ui.impl.screen.model.MenuItem
import kotlin.random.Random

@Composable
internal fun MenuItemBasic(
    item: MenuItem.Basic,
    onClick: (MenuItem.Basic) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (item.isHighlighted) {
            UiKitTheme2.colors.lightGray
        } else {
            UiKitTheme2.colors.white
        },
    )
    val startPadding = ContentPaddingStart + (item.nestingLevel * 24.dp)

    ZarinaItem(
        onClick = { onClick(item) },
        backgroundColor = backgroundColor,
        contentPadding = PaddingValues(
            start = startPadding,
            top = ContentPaddingTop,
            end = ContentPaddingEnd,
            bottom = ContentPaddingBottom,
        ),
        modifier = modifier,
    ) {
        val text = if (item.addBrackets) {
            item.item.title.uppercase().withZarinaBrackets()
        } else {
            item.item.title.uppercase()
        }

        val color = item.item.color?.toComposeColor() ?: Color.Unspecified

        Text(
            text = text,
            style = ItemTextStyle,
            color = color,
        )

        val label = item.item.label
        if (label != null) {
            Text(
                text = label.uppercase().withZarinaBrackets(),
                style = UiKitTheme2.typography.caption2,
                color = color,
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(start = 8.dp),
            )
        }

        if (item.item.isExpandable) {
            Spacer(modifier = Modifier.weight(1f))

            ZarinaExpandableIndicator(item.isExpanded)
        }
    }
}

@Composable
internal fun MenuItemBasicSkeleton(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        contentPadding = ContentPadding,
        modifier = modifier,
    ) {
        val widthFraction = remember {
            lerp(SkeletonMinWidthFraction, SkeletonMaxWidthFraction, Random.nextFloat())
        }

        ZarinaTextSkeleton(
            textStyle = ItemTextStyle,
            shimmer = shimmer,
            modifier = Modifier.fillMaxWidth(widthFraction),
        )
    }
}

private val ItemTextStyle: TextStyle
    @Composable
    get() = UiKitTheme2.typography.body

private val ContentPaddingStart: Dp get() = 16.dp
private val ContentPaddingTop: Dp get() = 4.dp
private val ContentPaddingEnd: Dp get() = 16.dp
private val ContentPaddingBottom: Dp get() = 4.dp

private val ContentPadding: PaddingValues
    get() = PaddingValues(
        start = ContentPaddingStart,
        top = ContentPaddingTop,
        end = ContentPaddingEnd,
        bottom = ContentPaddingBottom,
    )

private const val SkeletonMinWidthFraction = 0.2f
private const val SkeletonMaxWidthFraction = 0.5f
