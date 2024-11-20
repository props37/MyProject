package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.uicompose.AnimatedContentCrossfadeTransitionSpec
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.MenuItem
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun Menu(
    items: ImmutableList<MenuItem>,
    onItemClicked: (MenuItem) -> Unit,
    city: City?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        items.forEachIndexed { index, item ->
            key(item) {
                Item(
                    item = item,
                    city = city,
                    onItemClicked = onItemClicked,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (index < items.lastIndex) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun Item(
    item: MenuItem,
    onItemClicked: (MenuItem) -> Unit,
    city: City?,
    modifier: Modifier = Modifier,
) {
    val itemNameResId = when (item) {
        MenuItem.MyOrders -> RCommon.string.my_orders
        MenuItem.City -> RCommon.string.city
        MenuItem.Stores -> RCommon.string.stores
        MenuItem.Help -> RCommon.string.help
        MenuItem.AboutCompany -> RCommon.string.about_company
    }

    ZarinaItem(
        onClick = { onItemClicked(item) },
        startContent = {
            val textStyle = UiKitTheme.typography.secondary.light
            Text(
                text = stringResource(itemNameResId),
                style = textStyle,
                color = UiKitTheme.colors.text.general.regular.default,
            )

            if (item == MenuItem.City) {
                Spacer(modifier = Modifier.width(8.dp))
                AnimatedContent(
                    targetState = city,
                    transitionSpec = {
                        AnimatedContentCrossfadeTransitionSpec.using(sizeTransform = null)
                    },
                    contentAlignment = Alignment.CenterStart,
                    label = "City",
                ) { city ->
                    if (city != null) {
                        Text(
                            text = city.name,
                            style = textStyle,
                            color = UiKitTheme.colors.text.general.regular.muted,
                        )
                    } else {
                        ZarinaTextSkeleton(
                            textStyle = textStyle,
                            modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                        )
                    }
                }
            }
        },
        endContent = {
            Icon(
                imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
                contentDescription = null,
                tint = UiKitTheme.colors.icon.regular.default,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(degrees = 90f),
            )
        },
        modifier = modifier,
    )
}
