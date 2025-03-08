package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.component.AuthorizationOrLoyaltyCard
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.component.BuildInfo
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.component.ProfileMenu
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.component.ProfileTopBar
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileUserState

@Composable
internal fun ProfileScreen(
    navActions: ProfileNavActions,
    viewModel: ProfileViewModel,
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    ScreenContent(
        profileState = profileState,
        onProfileEvent = viewModel::onProfileEvent,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    profileState: ProfileState,
    onProfileEvent: (ProfileEvent) -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<ProfileSideEffect>,
    navActions: ProfileNavActions,
) {
    ProfileScreenBehavior(
        onBackClicked = onBackClicked,
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
        val user = (userState as? ProfileUserState.Success)?.user

        ProfileTopBar(
            userFirstName = user?.firstName,
            isProfileDetailsButtonVisible = user != null,
            onProfileDetailsClicked = { onProfileEvent(ProfileEvent.ProfileDetailsClicked) },
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            AuthorizationOrLoyaltyCard(
                userState = profileState.userState,
                loyaltyCard = profileState.loyaltyCard,
                onSignInClicked = { onProfileEvent(ProfileEvent.SignInClicked) },
                onSignUpClicked = { onProfileEvent(ProfileEvent.SignUpClicked) },
                onLoyaltyCardInfoClicked = { onProfileEvent(ProfileEvent.LoyaltyCardInfoClicked) },
                modifier = Modifier.fillMaxWidth(),
            )

            ProfileMenu(
                items = profileState.menuItems,
                onItemClicked = { onProfileEvent(ProfileEvent.MenuItemClicked(it)) },
                city = profileState.userCity,
            )
            Spacer(modifier = Modifier.height(16.dp))

            BuildInfo(
                buildInfo = profileState.buildInfo,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        }
    }
}
