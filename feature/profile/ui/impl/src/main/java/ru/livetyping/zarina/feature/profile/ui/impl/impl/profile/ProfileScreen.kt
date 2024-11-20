package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.ProfileNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.component.Menu
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.component.TopBar
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.component.VersionDetails
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.UserState

@Composable
internal fun ProfileScreen(
    navActions: ProfileNavActions,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    ScreenContent(
        profileState = profileState,
        onProfileEvent = viewModel::onProfileEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
internal fun ScreenContent(
    profileState: ProfileState,
    onProfileEvent: (ProfileEvent) -> Unit,
    sideEffects: Flow<ProfileSideEffect>,
    navActions: ProfileNavActions,
) {
    ProfileScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
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
        val userState = profileState.userState
        val user = (userState as? UserState.Success)?.user

        TopBar(
            userFirstName = user?.firstName,
            isProfileDetailsButtonVisible = user != null,
            onProfileDetailsClicked = { onProfileEvent(ProfileEvent.ProfileDetailsClicked) },
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            // TODO: [Top] Add auth form and loyalty card

            Menu(
                items = profileState.menuItems,
                onItemClicked = { onProfileEvent(ProfileEvent.MenuItemClicked(it)) },
                city = profileState.userCity,
            )
            Spacer(modifier = Modifier.height(16.dp))

            VersionDetails(details = profileState.versionDetails)
            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        }
    }
}
