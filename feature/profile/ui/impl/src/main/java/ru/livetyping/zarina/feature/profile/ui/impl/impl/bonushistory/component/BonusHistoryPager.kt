package ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.model.BonusHistoryTab

@Composable
internal fun BonusHistoryPager(
    pagerState: PagerState,
    tabs: ImmutableList<BonusHistoryTab>,
    bonusHistoryPagingDataFlow: Flow<PagingData<LoyaltyProgramBonusAction>>,
    expectedBonusesPagingDataFlow: Flow<PagingData<LoyaltyProgramBonusAction>>,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { page ->
        val tab = tabs[page]
        val pagingData = when (tab) {
            BonusHistoryTab.BONUS_HISTORY -> bonusHistoryPagingDataFlow
            BonusHistoryTab.EXPECTED_BONUSES -> expectedBonusesPagingDataFlow
        }
        val pagingItems = pagingData.collectAsLazyPagingItems()

        BonusHistoryList(
            pagingItems = pagingItems,
            emptyPlaceholder = {
                BonusHistoryListEmptyPlaceholder(
                    tab = tab,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                )
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
