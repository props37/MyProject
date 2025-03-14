package ru.livetyping.zarina.feature.signin.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.signin.ui.api.SignInFeature
import ru.livetyping.zarina.feature.signin.ui.impl.impl.navigation.passwordRecoveryScreen
import ru.livetyping.zarina.feature.signin.ui.impl.impl.navigation.signInByEmailPhoneConfirmationScreen
import ru.livetyping.zarina.feature.signin.ui.impl.impl.navigation.signInByPhonePhoneConfirmationScreen
import ru.livetyping.zarina.feature.signin.ui.impl.impl.navigation.signInScreen
import ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery.PasswordRecoveryNavActions
import ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery.PasswordRecoveryNavEntry
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail.SignInByEmailPhoneConfirmationNavActions
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail.SignInByEmailPhoneConfirmationNavEntry
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone.SignInByPhonePhoneConfirmationNavActions
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone.SignInByPhonePhoneConfirmationNavEntry
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.SignInNavActions as SignInScreenNavActions
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.SignInNavEntry as SignInScreenNavEntry

public class SignInFeatureImpl : SignInFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: SignInFeature.NavActions,
        resultRetrievers: EmptyNavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?
    ) {
        navigation<SignInFeature.NavEntry>(
            startDestination = SignInScreenNavEntry,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            val navigateUp: () -> Unit = { navController.navigateUp() }

            val signInScreenNavActions = SignInScreenNavActions(
                onBackClicked = navigateUp,
                onUserSignedIn = { navController.popBackStack<SignInFeature.NavEntry>(inclusive = true) },
                onSignInByPhoneRequested = { phone ->
                    val phoneConfirmationNavEntry = SignInByPhonePhoneConfirmationNavEntry.create(phone)
                    navController.navigate(phoneConfirmationNavEntry)
                },
                onForgotPasswordClicked = { navController.navigate(PasswordRecoveryNavEntry) },
                onSignUpClicked = actions.onSignUpClicked,
                onSignInByEmailPhoneConfirmationNeeded = { phone ->
                    val phoneConfirmationNavEntry = SignInByEmailPhoneConfirmationNavEntry.create(phone)
                    navController.navigate(phoneConfirmationNavEntry)
                },
            )
            signInScreen(signInScreenNavActions)

            val passwordRecoveryNavActions = PasswordRecoveryNavActions(
                onBackClicked = navigateUp,
                onPasswordResetRequested = navigateUp,
            )
            passwordRecoveryScreen(passwordRecoveryNavActions)

            val signInByPhonePhoneConfirmationNavActions = SignInByPhonePhoneConfirmationNavActions(
                onBackClicked = navigateUp,
                onPhoneConfirmed = { navController.popBackStack<SignInFeature.NavEntry>(inclusive = true) },
            )
            signInByPhonePhoneConfirmationScreen(signInByPhonePhoneConfirmationNavActions)

            val signInByEmailPhoneConfirmationNavActions = SignInByEmailPhoneConfirmationNavActions()
            signInByEmailPhoneConfirmationScreen(signInByEmailPhoneConfirmationNavActions)
        }
    }
}
