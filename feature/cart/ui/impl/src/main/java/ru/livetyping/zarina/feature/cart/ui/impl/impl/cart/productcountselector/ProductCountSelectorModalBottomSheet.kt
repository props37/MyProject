package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.productcountselector

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uicompose.none
import ru.livetyping.zarina.core.uicompose.plus
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaModalBottomSheet
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.icon.ZarinaCheckmarkIcon
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductCountSelectorModalBottomSheet(
    state: ProductCountSelectorState,
    onEvent: (ProductCountSelectorEvent) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    @Suppress("NAME_SHADOWING")
    val state by rememberUpdatedState(state)

    var actualState by remember {
        mutableStateOf<ProductCountSelectorState>(ProductCountSelectorState.None)
    }

    LaunchedEffect(Unit) {
        snapshotFlow { state }.collect { state ->
            if (state is ProductCountSelectorState.None && sheetState.isVisible) {
                sheetState.hide()
            }
            actualState = state
        }
    }

    val actualStateValue = actualState
    if (actualStateValue is ProductCountSelectorState.ProductCountSelector) {
        ZarinaModalBottomSheet(
            onDismissRequest = { onEvent(ProductCountSelectorEvent.DismissRequested) },
            sheetState = sheetState,
            modifier = modifier.statusBarsPadding(),
            windowInsets = WindowInsets.none,
        ) {
            Content(
                state = actualStateValue,
                onEvent = onEvent,
                sheetState = sheetState,
                windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: ProductCountSelectorState.ProductCountSelector,
    onEvent: (ProductCountSelectorEvent) -> Unit,
    sheetState: SheetState,
    windowInsets: WindowInsets,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        TopBar(
            onCloseClicked = {
                coroutineScope
                    .launch { sheetState.hide() }
                    .invokeOnCompletion { onEvent(ProductCountSelectorEvent.DismissRequested) }
            },
        )

        val windowInsetsPadding = windowInsets.asPaddingValues()
        val additionalPadding = PaddingValues(
            bottom = ZarinaScrollableDefaults.ScrollableBottomPadding,
        )
        val contentPadding = windowInsetsPadding
            .plus(additionalPadding, LocalLayoutDirection.current)

        LazyColumn(contentPadding = contentPadding) {
            itemsIndexed(
                items = state.countItems,
                key = { _, item -> item.count },
            ) { index, item ->
                CountItem(
                    item = item,
                    onClick = {
                        val event = ProductCountSelectorEvent.CountItemClicked(
                            product = state.product,
                            countItem = item,
                        )
                        onEvent(event)
                    },
                )

                if (index < state.countItems.lastIndex) {
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
private fun TopBar(
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        centerContent = {
            Text(
                text = stringResource(R.string.cart_select_count),
                style = UiKitTheme.typography.primary.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        endContent = {
            ZarinaCloseIconButton(
                onClick = onCloseClicked,
                iconSize = 20.dp,
                modifier = Modifier.padding(end = 2.dp),
            )
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}

@Composable
private fun CountItem(
    item: ProductCountItem,
    onClick: (ProductCountItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        startContent = {
            Text(
                text = item.count.toString(),
                style = UiKitTheme.typography.secondary.light,
                color = UiKitTheme.colors.text.general.regular.default,
            )
        },
        endContent = {
            val state = when {
                item.isLoading -> CountItemState.Loading
                item.isSelected -> CountItemState.Selected
                else -> CountItemState.Default
            }

            @Suppress("NAME_SHADOWING")
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    AnimatedContentDefaultTransitionSpec.using(sizeTransform = null)
                },
                contentAlignment = Alignment.Center,
                label = "CountItem icon",
            ) { state ->
                when (state) {
                    CountItemState.Default -> Unit
                    CountItemState.Selected -> {
                        ZarinaCheckmarkIcon(
                            isVisible = true,
                            iconSize = 16.dp,
                        )
                    }

                    CountItemState.Loading -> {
                        ZarinaCircularLoader(
                            strokeWidth = 1.dp,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }
        },
        onClick = { onClick(item) },
        modifier = modifier,
    )
}

private enum class CountItemState { Default, Selected, Loading }
