package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.tag.ZarinaTag
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model.Size
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model.SizeState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SizeRow(
    state: SizeState.Success,
    onSizeClicked: (Size) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier,
    ) {
        items(
            items = state.sizes,
            key = { it.offer.barcode.value },
        ) { size ->
            ZarinaTag(
                onClick = { onSizeClicked(size) },
                isEnabled = size.offer.isAvailableInStores,
                isSelected = size.isSelected,
            ) {
                val heightText = size.offer.height?.let {
                    stringResource(RCommon.string.res_height_cm, it)
                }
                val text = rememberSizeText(size, heightText)
                Text(text = text)
            }
        }
    }
}

@Composable
private fun rememberSizeText(size: Size, heightText: String?): String {
    val offer = size.offer
    val isHeightVisible = size.isHeightVisible
    return remember(offer, isHeightVisible, heightText) {
        buildString {
            append(offer.size)
            if (offer.sizeRu != null) {
                append(" ${offer.sizeRu}")
            }
            if (isHeightVisible && heightText != null) {
                append(" — $heightText")
            }
        }
    }
}
