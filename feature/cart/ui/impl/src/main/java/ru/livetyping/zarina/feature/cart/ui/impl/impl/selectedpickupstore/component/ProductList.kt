package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.product.ProductOrderCard
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.R

@Composable
internal fun ProductList(
    products: ImmutableList<CartProduct>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ZarinaItem {
            Text(
                text = stringResource(R.string.cart_these_products_are_available_in_this_store),
                style = UiKitTheme.typography.secondary.bold,
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(bottom = ZarinaScrollableDefaults.ScrollableBottomPadding),
        ) {
            itemsIndexed(
                items = products,
            ) { index, product ->
                ProductOrderCard(
                    name = product.name,
                    imageUrl = product.imageUrl.value,
                    size = product.size,
                    sizeRu = null,
                    height = product.height,
                    color = product.color,
                    price = product.price,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (index < products.lastIndex) {
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
