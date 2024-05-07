package ru.livetyping.zarina.presentation.screen.shops

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.shop.Shop
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTab
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.screen.shops.ShopsViewModel.ShopListState
import ru.livetyping.zarina.presentation.screen.shops.ShopsViewModel.ViewMode
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade

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
        shopListState: ShopListState,
        onShopsErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            when (viewModes[page]) {
                ViewMode.MAP -> {
                    MapViewMode()
                }

                ViewMode.LIST -> {
                    ListViewMode(
                        shopListState = shopListState,
                        onShopsErrorRefreshClicked = onShopsErrorRefreshClicked,
                    )
                }
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
        shopListState: ShopListState,
        onShopsErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = shopListState,
            contentKey = {
                when (it) {
                    is ShopListState.Success -> ShopListContentKeySuccess
                    ShopListState.Loading, is ShopListState.Error -> it
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is ShopListState.Success -> {
                    ShopList(
                        shops = state.shops,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                ShopListState.Loading -> {
                    ShopListSkeleton(modifier = Modifier.fillMaxSize())
                }

                is ShopListState.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onShopsErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun ShopList(
        shops: ImmutableList<Shop>,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(modifier = modifier) {
            itemsIndexed(
                items = shops,
                key = { _, shop -> shop.id.value },
            ) { index, shop ->
                ShopListItem(shop = shop)

                if (index < shops.lastIndex) {
                    Divider(
                        color = UiKitTheme.colors.background.skeleton,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun ShopListSkeleton(
        modifier: Modifier = Modifier,
    ) {
        val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

        LazyColumn(modifier = modifier) {
            items(count = ShopListSkeletonItemCount) { index ->
                ShopListItemSkeleton(shimmer = shimmer)

                if (index < ShopListSkeletonItemCount - 1) {
                    Divider(
                        color = UiKitTheme.colors.background.skeleton,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun ShopListItem(
        shop: Shop,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            contentPadding = ShopListItemContentPadding,
            modifier = modifier,
        ) {
            Column {
                Text(
                    text = shop.name,
                    style = ShopListItemNameTextStyle,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = shop.address,
                    style = ShopListItemInfoTextStyle,
                )
                
                if (shop.schedule != null) {
                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = shop.schedule,
                        style = ShopListItemInfoTextStyle,
                    )
                }
            }
        }
    }

    @Composable
    private fun ShopListItemSkeleton(
        shimmer: Shimmer,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            contentPadding = ShopListItemContentPadding,
            modifier = modifier,
        ) {
            Column {
                ZarinaTextSkeleton(
                    textStyle = ShopListItemNameTextStyle,
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(fraction = 0.4f),
                )

                Spacer(modifier = Modifier.height(6.dp))

                ZarinaTextSkeleton(
                    textStyle = ShopListItemInfoTextStyle,
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(fraction = 0.7f),
                )
            }
        }
    }

    private const val ShopListContentKeySuccess = "ShopListContentKeySuccess"

    private const val ShopListSkeletonItemCount = 12

    private val ShopListItemContentPadding: PaddingValues get() = PaddingValues(16.dp)

    private val ShopListItemNameTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.light

    private val ShopListItemInfoTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.tertiary.light
}
