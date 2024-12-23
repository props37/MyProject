package ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.component

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.model.BonusHistoryTab

@Composable
internal fun BonusHistoryPager(
    pagerState: PagerState,
    tabs: ImmutableList<BonusHistoryTab>,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { page ->
        val tab = tabs[page]

        // TODO: [Top] Implement
    }
}
