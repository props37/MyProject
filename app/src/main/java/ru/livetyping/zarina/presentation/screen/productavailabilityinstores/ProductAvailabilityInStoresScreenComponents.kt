package ru.livetyping.zarina.presentation.screen.productavailabilityinstores

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.tag.ZarinaTag
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.screen.productavailabilityinstores.ProductAvailabilityInStoresViewModel.OfferItem

object ProductAvailabilityInStoresScreenComponents {

    @Composable
    fun TopBar(
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
                Text(
                    text = stringResource(R.string.availability_in_stores)
                )
            },
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    fun Offers(
        offers: ImmutableList<OfferItem>,
        onOfferClicked: (OfferItem) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = modifier,
        ) {
            items(
                items = offers,
                key = { it.offer.barcode.value },
            ) { item ->
                ZarinaTag(
                    onClick = { onOfferClicked(item) },
                    isSelected = item.isSelected,
                ) {
                    val offer = item.offer
                    val heightText = offer.height?.let { stringResource(R.string.height_cm, it) }
                    val text = remember(offer, item.isHeightVisible, heightText) {
                        buildString {
                            append(offer.size)
                            if (offer.sizeRu != null) {
                                append(" ${offer.sizeRu}")
                            }
                            if (item.isHeightVisible && heightText != null) {
                                append(" — $heightText")
                            }
                        }
                    }
                    Text(text = text)
                }
            }
        }
    }
}
