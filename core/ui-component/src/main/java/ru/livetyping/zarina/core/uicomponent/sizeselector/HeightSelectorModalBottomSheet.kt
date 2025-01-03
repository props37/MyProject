package ru.livetyping.zarina.core.uicomponent.sizeselector

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
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HeightSelectorModalBottomSheet(
    heights: List<ProductOffer>,
    onHeightSelected: (ProductOffer) -> Unit,
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
                HeightSelectorHeader(
                    onBackClicked = onCloseClicked,
                    onCloseClicked = onCloseClicked,
                )

                SizeSelectorHeightList(
                    heights = heights,
                    onHeightClicked = {
                        coroutineScope.launch {
                            sheetState.hide()
                            onHeightSelected(it)
                        }
                    },
                )

                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}

@Composable
private fun HeightSelectorHeader(
    onBackClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        startContent = {
            ZarinaBackIconButton(
                onClick = onBackClicked,
                iconSize = SizeSelectorDefaults.HeaderIconSize,
                modifier = Modifier.padding(start = 2.dp),
            )
        },
        centerContent = {
            Text(
                text = stringResource(R.string.uikit_choose_height),
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
