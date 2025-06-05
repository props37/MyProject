package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.core.uikit.blur.StatusBarBlur
import ru.livetyping.zarina.core.uikit.blur.StatusBarBlurDefaults
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductState

@Composable
internal fun ProductSuccess(
    state: ProductState.Success,
    onEvent: (ProductEvent) -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    bottomPaddingProvider: @Composable () -> Dp,
    modifier: Modifier = Modifier,
) {
    val topBarScrollBehavior = CollapsingTopBarDefaults.rememberEnterAlwaysScrollBehavior(
        scrollBeforeContent = { false },
    )
    val listState = rememberLazyListState()

    Box(modifier = modifier) {
        val hazeState = rememberHazeState(StatusBarBlurDefaults.isStatusBarBlurEnabled())

        StatusBarBlur(
            hazeState = hazeState,
            modifier = Modifier.zIndex(1f),
        )

        CollapsingTopBarLayout(
            topBar = {
                val firstVisibleItemKey by remember(listState) {
                    derivedStateOf { listState.layoutInfo.visibleItemsInfo.firstOrNull()?.key }
                }
                val backgroundAlpha by animateFloatAsState(
                    targetValue = if (firstVisibleItemKey == ContentListKey.MediaPager) 0f else 1f,
                )

                TopBar(
                    onBackClicked = { onEvent(ProductEvent.BackClicked) },
                    backgroundAlphaProvider = { backgroundAlpha },
                )
            },
            scrollBehavior = topBarScrollBehavior,
            modifier = Modifier.hazeSource(hazeState),
        ) {
            val contentPadding = PaddingValues(
                top = windowInsetsProvider().asPaddingValues().calculateTopPadding(),
                bottom = bottomPaddingProvider(),
            )

            ContentList(
                state = state,
                onEvent = onEvent,
                listState = listState,
                contentPadding = contentPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
            )
        }
    }
}

@Composable
private fun ContentList(
    state: ProductState.Success,
    onEvent: (ProductEvent) -> Unit,
    listState: LazyListState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        item(key = ContentListKey.MediaPager, contentType = ContentListContentType.MediaPager) {
            MediaPager(mediaList = state.product.media)
        }

        item(key = ContentListKey.ProductName, contentType = ContentListContentType.ProductName) {
            ProductName(
                state.product.name.uppercase(),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 16.dp),
            )
        }
    }
}

private enum class ContentListKey {
    MediaPager,
    ProductName,
}

private enum class ContentListContentType {
    MediaPager,
    ProductName,
}
