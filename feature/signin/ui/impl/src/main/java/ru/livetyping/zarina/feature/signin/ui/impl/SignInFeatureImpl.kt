package ru.livetyping.zarina.feature.signin.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import ru.livetyping.zarina.feature.signin.ui.api.SignInFeature
import ru.livetyping.zarina.feature.signin.ui.api.SignInNavActions
import ru.livetyping.zarina.feature.signin.ui.api.SignInNavEntry
import ru.livetyping.zarina.feature.signin.ui.impl.impl.navigation.signin.signInScreen
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.SignInNavActions as SignInScreenNavActions
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.SignInNavEntry as SignInScreenNavEntry

public class SignInFeatureImpl : SignInFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: SignInNavActions,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        navigation<SignInNavEntry>(
            startDestination = SignInScreenNavEntry,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            val signInScreenNavActions = SignInScreenNavActions(
                onBackClicked = { navController.navigateUp() },
            )
            signInScreen(signInScreenNavActions)
        }
    }
}
