package ru.livetyping.zarina.ui.screen.shops

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.ui.common.component.tab.ZarinaTab
import ru.livetyping.zarina.ui.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.ui.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.ui.screen.shops.ShopsViewModel.ViewMode

object ShopsScreenComponents {

    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(text = stringResource(R.string.shops))
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ViewModeTabRow(
        viewModes: ImmutableList<ViewMode>,
        currentViewMode: ViewMode,
        onViewModeChanged: (ViewMode) -> Unit,
        viewModePagerState: PagerState,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTabRow(
            selectedTabIndex = viewModePagerState.currentPage,
            modifier = modifier,
        ) {
            viewModes.forEach { mode ->
                val textResId = when (mode) {
                    ViewMode.MAP -> R.string.map
                    ViewMode.LIST -> R.string.list
                }
                ZarinaTab(
                    text = stringResource(textResId),
                    onClick = { onViewModeChanged(mode) },
                    isSelected = mode == currentViewMode,
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ViewModePager(
        viewModes: ImmutableList<ViewMode>,
        pagerState: PagerState,
        modifier: Modifier = Modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            when (viewModes[page]) {
                ViewMode.MAP -> MapViewMode()
                ViewMode.LIST -> ListViewMode()
            }
        }
    }

    @Composable
    private fun MapViewMode(
        modifier: Modifier = Modifier,
    ) {
        // TODO: [High] Implement
    }

    @Composable
    private fun ListViewMode(
        modifier: Modifier = Modifier,
    ) {
        // TODO: [High] Implement
    }
}
