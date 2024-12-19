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
import ru.livetyping.zarina.feature.signup.ui.impl.impl.navigation.signUpConfirmationScreen
import ru.livetyping.zarina.feature.signup.ui.impl.impl.navigation.signUpScreen
import ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation.SignUpConfirmationNavActions
import ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation.SignUpConfirmationNavParams
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.SignUpNavActions as SignUpScreenNavActions
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.SignUpNavEntry as SignUpScreenNavEntry

public class SignUpFeatureImpl : SignUpFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: SignUpNavActions,
        resultRetrievers: Unit,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?
    ) {
        navigation<SignUpNavEntry>(
            startDestination = SignUpScreenNavEntry,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            val navigateUp: () -> Unit = { navController.navigateUp() }

            val signUpScreenNavActions = SignUpScreenNavActions(
                onBackClicked = navigateUp,
                onUserCreated = { phone ->
                    val signUpConfirmationParams = SignUpConfirmationNavParams(phone)
                    val signUpConfirmationNavEntry = signUpConfirmationParams.toNavEntry()
                    navController.navigate(signUpConfirmationNavEntry)
                },
            )
            signUpScreen(signUpScreenNavActions)

            val signUpConfirmationNavActions = SignUpConfirmationNavActions(
                onBackClicked = navigateUp,
                onPhoneConfirmed = {
                    navController.popBackStack<SignUpNavEntry>(inclusive = true)
                },
            )
            signUpConfirmationScreen(signUpConfirmationNavActions)
        }
    }
}
