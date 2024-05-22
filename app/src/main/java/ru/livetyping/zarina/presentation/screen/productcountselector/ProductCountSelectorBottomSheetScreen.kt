package ru.livetyping.zarina.presentation.screen.productcountselector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.presentation.common.component.bottomsheet.ZarinaBottomSheet
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.productcountselector.ProductCountSelectorScreenComponents.CountItem
import ru.livetyping.zarina.presentation.screen.productcountselector.ProductCountSelectorScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.productcountselector.ProductCountSelectorViewModel.CountItem
import ru.livetyping.zarina.presentation.screen.productcountselector.ProductCountSelectorViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.plus

// TODO: [Low] Put package inside ui.cart?

@Composable
fun ProductCountSelectorBottomSheetScreen(
    navigate: (ProductCountSelectorScreenAction) -> Unit,
    viewModel: ProductCountSelectorViewModel = hiltViewModel(),
) {
    val countItems by viewModel.countItems.collectAsStateWithLifecycle()

    ScreenContent(
        onCloseClicked = viewModel::onCloseClicked,
        countItems = countItems,
        onCountItemClicked = viewModel::onCountItemClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    onCloseClicked: () -> Unit,
    countItems: ImmutableList<CountItem>,
    onCountItemClicked: (CountItem) -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProductCountSelectorScreenAction) -> Unit,
) {
    ProductCountSelectorScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    ZarinaBottomSheet(
        windowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TopBar(
                onCloseClicked = onCloseClicked,
                modifier = Modifier.fillMaxWidth(),
            )

            val contentPadding = WindowInsets.safeDrawing
                .only(WindowInsetsSides.Bottom)
                .asPaddingValues()
                .plus(PaddingValues(bottom = 24.dp))

            LazyColumn(contentPadding = contentPadding) {
                itemsIndexed(
                    items = countItems,
                    key = { _, item -> item.count },
                ) { index, item ->
                    CountItem(
                        item = item,
                        onClick = onCountItemClicked,
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
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            onCloseClicked = {},
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
            onCountItemClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
