package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.product.ProductHeight
import ru.livetyping.zarina.core.domain.model.product.ProductMeasurement
import ru.livetyping.zarina.core.domain.model.product.ProductSizeEn
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.feature.product.ui.impl.R
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.ProductMeasurementsState

@Composable
internal fun ProductMeasurements(
    state: ProductMeasurementsState,
    onSizeSelected: (ProductSizeEn) -> Unit,
    onHeightSelected: (ProductHeight) -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    val topBarScrollBehavior = CollapsingTopBarDefaults.rememberEnterAlwaysScrollBehavior()

    CollapsingTopBarLayout(
        topBar = {
            Column(modifier = Modifier.padding(top = 20.dp)) {
                SizeSelector(
                    sizes = state.sizes,
                    selectedSize = state.selectedSize,
                    onSizeSelected = { onSizeSelected(it) },
                )

                if (state.heights != null) {
                    Spacer(modifier = Modifier.height(8.dp))

                    HeightSelector(
                        heights = state.heights,
                        selectedHeight = state.selectedHeight,
                        onHeightSelected = { onHeightSelected(it) },
                    )
                }
            }
        },
        scrollBehavior = topBarScrollBehavior,
        modifier = modifier.clipToBounds(),
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .nestedScroll(topBarScrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState()),
        ){
            Spacer(modifier = Modifier.height(8.dp))

            Crossfade(
                targetState = state.measurements,
                modifier = Modifier.fillMaxSize(),
            ) { measurements ->
                if (measurements != null) {
                    ProductMeasurementsImpl(measurements = measurements)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // TODO: [Top] Implement

            Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
            Spacer(modifier = Modifier.windowInsetsBottomHeight(windowInsetsProvider()))
        }
    }
}

@Composable
private fun SizeSelector(
    sizes: ImmutableList<ProductSizeEn>,
    selectedSize: ProductSizeEn?,
    onSizeSelected: (ProductSizeEn) -> Unit,
    modifier: Modifier = Modifier,
) {
    val horizontalPadding = 8.dp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.horizontalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.width(horizontalPadding))

        sizes.forEach { size ->
            SizeSelectorTag(
                text = size.size,
                onClick = { onSizeSelected(size) },
                isSelected = size == selectedSize,
            )
        }

        Spacer(modifier = Modifier.width(horizontalPadding))
    }
}

@Composable
private fun HeightSelector(
    heights: ImmutableList<ProductHeight>,
    selectedHeight: ProductHeight?,
    onHeightSelected: (ProductHeight) -> Unit,
    modifier: Modifier = Modifier,
) {
    val horizontalPadding = 8.dp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.horizontalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.width(horizontalPadding))

        heights.forEach { height ->
            SizeSelectorTag(
                text = stringResource(R.string.product_measurements_height_selector_item, height.height),
                onClick = { onHeightSelected(height) },
                isSelected = height == selectedHeight,
            )
        }

        Spacer(modifier = Modifier.width(horizontalPadding))
    }
}

@Composable
private fun ProductMeasurementsImpl(
    measurements: ImmutableList<ProductMeasurement>,
    modifier: Modifier = Modifier,
) {
    val dividerModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)

    Column(modifier = modifier) {
        measurements.forEach { measurement ->
            SizeTableItem(
                name = measurement.title,
                value = measurement.value,
            )

            ZarinaDivider(modifier = dividerModifier)
        }
    }
}
