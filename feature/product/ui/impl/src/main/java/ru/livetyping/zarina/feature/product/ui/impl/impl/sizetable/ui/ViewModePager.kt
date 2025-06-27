package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.model.product.SizeGuide
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.ViewMode

@Composable
internal fun ViewModePager(
    pagerState: PagerState,
    viewModes: ImmutableList<ViewMode>,
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
                // TODO: [Top] Implement
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
