package ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTab
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryViewModel.ViewMode

object CheckoutPickupPointDeliveryComponents {

    @Composable
    fun ViewModeTabRow(
        modes: List<ViewMode>,
        currentMode: ViewMode,
        onModeChanged: (ViewMode) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTabRow(
            selectedTabIndex = modes.indexOf(currentMode),
            modifier = modifier,
        ) {
            modes.forEach { mode ->
                val textResId = when (mode) {
                    ViewMode.MAP -> R.string.map
                    ViewMode.LIST -> R.string.list
                }

                ZarinaTab(
                    text = stringResource(textResId),
                    isSelected = mode == currentMode,
                    onClick = { onModeChanged(mode) },
                )
            }
        }
    }

    @Composable
    fun ViewModeHorizontalPager(
        pagerState: PagerState,
        viewModes: List<ViewMode>,
        modifier: Modifier = Modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            val viewMode = viewModes[page]
            // TODO: [High] Implement
        }
    }
}
