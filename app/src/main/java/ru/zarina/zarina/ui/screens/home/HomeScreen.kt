package ru.zarina.zarina.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.domain.Banner
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.MediaPager
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContent(
    banners: ImmutableList<Banner>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.screenBackground),
    ) {
        MediaPager(
            media = banners.map { it.media }.toPersistentList(),
            aspectRatio = Media.Defaults.BANNER_MEDIA_ASPECT_RATIO,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
        )
    }
}

@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()

    val banners by viewModel.banners.collectAsStateWithLifecycle()

    HomeScreenBehavior(
        sideEffects = viewModel.sideEffects,
    )

    HomeScreenContent(
        banners = banners,
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
            banners = persistentListOf()
        )
    }
}
