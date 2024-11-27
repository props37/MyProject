package ru.livetyping.zarina.feature.profile.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import ru.livetyping.zarina.feature.profile.ui.ProfileFeature
import ru.livetyping.zarina.feature.profile.ui.ProfileNavActions
import ru.livetyping.zarina.feature.profile.ui.ProfileNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.profileDetailsScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.profileScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.ProfileDetailsNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.ProfileDetailsNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileNavActions as ProfileScreenNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileNavEntry as ProfileScreenNavEntry

public class ProfileFeatureImpl : ProfileFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: ProfileNavActions,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?
    ) {
        navigation<ProfileNavEntry>(
            startDestination = ProfileScreenNavEntry,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            val profileScreenNavActions = ProfileScreenNavActions(
                onSignInClicked = actions.onSignInClicked,
                onSignUpClicked = actions.onSignUpClicked,
                onProfileDetailsClicked = { navController.navigate(ProfileDetailsNavEntry) },
                onChangeCityClicked = actions.onChangeCityClicked,
            )
            profileScreen(profileScreenNavActions)

            val profileDetailsNavActions = ProfileDetailsNavActions()
            profileDetailsScreen(profileDetailsNavActions)
        }
    }
}
