package ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.core.uicompose.pager.rememberPagerStateWithTabRow
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.component.BonusHistoryPager
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.component.BonusHistoryTabRow
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.component.BonusHistoryTopBar
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.model.BonusHistoryTab

@Composable
internal fun BonusHistoryScreen(
    navActions: BonusHistoryNavActions,
    viewModel: BonusHistoryViewModel = hiltViewModel(),
) {
    val tabRowState by viewModel.tabRowState.collectAsStateWithLifecycle()

    ScreenContent(
        tabRowState = tabRowState,
        onTabRowEvent = viewModel::onTabRowEvent,
        bonusHistoryPagingDataFlow = viewModel.bonusHistoryPagingDataFlow,
        expectedBonusesPagingDataFlow = viewModel.expectedBonusesPagingDataFlow,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
internal fun ScreenContent(
    tabRowState: TabRowState<BonusHistoryTab>,
    onTabRowEvent: (TabRowEvent<BonusHistoryTab>) -> Unit,
    bonusHistoryPagingDataFlow: Flow<PagingData<LoyaltyProgramBonusAction>>,
    expectedBonusesPagingDataFlow: Flow<PagingData<LoyaltyProgramBonusAction>>,
    onBackClicked: () -> Unit,
    sideEffects: Flow<BonusHistorySideEffect>,
    navActions: BonusHistoryNavActions,
) {
    BonusHistoryScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(),
    ) {
        BonusHistoryTopBar(onBackClicked = onBackClicked)

        BonusHistoryTabRow(
            state = tabRowState,
            onEvent = onTabRowEvent,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        val pagerState = rememberPagerStateWithTabRow(
            tabs = tabRowState.tabs,
            currentTab = tabRowState.currentTab,
            onTabChanged = { onTabRowEvent(TabRowEvent.TabChanged(it)) },
            pageCount = { tabRowState.tabs.size },
        )

        BonusHistoryPager(
            pagerState = pagerState,
            tabs = tabRowState.tabs,
            bonusHistoryPagingDataFlow = bonusHistoryPagingDataFlow,
            expectedBonusesPagingDataFlow = expectedBonusesPagingDataFlow,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
