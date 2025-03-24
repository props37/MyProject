package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun Store(
    store: PickupStore,
    cartItemCount: Int,
    onClick: (PickupStore) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable { onClick(store) }
            .padding(ContentPadding),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = store.store.name,
                style = UiKitTheme.typography.secondary.light,
            )

            Spacer(modifier = Modifier.height(6.dp))

            val productAvailabilityText = if (store.availableItemCount == cartItemCount) {
                stringResource(R.string.cart_all_products_are_available)
            } else {
                pluralStringResource(
                    id = R.plurals.cart_available_products,
                    count = cartItemCount,
                    store.availableItemCount,
                    cartItemCount,
                )
            }
            Text(
                text = productAvailabilityText,
                style = UiKitTheme.typography.tertiary.regular,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = store.store.address,
                style = UiKitTheme.typography.tertiary.light,
            )

            store.store.schedule?.let { schedule ->
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = schedule,
                    style = UiKitTheme.typography.tertiary.light,
                )
            }
        }

        Icon(
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
            contentDescription = stringResource(R.string.cart_select_store),
            tint = UiKitTheme.colors.icon.regular.default,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(16.dp)
                .rotate(degrees = 90f),
        )
    }
}

@Composable
internal fun StoreSkeleton(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(ContentPadding)) {
        ZarinaTextSkeleton(
            textStyle = UiKitTheme.typography.secondary.light,
            shimmer = shimmer,
            modifier = Modifier.width(140.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        ZarinaTextSkeleton(
            textStyle = UiKitTheme.typography.tertiary.regular,
            shimmer = shimmer,
            modifier = Modifier.width(170.dp),
        )
        Spacer(modifier = Modifier.height(10.dp))
        ZarinaTextSkeleton(
            textStyle = UiKitTheme.typography.tertiary.light,
            shimmer = shimmer,
            modifier = Modifier.width(190.dp),
        )
        Spacer(modifier = Modifier.height(6.dp))
        ZarinaTextSkeleton(
            textStyle = UiKitTheme.typography.tertiary.light,
            shimmer = shimmer,
            modifier = Modifier.width(150.dp),
        )
    }
}

private val ContentPadding: PaddingValues
    get() = PaddingValues(16.dp)
