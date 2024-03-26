package ru.zarina.zarina.ui.screen.profile

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.profile.ProfileScreenComponents.AppVersion
import ru.zarina.zarina.ui.screen.profile.ProfileScreenComponents.AuthorizationSuggestion
import ru.zarina.zarina.ui.screen.profile.ProfileScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.profile.ProfileViewModel.SideEffect
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    ScreenContent(
        onSignInClicked = { /* TODO */ },
        onSignUpClicked = { /* TODO */ },
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    onSignInClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
) {
    ProfileScreenBehavior(sideEffects = sideEffects)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        TopBar()

        Spacer(modifier = Modifier.height(24.dp))

        AuthorizationSuggestion(
            onSignInClicked = onSignInClicked,
            onSignUpClicked = onSignUpClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Spacer(modifier = Modifier.height(16.dp))

        AppVersion(
            version = BuildConfig.VERSION_NAME,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))
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
