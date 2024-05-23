package ru.livetyping.zarina.presentation.screen.profile.details

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

// TODO: [High] Close screen if the user is null

@Composable
fun ProfileDetailsScreen(
    navigate: (ProfileDetailsScreenAction) -> Unit,
    viewModel: ProfileDetailsViewModel = hiltViewModel(),
) {
    ScreenContent(
        onSignOutClicked = viewModel::onSignOutClicked,
        onDeleteAccountClicked = viewModel::onDeleteAccountClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    onSignOutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProfileDetailsScreenAction) -> Unit,
) {
    ProfileDetailsScreenBehavior(
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
        TopBar(onBackClicked = onBackClicked)

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            ZarinaButton(
                onClick = onSignOutClicked,
                colors = ZarinaButtonDefaults.outlineColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.sign_out).uppercase())
            }

            Spacer(modifier = Modifier.height(8.dp))

            ZarinaButton(
                onClick = onDeleteAccountClicked,
                colors = ZarinaButtonDefaults.backlessErrorColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.delete_account).uppercase())
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            onSignOutClicked = {},
            onDeleteAccountClicked = {},
            onBackClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {}
        )
    }
}
