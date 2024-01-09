package ru.zarina.zarina.ui.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.zarina.zarina.domain.rework.content.HomeBanners
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.component.screen.ZarinaLoadingScreen
import ru.zarina.zarina.ui.screen.home.HomeViewModel.BannersState
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.Crossfade

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val bannersState by viewModel.bannersState.collectAsStateWithLifecycle()

    ScreenContent(
        bannersState = bannersState,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScreenContent(
    bannersState: BannersState,
) {
    ForcedBottomNavBarBehavior(isVisible = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsReworked.background.general.regular.default),
    ) {
        Crossfade(
            targetState = bannersState,
            modifier = Modifier.fillMaxSize(),
        ) { bannersState ->
            when (bannersState) {
                BannersState.Loading -> {
                    ZarinaLoadingScreen(modifier = Modifier.fillMaxSize())
                }

                is BannersState.Banners -> {
                    val pagerState = rememberPagerState { 2 }
                    HorizontalPager(
                        state = pagerState,
                        beyondBoundsPageCount = 0,
                        userScrollEnabled = false,
                        modifier = Modifier.fillMaxSize(),
                    ) { pageIndex ->
                        val listState = rememberLazyListState()
                        val flingBehavior = rememberSnapFlingBehavior(listState)
                        val items = when (pageIndex) {
                            0 -> bannersState.banners.womenBanners
                            1 -> bannersState.banners.menBanners
                            else -> error("Unknown page $pageIndex")
                        }

                        LazyColumn(
                            state = listState,
                            flingBehavior = flingBehavior,
                            modifier = Modifier
                                .fillMaxSize()
                                .bottomNavBarPadding(),
                        ) {
                            items(
                                items = items,
                                key = { it.id.value },
                                contentType = { null }, // TODO: [High] Implement
                            ) { banner ->
                                when (banner.mediaType) {
                                    HomeBanners.Banner.MediaType.IMAGE -> {
                                        AsyncImage(
                                            model = banner.mediaUrl.value,
                                            contentDescription = null, // TODO: [High] Implement
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillParentMaxSize(),
                                        )
                                    }

                                    HomeBanners.Banner.MediaType.VIDEO -> {
                                        // TODO: [High] Implement
                                        Box(modifier = Modifier.fillParentMaxSize())
                                    }
                                }
                            }
                        }
                    }
                }

                is BannersState.Error -> {
                    ZarinaErrorScreen(
                        state = bannersState.errorState,
                        onRefreshClicked = { /*TODO*/ },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    // TODO: [High] Add preview
}
