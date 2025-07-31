package ru.livetyping.zarina.feature.cityselector.ui.impl.screen.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.uikit.icon.ZarinaCheckmarkIcon
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.cityselector.ui.impl.screen.model.CityListItem
import kotlin.random.Random

@Composable
internal fun CityListItem(
    item: CityListItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        onClick = onClick,
        contentPadding = ContentPadding,
        startContent = {
            Text(
                text = item.city.name.uppercase(),
                style = DefaultTextStyle,
            )
        },
        endContent = {
            ZarinaCheckmarkIcon(
                isVisible = item.isSelected,
            )
        },
        modifier = modifier,
    )
}

@Composable
internal fun CityListItemSkeleton(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(modifier = modifier) {
        val widthFraction = remember {
            val progress = Random.nextFloat()
            lerp(SkeletonMinWidthFraction, SkeletonMaxWidthFraction, progress)
        }

        ZarinaTextSkeleton(
            textStyle = DefaultTextStyle,
            shimmer = shimmer,
            modifier = Modifier.fillMaxWidth(widthFraction),
        )
    }
}

private val ContentPadding: PaddingValues
    get() = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

private val DefaultTextStyle: TextStyle
    @Composable
    get() = UiKitTheme2.typography.body

private const val SkeletonMinWidthFraction = 0.3f
private const val SkeletonMaxWidthFraction = 0.7f
