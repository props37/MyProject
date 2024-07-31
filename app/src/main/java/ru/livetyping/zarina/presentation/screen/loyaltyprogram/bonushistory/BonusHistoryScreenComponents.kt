package ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.paging.zarinaPagingAppendItem
import ru.livetyping.zarina.presentation.common.component.paging.zarinaPagingPrependItem
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTab
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
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
        bonusHistoryPagingItems: LazyPagingItems<LoyaltyProgramBonusAction>,
        expectedBonusesPagingItems: LazyPagingItems<LoyaltyProgramBonusAction>,
        modifier: Modifier = Modifier,
    ) {
        PagerTabRowIntegration(
            pagerState = pagerState,
            tabs = tabs,
            currentTab = selectedTab,
            onCurrentTabChanged = onSelectedTabChanged,
        )

        HorizontalPager(
            state = pagerState,
            modifier = modifier,
        ) { page ->
            val tab = tabs[page]
            val pagingItems = when (tab) {
                Tab.BONUS_HISTORY -> bonusHistoryPagingItems
                Tab.EXPECTED_BONUSES -> expectedBonusesPagingItems
            }

            BonusHistoryList(
                pagingItems = pagingItems,
                noBonusHistoryPlaceholder = {
                    BonusHistoryListEmptyPlaceholder(tab)
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    @Composable
    private fun BonusHistoryList(
        pagingItems: LazyPagingItems<LoyaltyProgramBonusAction>,
        noBonusHistoryPlaceholder: @Composable () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = pagingItems.loadState.refresh,
            label = "BonusHistoryList",
            modifier = modifier,
        ) { loadState ->
            when (loadState) {
                is LoadState.NotLoading -> {
                    BonusHistoryListImpl(
                        pagingItems = pagingItems,
                        noBonusHistoryPlaceholder = noBonusHistoryPlaceholder,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                LoadState.Loading -> {
                    BonusHistoryListSkeleton()
                }

                is LoadState.Error -> {
                    val state = remember(loadState.error) {
                        ErrorState.from(loadState.error)
                    }

                    ZarinaErrorScreen(
                        state = state,
                        onButtonClicked = pagingItems::retry,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun BonusHistoryListImpl(
        pagingItems: LazyPagingItems<LoyaltyProgramBonusAction>,
        noBonusHistoryPlaceholder: @Composable () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            if (pagingItems.itemCount > 0) {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    zarinaPagingPrependItem(
                        prependLoadState = pagingItems.loadState.prepend,
                        onRetryClicked = pagingItems::retry,
                    )

                    // TODO: [Backend] Add keys
                    items(count = pagingItems.itemCount) { index ->
                        val action = pagingItems[index]
                        if (action != null) {
                            Column(modifier = Modifier.animateItem()) {
                                BonusAction(action)

                                if (index < pagingItems.itemCount - 1) {
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
                        appendLoadState = pagingItems.loadState.append,
                        onRetryClicked = pagingItems::retry,
                    )
                }
            } else {
                noBonusHistoryPlaceholder()
            }
        }
    }

    @Composable
    private fun BonusHistoryListSkeleton(
        modifier: Modifier = Modifier,
    ) {
        val shimmer = rememberZarinaSkeletonShimmer()
        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = modifier,
        ) {
            items(BonusHistorySkeletonItemCount) { index ->
                Column {
                    ZarinaItem(
                        startContent = {
                            Column {
                                ZarinaTextSkeleton(
                                    textStyle = BonusActionTitleTextStyle,
                                    shimmer = shimmer,
                                    modifier = Modifier.width(100.dp),
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                ZarinaTextSkeleton(
                                    textStyle = BonusActionDescriptionTextStyle,
                                    shimmer = shimmer,
                                    modifier = Modifier.width(120.dp),
                                )
                            }
                        },
                        endContent = {
                            ZarinaTextSkeleton(
                                textStyle = BonusActionTitleTextStyle,
                                shimmer = shimmer,
                                modifier = Modifier.width(80.dp),
                            )
                        },
                        contentPadding = PaddingValues(16.dp),
                    )

                    if (index < BonusHistorySkeletonItemCount - 1) {
                        ZarinaDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun BonusHistoryListEmptyPlaceholder(
        tab: Tab,
        modifier: Modifier = Modifier,
    ) {
        val titleResId = when (tab) {
            Tab.BONUS_HISTORY -> R.string.bonus_history_is_empty
            Tab.EXPECTED_BONUSES -> R.string.expected_bonuses_list_is_empty
        }
        val bodyResId = when (tab) {
            Tab.BONUS_HISTORY -> R.string.bonus_history_will_be_displayed_here
            Tab.EXPECTED_BONUSES -> R.string.expected_bonuses_will_be_displayed_here
        }
        val textColor = UiKitTheme.colors.text.general.regular.default

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(titleResId),
                style = UiKitTheme.typography.primary.bold,
                color = textColor,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(bodyResId),
                style = UiKitTheme.typography.secondary.regular,
                color = textColor,
                textAlign = TextAlign.Center,
            )
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
                                style = BonusActionTitleTextStyle,
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = formattedDate,
                                style = BonusActionDescriptionTextStyle,
                                color = UiKitTheme.colors.text.general.regular.muted,
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(typeTextResId),
                            style = BonusActionTitleTextStyle,
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

    private val BonusActionTitleTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.light

    private val BonusActionDescriptionTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.footnote.light

    private const val DateFormatterPattern = "dd MMMM yyyy"

    private const val BonusHistorySkeletonItemCount = 30
}
