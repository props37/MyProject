package ru.zarina.zarina.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.datasource.cache.Cache
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.domain.Banner
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.domain.Selection
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.MediaPager
import ru.zarina.zarina.ui.common.components.PageDots
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme


@Composable
fun HomeScreenContent(
    banners: ImmutableList<Banner>,
    selections: ImmutableList<Selection>,
    cache: State<Cache?>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.screenBackground),
    ) {
        item(
            key = LAZY_KEY_BANNERS,
            contentType = ContentType.BANNERS,
        ) {
            Banners(
                banners,
                cache,
                Modifier.statusBarsPadding(),
            )
        }
        items(
            items = selections,
            contentType = { selection ->
                when (selection) {
                    is Selection.Banners -> ContentType.BANNERS
                    is Selection.Products -> ContentType.PRODUCTS
                }
            }
        ) { selection ->
            when (selection) {
                is Selection.Banners -> Banners(
                    banners = selection.banners,
                    cache = cache
                )

                is Selection.Products -> Unit // TODO
            }
        }
    }
}

private const val LAZY_KEY_BANNERS = "lazy_key_banners"

private enum class ContentType { BANNERS, PRODUCTS }

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Banners(
    banners: ImmutableList<Banner>,
    cache: State<Cache?>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        val pagerState = rememberPagerState()
        MediaPager(
            media = banners.map { it.media }.toPersistentList(),
            aspectRatio = Media.Defaults.BANNER_MEDIA_ASPECT_RATIO,
            state = pagerState,
            cache = cache,
            modifier = Modifier.fillMaxWidth(),
        )
        PageDots(
            count = banners.size,
            activeIndex = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(10.dp),
        )
    }
}

@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()

    val banners by viewModel.banners.collectAsStateWithLifecycle()
    val selections by viewModel.selections.collectAsStateWithLifecycle()
    val cache = viewModel.cache.collectAsStateWithLifecycle()

    HomeScreenBehavior(
        sideEffects = viewModel.sideEffects,
    )

    HomeScreenContent(
        banners = banners,
        selections = selections,
        cache = cache,
    )
}

@Composable
fun HomeScreenBehavior(
    sideEffects: Flow<HomeViewModel.SideEffect>,
) {
    NavigationBarState(isVisible = true, isAnimated = true)
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> Unit
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun HomeScreenContentPreview() {
    ZarinaTheme {
        HomeScreenContent(
            banners = persistentListOf(),
            selections = persistentListOf(),
            cache = remember { mutableStateOf<Cache?>(null) },
        )
    }
}
