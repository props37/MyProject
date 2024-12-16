package ru.livetyping.zarina.feature.profile.ui.impl.impl.order.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.uicompose.AnimatedContentCrossfadeTransitionSpec
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.profile.ui.impl.R

@Composable
internal fun OrderTopBar(
    orderNumber: Order.Number?,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        startContent = {
            ZarinaBackIconButton(
                onClick = onBackClicked,
                iconSize = 20.dp,
                modifier = Modifier.padding(start = 2.dp),
            )
        },
        centerContent = {
            AnimatedContent(
                targetState = orderNumber,
                transitionSpec = {
                    AnimatedContentCrossfadeTransitionSpec.using(sizeTransform = null)
                },
                contentAlignment = Alignment.Center,
                label = "Order number",
            ) { number ->
                if (number != null) {
                    Text(
                        text = stringResource(R.string.profile_order_number, number.value),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    ZarinaTextSkeleton(
                        textStyle = LocalTextStyle.current,
                        modifier = Modifier.width(96.dp),
                    )
                }
            }
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}
