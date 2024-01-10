package ru.zarina.zarina.ui.screen.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import ru.zarina.zarina.domain.rework.content.HomeContent
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.component.screen.ZarinaLoadingScreen
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.home.HomeScreenComponents.ContentPager
import ru.zarina.zarina.ui.screen.home.HomeScreenComponents.TabBar
import ru.zarina.zarina.ui.screen.home.HomeViewModel.ContentState
import ru.zarina.zarina.ui.screen.home.HomeViewModel.Tab
import ru.zarina.zarina.ui.screen.home.tooling.preview.ContentStatePreviewParameterProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.Crossfade
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val tabs by viewModel.tabs.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val contentState by viewModel.contentState.collectAsStateWithLifecycle()

    ScreenContent(
        tabs = tabs,
        currentTab = currentTab,
        onTabClicked = viewModel::onTabClicked,
        contentState = contentState,
        onBannerClicked = viewModel::onBannerClicked,
        onContentErrorRefreshClicked = viewModel::onContentErrorRefreshClicked,
    )
}

@Composable
private fun ScreenContent(
    tabs: List<Tab>,
    currentTab: Tab,
    onTabClicked: (Tab) -> Unit,
    contentState: ContentState,
    onBannerClicked: (HomeContent.Banner) -> Unit,
    onContentErrorRefreshClicked: () -> Unit,
) {
    ForcedBottomNavBarBehavior(isVisible = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsReworked.background.general.regular.default),
    ) {
        Crossfade(
            targetState = contentState,
            modifier = Modifier.fillMaxSize(),
        ) { contentState ->
            when (contentState) {
                ContentState.Loading -> {
                    ZarinaLoadingScreen(modifier = Modifier.fillMaxSize())
                }

                is ContentState.Success -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // TODO: [High] Refactor
                        val tabBarHeightPx = remember { mutableIntStateOf(0) }
                        val tabBarYOffsetChannel = remember { Channel<Int>(Channel.UNLIMITED) }
                        val tabBarYOffsetAnimatable = remember {
                            Animatable(0, Int.VectorConverter, Int.VisibilityThreshold)
                        }
                        val tabBarAlpha = remember(tabBarYOffsetAnimatable) {
                            derivedStateOf {
                                (1f - (tabBarYOffsetAnimatable.value.toFloat() * 2 / -tabBarHeightPx.intValue))
                                    .coerceIn(0f, 1f)
                            }
                        }

                        LaunchedEffect(tabBarYOffsetAnimatable, tabBarYOffsetChannel) {
                            tabBarYOffsetChannel.receiveAsFlow().collect { yOffset ->
                                tabBarYOffsetAnimatable.snapTo(yOffset)
                            }
                        }

                        TabBar(
                            tabs = tabs,
                            currentTab = currentTab,
                            onTabClicked = onTabClicked,
                            modifier = Modifier
                                .zIndex(1f)
                                .align(Alignment.TopCenter)
                                .onSizeChanged { tabBarHeightPx.intValue = it.height }
                                .statusBarsPadding()
                                .padding(top = 12.dp)
                                .offset { IntOffset(0, tabBarYOffsetAnimatable.value) }
                                .graphicsLayer { alpha = tabBarAlpha.value },
                        )

                        val nestedScrollConnection = remember {
                            object : NestedScrollConnection {
                                override fun onPreScroll(
                                    available: Offset,
                                    source: NestedScrollSource,
                                ): Offset {
                                    val newTabBarYOffset = (tabBarYOffsetAnimatable.value + available.y.roundToInt())
                                        .coerceIn(-tabBarHeightPx.intValue, 0)
                                    tabBarYOffsetChannel.trySend(newTabBarYOffset)
                                    return super.onPreScroll(available, source)
                                }

                                override suspend fun onPostFling(
                                    consumed: Velocity,
                                    available: Velocity,
                                ): Velocity {
                                    val currentYOffset = tabBarYOffsetAnimatable.value
                                    val settleYOffset = if (currentYOffset <= (-tabBarHeightPx.intValue / 2)) {
                                        -tabBarHeightPx.intValue
                                    } else {
                                        0
                                    }
                                    tabBarYOffsetAnimatable.animateTo(settleYOffset)
                                    return super.onPostFling(consumed, available)
                                }
                            }
                        }

                        ContentPager(
                            tabs = tabs,
                            currentPage = tabs.indexOf(currentTab),
                            content = contentState.content,
                            onBannerClicked = onBannerClicked,
                            modifier = Modifier
                                .nestedScroll(nestedScrollConnection)
                                .fillMaxSize()
                                .bottomNavBarPadding(),
                        )
                    }
                }

                is ContentState.Error -> {
                    ZarinaErrorScreen(
                        state = contentState.errorState,
                        onRefreshClicked = onContentErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(
                                WindowInsets.statusBars
                                    .union(WindowInsets.displayCutout),
                            )
                            .bottomNavBarPadding()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                    )
                }
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview(
    @PreviewParameter(ContentStatePreviewParameterProvider::class)
    contentState: ContentState,
) {
    ZarinaPreview {
        ScreenContent(
            tabs = remember { Tab.entries.toList() },
            currentTab = Tab.FOR_WOMEN,
            onTabClicked = {},
            contentState = contentState,
            onBannerClicked = {},
            onContentErrorRefreshClicked = {},
        )
    }
}
