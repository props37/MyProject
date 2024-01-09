package ru.zarina.zarina.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.component.screen.ZarinaLoadingScreen
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.home.HomeScreenComponents.Banners
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

                is BannersState.Success -> {
                    Banners(
                        banners = bannersState.banners,
                        modifier = Modifier
                            .fillMaxSize()
                            .bottomNavBarPadding(),
                    )
                }

                is BannersState.Error -> {
                    ZarinaErrorScreen(
                        state = bannersState.errorState,
                        onRefreshClicked = { /*TODO*/ },
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
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [High] Add preview
    }
}
