package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.model.product.ProductHeight
import ru.livetyping.zarina.core.domain.model.product.ProductSizeEn
import ru.livetyping.zarina.core.domain.model.product.SizeGuide
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.ProductMeasurementsState
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.ViewMode

@Composable
internal fun ViewModePager(
    pagerState: PagerState,
    viewModes: ImmutableList<ViewMode>,
    productMeasurementsState: ProductMeasurementsState,
    onProductMeasurementsSizeSelected: (ProductSizeEn) -> Unit,
    onProductMeasurementsHeightSelected: (ProductHeight) -> Unit,
    sizeGuide: SizeGuide,
    gender: Gender,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
        modifier = modifier,
    ) { page ->
        when (viewModes[page]) {
            ViewMode.PRODUCT_MEASUREMENTS -> {
                ProductMeasurements(
                    state = productMeasurementsState,
                    onSizeSelected = onProductMeasurementsSizeSelected,
                    onHeightSelected = onProductMeasurementsHeightSelected,
                    windowInsetsProvider = windowInsetsProvider,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            ViewMode.SIZE_GUIDE -> {
                SizeGuide(
                    sizeGuide = sizeGuide,
                    gender = gender,
                    windowInsetsProvider = windowInsetsProvider,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
