package ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.paging.zarinaPagingAppendItem
import ru.livetyping.zarina.presentation.common.component.paging.zarinaPagingPrependItem
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTab
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.util.rememberFormattedLocalDate
import ru.livetyping.zarina.presentation.common.util.rememberFormattedPrice
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory.BonusHistoryViewModel.Tab
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.pager.PagerTabRowIntegration

@Suppress("ConstPropertyName")
object BonusHistoryScreenComponents {

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
                Text(
                    text = stringResource(R.string.bonus_account_history),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    fun BonusHistoryTabRow(
        tabs: List<Tab>,
        selectedTab: Tab,
        onSelectedTabChanged: (Tab) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTabRow(
            selectedTabIndex = tabs.indexOf(selectedTab),
            modifier = modifier,
        ) {
            tabs.forEach { tab ->
                ZarinaTab(
                    onClick = { onSelectedTabChanged(tab) },
                    isSelected = tab == selectedTab,
                ) {
                    val textResId = when (tab) {
                        Tab.BONUS_HISTORY -> R.string.bonus_history
                        Tab.EXPECTED_BONUSES -> R.string.expected_bonuses
                    }
                    Text(
                        text = stringResource(textResId),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }

    @Composable
    fun BonusHistoryHorizontalPager(
        pagerState: PagerState,
        tabs: List<Tab>,
        selectedTab: Tab,
        onSelectedTabChanged: (Tab) -> Unit,
        bonusHistoryPagingDataFlow: Flow<PagingData<LoyaltyProgramBonusAction>>,
        expectedBonusesPagingDataFlow: Flow<PagingData<LoyaltyProgramBonusAction>>,
        modifier: Modifier = Modifier,
    ) {
        PagerTabRowIntegration(
            pagerState = pagerState,
            tabs = tabs,
            currentTab = selectedTab,
            onCurrentTabChanged = onSelectedTabChanged,
        )

        val bonusHistoryPagingItems = bonusHistoryPagingDataFlow.collectAsLazyPagingItems()
        val expectedBonusesPagingItems = expectedBonusesPagingDataFlow.collectAsLazyPagingItems()

        HorizontalPager(
            state = pagerState,
            modifier = modifier,
        ) { page ->
            val tab = tabs[page]
            val lazyPagingItems = when (tab) {
                Tab.BONUS_HISTORY -> bonusHistoryPagingItems
                Tab.EXPECTED_BONUSES -> expectedBonusesPagingItems
            }

            // TODO: [High] Display different states
            // TODO: [High] Refactor
            LazyColumn {
                zarinaPagingPrependItem(
                    prependLoadState = lazyPagingItems.loadState.prepend,
                    onRetryClicked = lazyPagingItems::retry,
                )

                // TODO: [Backend] Add keys
                items(count = lazyPagingItems.itemCount) { index ->
                    val action = lazyPagingItems[index]
                    if (action != null) {
                        Column(modifier = Modifier.animateItem()) {
                            BonusAction(action)

                            if (index < lazyPagingItems.itemCount - 1) {
                                ZarinaDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                )
                            }
                        }
                    }
                }

                zarinaPagingAppendItem(
                    appendLoadState = lazyPagingItems.loadState.append,
                    onRetryClicked = lazyPagingItems::retry,
                )
            }
        }
    }

    @Composable
    private fun BonusAction(
        action: LoyaltyProgramBonusAction,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            startContent = {
                Box(contentAlignment = Alignment.CenterStart) {
                    val typeTextResId = when (action.type) {
                        LoyaltyProgramBonusAction.Type.EARNED -> R.string.accrual
                        LoyaltyProgramBonusAction.Type.SPENT -> R.string.write_off
                    }

                    if (action.date != null) {
                        Column {
                            val formattedDate = rememberFormattedLocalDate(
                                localDate = action.date,
                                formatterPattern = DateFormatterPattern,
                            )

                            Text(
                                text = stringResource(typeTextResId),
                                style = UiKitTheme.typography.secondary.light,
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = formattedDate,
                                style = UiKitTheme.typography.footnote.light,
                                color = UiKitTheme.colors.text.general.regular.muted,
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(typeTextResId),
                            style = UiKitTheme.typography.secondary.light,
                        )
                    }
                }
            },
            endContent = {
                val formattedBonusCount = rememberFormattedPrice(action.bonusCount)
                val text = when (action.type) {
                    LoyaltyProgramBonusAction.Type.EARNED -> "+$formattedBonusCount"
                    LoyaltyProgramBonusAction.Type.SPENT -> "-$formattedBonusCount"
                }
                val style = when (action.type) {
                    LoyaltyProgramBonusAction.Type.EARNED -> UiKitTheme.typography.secondary.regular
                    LoyaltyProgramBonusAction.Type.SPENT -> UiKitTheme.typography.secondary.light
                }

                Text(
                    text = text,
                    style = style,
                    color = UiKitTheme.colors.text.general.regular.muted,
                )
            },
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            modifier = modifier,
        )
    }

    @Composable
    private fun BonusActionStartContent(
        text: String,
        date: String,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            Text(
                text = text,
                style = UiKitTheme.typography.secondary.light,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = date,
                style = UiKitTheme.typography.footnote.light,
                color = UiKitTheme.colors.text.general.regular.muted,
            )
        }
    }

    private const val DateFormatterPattern = "dd MMMM yyyy"
}
