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
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.zarina.zarina.ui.common.component.bottomsheet.ZarinaBottomSheet
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.productcountselector.ProductCountSelectorScreenComponents.CountItem
import ru.zarina.zarina.ui.screen.productcountselector.ProductCountSelectorScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.productcountselector.ProductCountSelectorViewModel.SideEffect
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ProductCountSelectorBottomSheetScreen(
    viewModel: ProductCountSelectorViewModel = hiltViewModel(),
) {
    val availableCount by viewModel.availableCount.collectAsStateWithLifecycle()

    ScreenContent(
        availableCount = availableCount,
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    availableCount: Int,
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
                items(
                    count = availableCount,
                    key = { it },
                ) { count ->
                    val adjustedCount = count + 1
                    CountItem(
                        count = adjustedCount,
                        onClick = { /*TODO*/ },
                        isSelected = false,
                        isLoading = false,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    if (adjustedCount < availableCount) {
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

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            availableCount = 10,
            sideEffects = remember { emptyFlow() },
        )
    }
}
