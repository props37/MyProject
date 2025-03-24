package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun DeliveryMethod(
    method: DeliveryMethod,
    onClick: (DeliveryMethod) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        onClick = { onClick(method) },
        contentPadding = ContentPadding,
        startContent = {
            Column {
                Text(
                    text = method.name,
                    style = UiKitTheme.typography.secondary.light,
                )

                method.description?.let {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = it,
                        style = UiKitTheme.typography.footnote.light,
                    )
                }
            }
        },
        endContent = {
            Icon(
                imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
                contentDescription = stringResource(R.string.cart_select_delivery_method),
                tint = UiKitTheme.colors.icon.regular.default,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(degrees = 90f),
            )
        },
        modifier = modifier,
    )
}

@Composable
internal fun DeliveryMethodSkeleton(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        contentPadding = ContentPadding,
        startContent = {
            Column {
                ZarinaTextSkeleton(
                    shimmer = shimmer,
                    textStyle = UiKitTheme.typography.secondary.light,
                    modifier = Modifier.width(200.dp),
                )
                Spacer(modifier = Modifier.height(6.dp))
                ZarinaTextSkeleton(
                    shimmer = shimmer,
                    textStyle = UiKitTheme.typography.footnote.light,
                    modifier = Modifier.width(260.dp),
                )
            }
        },
        endContent = {
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier.size(16.dp),
            )
        },
        modifier = modifier,
    )
}

private val ContentPadding: PaddingValues
    get() = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
