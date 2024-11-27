package ru.livetyping.zarina.feature.signup.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import ru.livetyping.zarina.feature.signup.ui.api.SignUpFeature
import ru.livetyping.zarina.feature.signup.ui.api.SignUpNavActions
import ru.livetyping.zarina.feature.signup.ui.api.SignUpNavEntry
import ru.livetyping.zarina.feature.signup.ui.impl.impl.navigation.signUpScreen
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.SignUpNavActions as SignUpScreenNavActions
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.SignUpNavEntry as SignUpScreenNavEntry

public class SignUpFeatureImpl : SignUpFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: SignUpNavActions,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        navigation<SignUpNavEntry>(
            startDestination = SignUpScreenNavEntry,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            val signUpScreenNavActions = SignUpScreenNavActions(
                onBackClicked = { navController.navigateUp() },
            )
            signUpScreen(signUpScreenNavActions)
        }
    }
}
