package ru.livetyping.zarina.ui.screen.product

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.domain.common.Media
import ru.livetyping.zarina.ui.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.ui.common.component.media.ZarinaMediaHorizontalPager
import ru.livetyping.zarina.ui.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.pager.rememberEndlessPagerState

object ProductScreenComponents {

    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        productName: String?,
        mode: TopBarMode,
        modifier: Modifier = Modifier,
    ) {
        val backgroundColor by animateColorAsState(
            targetValue = when (mode) {
                TopBarMode.Transparent -> Color.Unspecified
                TopBarMode.Filled -> UiKitTheme.colors.background.general.regular.default
            },
            label = "background color",
        )

        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                val alpha by animateFloatAsState(
                    targetValue = when (mode) {
                        TopBarMode.Transparent -> 0f
                        TopBarMode.Filled -> 1f
                    },
                    label = "product name alpha",
                )

                Text(
                    text = productName.orEmpty(),
                    color = UiKitTheme.colors.text.general.regular.default.copy(alpha = alpha),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            backgroundColor = backgroundColor,
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @Composable
    fun ProductDetailsList(
        lazyListState: LazyListState,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = modifier,
        ) {
            item(
                key = ProductDetailsListKeyMediaPager,
                contentType = ProductDetailsListContentTypeMediaPager,
            ) {
                MediaPagerItem(medias = emptyList()) // TODO: [High] Implement
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun MediaPagerItem(
        medias: List<Media>,
        modifier: Modifier = Modifier,
    ) {
        val pagerState = rememberEndlessPagerState(itemCount = medias.size)
        ZarinaMediaHorizontalPager(
            pagerState = pagerState,
            medias = medias,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(MediaPagerAspectRatio),
        )
    }

    @Composable
    fun topBarModeAsState(lazyListState: LazyListState): State<TopBarMode> {
        return remember {
            derivedStateOf {
                val visibleItemsInfo = lazyListState.layoutInfo.visibleItemsInfo
                val firstVisibleItemKey = visibleItemsInfo.firstOrNull()?.key
                if (firstVisibleItemKey == ProductDetailsListKeyMediaPager) {
                    TopBarMode.Transparent
                } else {
                    TopBarMode.Filled
                }
            }
        }
    }

    enum class TopBarMode { Transparent, Filled }

    private const val MediaPagerAspectRatio = 0.7f

    private const val ProductDetailsListKeyMediaPager = "ProductDetailsListKeyMediaPager"

    private const val ProductDetailsListContentTypeMediaPager =
        "ProductDetailsListContentTypeMediaPager"
}
