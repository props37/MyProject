package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.SizeGuide
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.tag.ZarinaTag
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.R

@Composable
internal fun SizeGuide(
    sizeGuide: SizeGuide,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        var selectedEntry by remember { mutableStateOf(sizeGuide.entries.firstOrNull()) }

        SizeSelector(
            sizeEntries = sizeGuide.entries,
            selectedSizeEntry = selectedEntry,
            onSizeEntrySelected = { selectedEntry = it },
        )

        Spacer(modifier = Modifier.height(8.dp))

        Crossfade(
            targetState = selectedEntry,
            modifier = Modifier.fillMaxSize(),
        ) { entry ->
            if (entry != null) {
                SizeGuideImpl(entry = entry)
            }
        }
    }
}

@Composable
private fun SizeSelector(
    sizeEntries: List<SizeGuide.Entry>,
    selectedSizeEntry: SizeGuide.Entry?,
    onSizeEntrySelected: (SizeGuide.Entry) -> Unit,
    modifier: Modifier = Modifier,
) {
    val horizontalPadding = 8.dp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.horizontalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.width(horizontalPadding))

        sizeEntries.forEach { entry ->
            ZarinaTag(
                onClick = { onSizeEntrySelected(entry) },
                isSelected = entry == selectedSizeEntry,
                modifier = Modifier.heightIn(min = 44.dp),
            ) {
                Text(
                    text = entry.sizeFull.size.uppercase(),
                    style = UiKitTheme2.typography.body,
                )
            }
        }

        Spacer(modifier = Modifier.width(horizontalPadding))
    }
}

@Composable
private fun SizeGuideImpl(
    entry: SizeGuide.Entry,
    modifier: Modifier = Modifier,
) {
    val dividerModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)

    Column(modifier = modifier) {
        SizeTableItem(
            name = stringResource(R.string.product_ru_size),
            value = entry.sizeRu.size,
        )

        ZarinaDivider(modifier = dividerModifier)

        SizeTableItem(
            name = stringResource(R.string.product_bust_size),
            value = entry.bust,
        )

        ZarinaDivider(modifier = dividerModifier)

        SizeTableItem(
            name = stringResource(R.string.product_waist_size),
            value = entry.waist,
        )

        ZarinaDivider(modifier = dividerModifier)

        SizeTableItem(
            name = stringResource(R.string.product_hips_size),
            value = entry.hips,
        )

        ZarinaDivider(modifier = dividerModifier)

        SizeTableItem(
            name = stringResource(R.string.product_height),
            value = entry.height,
        )

        ZarinaDivider(modifier = dividerModifier)

        // TODO: [Top] Add guide image
    }
}
