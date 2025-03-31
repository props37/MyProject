package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
internal fun PickupPoint(
    pickupPoint: PickupPoint,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        onClick = onClick,
        startContent = {
            Column {
                Text(
                    text = pickupPoint.title,
                    style = UiKitTheme.typography.secondary.light,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = pickupPoint.title,
                    style = UiKitTheme.typography.footnote.light,
                    color = UiKitTheme.colors.text.general.regular.muted,
                )
            }
        },
        endContent = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(degrees = 90f),
            )
        },
        contentPadding = ContentPadding,
        modifier = modifier,
    )
}

@Composable
internal fun PickupPointSkeleton(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        contentPadding = ContentPadding,
        modifier = modifier,
    ) {
        Column {
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.secondary.light,
                modifier = Modifier.fillMaxWidth(fraction = 0.7f),
                shimmer = shimmer,
            )
            Spacer(modifier = Modifier.height(6.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.footnote.light,
                modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                shimmer = shimmer,
            )
        }
    }
}

private val ContentPadding: PaddingValues
    get() = PaddingValues(vertical = 12.dp, horizontal = 16.dp)
