package ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory.BonusHistoryScreenComponents.BonusHistoryHorizontalPager
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory.BonusHistoryScreenComponents.BonusHistoryTabRow
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory.BonusHistoryScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory.BonusHistoryViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun BonusHistoryScreen(
    navigate: (BonusHistoryScreenAction) -> Unit,
    viewModel: BonusHistoryViewModel = hiltViewModel(),
) {
    ScreenContent(
        onBackClicked = viewModel::onBackClicked,
        bonusHistoryPagingDataFlow = viewModel.bonusHistoryPagingDataFlow,
        expectedBonusesPagingDataFlow = viewModel.expectedBonusesPagingDataFlow,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    onBackClicked: () -> Unit,
    bonusHistoryPagingDataFlow: Flow<PagingData<LoyaltyProgramBonusAction>>,
    expectedBonusesPagingDataFlow: Flow<PagingData<LoyaltyProgramBonusAction>>,
    sideEffects: Flow<SideEffect>,
    navigate: (BonusHistoryScreenAction) -> Unit,
) {
    BonusHistoryScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
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
        TopBar(onBackClicked = onBackClicked)

        val tabs = remember { BonusHistoryViewModel.Tab.entries }
        var selectedTab by remember {
            mutableStateOf(BonusHistoryViewModel.Tab.BONUS_HISTORY)
        }

        BonusHistoryTabRow(
            tabs = tabs,
            selectedTab = selectedTab,
            onSelectedTabChanged = { selectedTab = it },
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        BonusHistoryHorizontalPager(
            pagerState = rememberPagerState { tabs.size },
            tabs = tabs,
            selectedTab = selectedTab,
            onSelectedTabChanged = { selectedTab = it },
            bonusHistoryPagingDataFlow = bonusHistoryPagingDataFlow,
            expectedBonusesPagingDataFlow = expectedBonusesPagingDataFlow,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
