package ru.zarina.zarina.ui.screen.productcountselector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.zarina.zarina.ui.common.component.bottomsheet.ZarinaBottomSheet
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.productcountselector.ProductCountSelectorScreenComponents.CountItem
import ru.zarina.zarina.ui.screen.productcountselector.ProductCountSelectorScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.productcountselector.ProductCountSelectorViewModel.CountItem
import ru.zarina.zarina.ui.screen.productcountselector.ProductCountSelectorViewModel.SideEffect
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ProductCountSelectorBottomSheetScreen(
    viewModel: ProductCountSelectorViewModel = hiltViewModel(),
) {
    val countItems by viewModel.countItems.collectAsStateWithLifecycle()

    ScreenContent(
        countItems = countItems,
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    countItems: ImmutableList<CountItem>,
    sideEffects: Flow<SideEffect>,
) {
    ProductCountSelectorScreenBehavior(sideEffects = sideEffects)

    ZarinaBottomSheet(
        windowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TopBar(
                onCloseClicked = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(),
            )

            val contentPadding = WindowInsets.safeDrawing
                .only(WindowInsetsSides.Bottom)
                .asPaddingValues()

            LazyColumn(contentPadding = contentPadding) {
                itemsIndexed(
                    items = countItems,
                    key = { _, item -> item.count },
                ) { index, item ->
                    CountItem(
                        item = item,
                        onClick = { /*TODO*/ },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    if (index < countItems.lastIndex) {
                        Divider(
                            color = UiKitTheme.colors.border.general.default,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )
                    }
                }
            }
        }
    }
}

@Suppress("MagicNumber")
@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            countItems = remember {
                val selectedItem = 1
                val loadingItem = 3
                List(10) { count ->
                    CountItem(
                        count = count + 1,
                        isSelected = count == selectedItem,
                        isLoading = count == loadingItem,
                    )
                }.toImmutableList()
            },
            sideEffects = remember { emptyFlow() },
        )
    }
}
