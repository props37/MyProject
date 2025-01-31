package ru.livetyping.zarina.core.uikit.sizeselector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.uicompose.none
import ru.livetyping.zarina.core.uikit.R
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaModalBottomSheet
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar

@Composable
public fun SizeSelectorModalBottomSheet(
    state: SizeSelectorState,
    onEvent: (SizeSelectorEvent) -> Unit,
) {
    if (state is SizeSelectorState.Visible) {
        val product = state.product

        val mode = remember(product) { mutableStateOf<Mode>(Mode.SizeSelector) }

        val sizes = remember(product) {
            buildList {
                product.offers
                    .groupBy {
                        if (it.sizeRu != null) "${it.size} ${it.sizeRu}" else it.size
                    }
                    .forEach { (size, offers) ->
                        val item = SizeSelectorSizeItem(size, offers)
                        add(item)
                    }
            }
        }

        when (val modeValue = mode.value) {
            Mode.SizeSelector -> {
                SizeSelectorModalBottomSheetImpl(
                    sizes = sizes,
                    onSizeClicked = {
                        val offers = it.offers
                        if (offers.size > 1) {
                            mode.value = Mode.HeightSelector(offers)
                        } else {
                            val firstOffer = offers.firstOrNull()
                            if (firstOffer != null) {
                                onEvent(SizeSelectorEvent.SizeSelected(product, firstOffer))
                            }
                        }
                    },
                    onDismissRequest = { onEvent(SizeSelectorEvent.DismissRequested) },
                )
            }

            is Mode.HeightSelector -> {
                HeightSelectorModalBottomSheet(
                    heights = modeValue.heights,
                    onHeightSelected = {
                        onEvent(SizeSelectorEvent.SizeSelected(product, it))
                    },
                    onDismissRequest = { mode.value = Mode.SizeSelector },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SizeSelectorModalBottomSheetImpl(
    sizes: List<SizeSelectorSizeItem>,
    onSizeClicked: (SizeSelectorSizeItem) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val onCloseClicked: () -> Unit = {
        coroutineScope
            .launch { sheetState.hide() }
            .invokeOnCompletion { onDismissRequest() }
    }

    ZarinaModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        backgroundColor = Color.Unspecified,
        windowInsets = WindowInsets.none,
    ) {
        SizeSelectorScaffold(onCloseClicked = onCloseClicked) {
            Column {
                SizeSelectorHeader(onCloseClicked = onCloseClicked)

                SizeSelectorSizeList(
                    sizes = sizes,
                    onSizeClicked = {
                        coroutineScope.launch {
                            sheetState.hide()
                            onSizeClicked(it)
                        }
                    },
                )

                Spacer(
                    modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing),
                )
            }
        }
    }
}

@Composable
private fun SizeSelectorHeader(
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        centerContent = {
            Text(
                text = stringResource(R.string.uikit_choose_size),
                style = SizeSelectorDefaults.HeaderTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        endContent = {
            ZarinaCloseIconButton(
                onClick = onCloseClicked,
                iconSize = SizeSelectorDefaults.HeaderIconSize,
                modifier = Modifier.padding(end = 2.dp),
            )
        },
        contentPadding = SizeSelectorDefaults.HeaderContentPadding,
        modifier = modifier,
    )
}

@Stable
private sealed class Mode {
    data object SizeSelector : Mode()

    @Immutable
    data class HeightSelector(
        val heights: List<ProductOffer>,
    ) : Mode()
}
