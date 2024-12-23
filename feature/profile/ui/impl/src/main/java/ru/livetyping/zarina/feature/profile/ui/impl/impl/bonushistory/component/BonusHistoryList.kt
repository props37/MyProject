package ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.core.domain.model.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikitpaging.zarinaPagingAppendItem
import ru.livetyping.zarina.core.uikitpaging.zarinaPagingPrependItem

@Composable
internal fun BonusHistoryList(
    pagingItems: LazyPagingItems<LoyaltyProgramBonusAction>,
    emptyPlaceholder: @Composable () -> Unit,
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
                    emptyPlaceholder = emptyPlaceholder,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            LoadState.Loading -> {
                BonusHistoryListSkeleton()
            }

            is LoadState.Error -> {
                val state = remember(loadState.error) {
                    ZarinaErrorScreenState.from(loadState.error)
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
    emptyPlaceholder: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        if (pagingItems.itemCount > 0) {
            val contentPadding = PaddingValues(
                bottom = ZarinaScrollableDefaults.ScrollableBottomPadding,
            )

            LazyColumn(
                contentPadding = contentPadding,
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
                        Column(modifier = Modifier.animateZarinaItem(this)) {
                            BonusHistoryAction(action)

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
            emptyPlaceholder()
        }
    }
}

@Composable
private fun BonusHistoryListSkeleton(
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

    LazyColumn(
        contentPadding = PaddingValues(bottom = ZarinaScrollableDefaults.ScrollableBottomPadding),
        modifier = modifier,
    ) {
        items(BonusHistorySkeletonItemCount) { index ->
            BonusHistoryActionSkeleton(shimmer = shimmer)

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

private const val BonusHistorySkeletonItemCount = 24
