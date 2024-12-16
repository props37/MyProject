package ru.livetyping.zarina.feature.home.ui.impl.impl.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarHeightAsState
import ru.livetyping.zarina.core.uikit.screen.ZarinaLogoLoadingScreen
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.home.domain.model.Banner
import ru.livetyping.zarina.feature.home.domain.model.BannerContainer
import ru.livetyping.zarina.feature.home.domain.model.MultipleBanners
import ru.livetyping.zarina.feature.home.domain.model.SingleBanner

@Composable
internal fun Banner(
    bannerContainer: BannerContainer,
    onBannerClicked: (Banner) -> Unit,
    isOnScreen: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        var isBannerDisplayed by remember(bannerContainer) { mutableStateOf(false) }

        when (bannerContainer) {
            is SingleBanner -> {
                FullscreenBanner(
                    singleBanner = bannerContainer,
                    onBannerClicked = onBannerClicked,
                    isOnScreen = isOnScreen,
                    onBannerDisplayed = { isBannerDisplayed = true },
                    modifier = Modifier.matchParentSize(),
                )
            }

            is MultipleBanners -> {
                when (bannerContainer.arrangement) {
                    MultipleBanners.Arrangement.GRID -> {
                        GridBanners(
                            multipleBanners = bannerContainer,
                            onBannerClicked = onBannerClicked,
                            onBannerDisplayed = { isBannerDisplayed = true },
                            modifier = Modifier.matchParentSize(),
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = !isBannerDisplayed,
            enter = remember { fadeIn(tween(BannerLoaderAnimationDuration)) },
            exit = remember { fadeOut(tween(BannerLoaderAnimationDuration)) },
            modifier = Modifier.matchParentSize(),
        ) {
            ZarinaLogoLoadingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(UiKitTheme.colors.background.general.regular.default)
                    // Add bottomNavBar padding at the top to align the loader at the center
                    // of the entire screen
                    .padding(top = bottomNavBarHeightAsState().value),
            )
        }
    }
}

private const val BannerLoaderAnimationDuration = 250
