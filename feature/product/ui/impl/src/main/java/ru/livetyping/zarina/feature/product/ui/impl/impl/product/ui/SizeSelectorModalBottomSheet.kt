package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.uicompose.none
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaBottomSheetDefaults
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaModalBottomSheet
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.SizeSelectorItem
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.SizeSelectorState
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.SizeSelectorType
import ru.livetyping.zarina.core.resource.R as RCommon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SizeSelectorModalBottomSheet(
    state: SizeSelectorState,
    onSizeSelected: (ProductOffer, SizeSelectorType) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()

    var internalState by remember { mutableStateOf(state) }
    val internalStateValue = internalState
    LaunchedEffect(state) {
        if (state is SizeSelectorState.Hidden) {
            sheetState.hide()
        }
        internalState = state
    }

    if (internalStateValue is SizeSelectorState.Visible) {
        ZarinaModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest,
            modifier = modifier,
        ) {
            Title(
                type = internalStateValue.type,
                onCloseClicked = onDismissRequest,
            )

            internalStateValue.items.forEachIndexed { index, item ->
                Item(
                    item = item,
                    type = internalStateValue.type,
                    onClick = { onSizeSelected(item.offer, internalStateValue.type) },
                )

                if (index < internalStateValue.items.lastIndex) {
                    ZarinaDivider(modifier = Modifier.fillMaxWidth())
                }
            }

            Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        }
    }
}

@Composable
private fun Title(
    type: SizeSelectorType,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        centerContent = {
            val textResId = when (type) {
                SizeSelectorType.SIZE -> RCommon.string.res_select_size
                SizeSelectorType.HEIGHT -> RCommon.string.res_select_height
            }

            Text(text = stringResource(textResId).uppercase())
        },
        endContent = {
            ZarinaCloseIconButton(onClick = onCloseClicked)
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        windowInsets = WindowInsets.none,
        modifier = modifier,
    )
}

@Composable
private fun Item(
    item: SizeSelectorItem,
    type: SizeSelectorType,
    onClick: (SizeSelectorItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        onClick = { onClick(item) },
        contentPadding = PaddingValues(
            horizontal = ZarinaBottomSheetDefaults.HorizontalPadding,
            vertical = 8.dp,
        ),
        modifier = modifier,
    ) {
        val textStyle = UiKitTheme2.typography.body

        val text = when (type) {
            SizeSelectorType.SIZE -> item.offer.size.size.uppercase()
            SizeSelectorType.HEIGHT -> item.offer.height?.height.orEmpty()
        }
        val color =
            if (item.isSelected) UiKitTheme2.colors.mainBlack else UiKitTheme2.colors.middleGray

        Text(
            text = text,
            style = textStyle,
            color = color,
        )
    }
}
