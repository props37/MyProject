package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListState

@Composable
internal fun StoreListListViewMode(
    listStateProvider: () -> StoreListState,
    onStoreListEvent: (StoreListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = listStateProvider(),
        contentKey = {
            when (it) {
                is StoreListState.Success -> StoreListListViewModeContentKey.Success
                is StoreListState.Error, StoreListState.Loading -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is StoreListState.Success -> {
                StoreListSuccess(state)
            }

            StoreListState.Loading -> {
                StoreListLoading()
            }

            is StoreListState.Error -> {
                ZarinaErrorScreen(
                    state = state.state,
                    onButtonClicked = { onStoreListEvent(StoreListEvent.ErrorRefreshClicked) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun StoreListSuccess(
    storeListState: StoreListState.Success,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = ZarinaScrollableDefaults.ScrollableBottomPadding),
        modifier = modifier,
    ) {
        itemsIndexed(
            items = storeListState.stores,
            key = { _, store -> store.id.value },
        ) { index, store ->
            Column(modifier = Modifier.animateZarinaItem(this)) {
                StoreListItem(store)

                if (index < storeListState.stores.lastIndex) {
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
private fun StoreListLoading(
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

    LazyColumn(
        contentPadding = PaddingValues(bottom = ZarinaScrollableDefaults.ScrollableBottomPadding),
        modifier = modifier,
    ) {
        items(count = StoreListSkeletonItemCount) { index ->
            Column {
                StoreListItemSkeleton(shimmer = shimmer)

                if (index < StoreListSkeletonItemCount - 1) {
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
private fun StoreListItem(
    store: Store,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        contentPadding = StoreListItemContentPadding,
        modifier = modifier,
    ) {
        Column {
            Text(
                text = store.name,
                style = StoreListItemNameTextStyle,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = store.address,
                style = StoreListItemInfoTextStyle,
            )

            val schedule = store.schedule
            if (schedule != null) {
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = schedule,
                    style = StoreListItemInfoTextStyle,
                )
            }
        }
    }
}

@Composable
private fun StoreListItemSkeleton(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        contentPadding = StoreListItemContentPadding,
        modifier = modifier,
    ) {
        Column {
            ZarinaTextSkeleton(
                textStyle = StoreListItemNameTextStyle,
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(fraction = 0.4f),
            )

            Spacer(modifier = Modifier.height(6.dp))

            ZarinaTextSkeleton(
                textStyle = StoreListItemInfoTextStyle,
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(fraction = 0.7f),
            )
        }
    }
}

private enum class StoreListListViewModeContentKey { Success }

private val StoreListItemContentPadding: PaddingValues
    get() = PaddingValues(16.dp)

private val StoreListItemNameTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.light

private val StoreListItemInfoTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.tertiary.light

private const val StoreListSkeletonItemCount = 12
