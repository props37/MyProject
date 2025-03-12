package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.domain.model.product.ProductAvailabilityInStore
import ru.livetyping.zarina.core.uicommon.nameResId
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
internal fun StoreItem(
    availability: ProductAvailabilityInStore,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        contentPadding = ContentPadding,
        modifier = modifier,
    ) {
        Column {
            val store = availability.store
            Text(
                text = store.name,
                style = StoreNameTextStyle,
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (
                availability.amount == ProductAvailabilityInStore.Amount.LAST_CHANCE
                || availability.amount == ProductAvailabilityInStore.Amount.LITTLE
            ) {
                Text(
                    text = stringResource(availability.amount.nameResId),
                    style = UiKitTheme.typography.tertiary.regular,
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            Text(
                text = store.address,
                style = StoreDetailsTextStyle,
                color = UiKitTheme.colors.text.general.regular.muted,
            )

            val schedule = store.schedule
            if (schedule != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = schedule,
                    style = StoreDetailsTextStyle,
                    color = UiKitTheme.colors.text.general.regular.muted,
                )
            }
        }
    }
}

@Composable
internal fun StoreItemSkeleton(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        contentPadding = ContentPadding,
        modifier = modifier,
    ) {
        Column {
            ZarinaTextSkeleton(
                textStyle = StoreNameTextStyle,
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(0.45f),
            )
            Spacer(modifier = Modifier.height(4.dp))
            ZarinaTextSkeleton(
                textStyle = StoreDetailsTextStyle,
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(0.5f),
            )
            Spacer(modifier = Modifier.height(4.dp))
            ZarinaTextSkeleton(
                textStyle = StoreDetailsTextStyle,
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(0.35f),
            )
        }
    }
}

private val ContentPadding: PaddingValues
    get() = PaddingValues(horizontal = 16.dp, vertical = 12.dp)

private val StoreNameTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.light

private val StoreDetailsTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.tertiary.light
