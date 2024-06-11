package ru.livetyping.zarina.presentation.screen.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.user.LoyaltyCard
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.behavior.screenbrightness.ForcedScreenBrightnessBehavior
import ru.livetyping.zarina.presentation.common.behavior.screenbrightness.ScreenBrightness
import ru.livetyping.zarina.presentation.common.component.LoyaltyCardSide
import ru.livetyping.zarina.presentation.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.profile.ProfileScreenComponents.AuthorizationSuggestion
import ru.livetyping.zarina.presentation.screen.profile.ProfileScreenComponents.Info
import ru.livetyping.zarina.presentation.screen.profile.ProfileScreenComponents.LoyaltyCard
import ru.livetyping.zarina.presentation.screen.profile.ProfileScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.profile.ProfileViewModel.InfoItem
import ru.livetyping.zarina.presentation.screen.profile.ProfileViewModel.SideEffect
import ru.livetyping.zarina.presentation.screen.profile.ProfileViewModel.UserState
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec

@Composable
fun ProfileScreen(
    navigate: (ProfileScreenAction) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val loyaltyCard by viewModel.loyaltyCard.collectAsStateWithLifecycle()
    val infoItems by viewModel.infoItems.collectAsStateWithLifecycle()
    val city by viewModel.city.collectAsStateWithLifecycle()

    ScreenContent(
        userState = userState,
        onProfileDetailsClicked = viewModel::onProfileDetailsClicked,
        loyaltyCard = loyaltyCard,
        onLoyaltyCardInfoClicked = viewModel::onLoyaltyCardInfoClicked,
        infoItems = infoItems,
        city = city,
        onInfoItemClicked = viewModel::onInfoItemClicked,
        onSignInClicked = viewModel::onSignInClicked,
        onSignUpClicked = viewModel::onSignUpClicked,
        onScreenOpened = viewModel::onScreenOpened,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    userState: UserState,
    onProfileDetailsClicked: () -> Unit,
    loyaltyCard: LoyaltyCard?,
    onLoyaltyCardInfoClicked: () -> Unit,
    infoItems: ImmutableList<InfoItem>,
    city: City?,
    onInfoItemClicked: (InfoItem) -> Unit,
    onSignInClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    onScreenOpened: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProfileScreenAction) -> Unit,
) {
    ProfileScreenBehavior(
        onScreenOpened = onScreenOpened,
        sideEffects = sideEffects,
        navigate = navigate,
    )

    var screenBrightness by remember { mutableStateOf(ScreenBrightness.DEFAULT) }
    ForcedScreenBrightnessBehavior(brightness = screenBrightness)

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
        val user = (userState as? UserState.Success)?.user
        TopBar(
            userFirstName = user?.firstName,
            isEditProfileButtonVisible = user != null,
            onProfileDetailsClicked = onProfileDetailsClicked,
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            AnimatedContent(
                targetState = userState,
                transitionSpec = {
                    AnimatedContentDefaultTransitionSpec().using(SizeTransform(clip = false))
                },
                contentAlignment = Alignment.Center,
                label = "AuthorizationSuggestion/LoyaltyCard",
                modifier = Modifier.fillMaxWidth(),
            ) { userState ->
                val paddingModifier = Modifier
                    .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 32.dp)

                when (userState) {
                    is UserState.Success -> {
                        if (userState.user != null) {
                            LoyaltyCard(
                                loyaltyCard = loyaltyCard,
                                onLoyaltyCardInfoClicked = onLoyaltyCardInfoClicked,
                                onSideChanged = {
                                    screenBrightness = if (it == LoyaltyCardSide.BACK) {
                                        ScreenBrightness.MAX
                                    } else {
                                        ScreenBrightness.DEFAULT
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clipToBounds()
                                    .then(paddingModifier),
                            )
                        } else {
                            SideEffect { screenBrightness = ScreenBrightness.DEFAULT }
                            AuthorizationSuggestion(
                                onSignInClicked = onSignInClicked,
                                onSignUpClicked = onSignUpClicked,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(paddingModifier),
                            )
                        }
                    }

                    UserState.Loading -> Unit
                }
            }

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
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun PreviewUnauthorized() {
    ZarinaPreview {
        ScreenContent(
            userState = remember {
                UserState.Success(user = null)
            },
            onProfileDetailsClicked = {},
            loyaltyCard = null,
            onLoyaltyCardInfoClicked = {},
            infoItems = remember { InfoItem.entries.toImmutableList() },
            city = remember { City.DEFAULT },
            onInfoItemClicked = {},
            onSignInClicked = {},
            onSignUpClicked = {},
            onScreenOpened = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun PreviewAuthorized() {
    ZarinaPreview {
        ScreenContent(
            userState = remember {
                val user = FakeDataGenerator.getUser()
                UserState.Success(user)
            },
            onProfileDetailsClicked = {},
            loyaltyCard = remember { FakeDataGenerator.getLoyaltyCard() },
            onLoyaltyCardInfoClicked = {},
            infoItems = remember { InfoItem.entries.toImmutableList() },
            city = remember { City.DEFAULT },
            onInfoItemClicked = {},
            onSignInClicked = {},
            onSignUpClicked = {},
            onScreenOpened = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
