package ru.livetyping.zarina.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.profile.ProfileScreenComponents.AuthorizationSuggestion
import ru.livetyping.zarina.ui.screen.profile.ProfileScreenComponents.Info
import ru.livetyping.zarina.ui.screen.profile.ProfileScreenComponents.TopBar
import ru.livetyping.zarina.ui.screen.profile.ProfileViewModel.InfoItem
import ru.livetyping.zarina.ui.screen.profile.ProfileViewModel.SideEffect
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun ProfileScreen(
    navigate: (ProfileScreenAction) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val infoItems by viewModel.infoItems.collectAsStateWithLifecycle()
    val city by viewModel.city.collectAsStateWithLifecycle()

    ScreenContent(
        infoItems = infoItems,
        city = city,
        onInfoItemClicked = viewModel::onInfoItemClicked,
        onSignInClicked = { /* TODO */ },
        onSignUpClicked = viewModel::onSignUpClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    infoItems: ImmutableList<InfoItem>,
    city: City?,
    onInfoItemClicked: (InfoItem) -> Unit,
    onSignInClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProfileScreenAction) -> Unit,
) {
    ProfileScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(),
    ) {
        TopBar()

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(24.dp))

            AuthorizationSuggestion(
                onSignInClicked = onSignInClicked,
                onSignUpClicked = onSignUpClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(32.dp))

            Info(
                infoItems = infoItems,
                city = city,
                onInfoItemClicked = onInfoItemClicked,
                appVersion = BuildConfig.VERSION_NAME,
            )
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
