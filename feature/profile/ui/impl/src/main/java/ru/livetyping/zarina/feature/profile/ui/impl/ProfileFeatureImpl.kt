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
import ru.livetyping.zarina.feature.profile.ui.impl.bonushistory.BonusHistoryNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.bonushistory.BonusHistoryNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.emailchange.EmailChangeNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.emailchange.EmailChangeNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.loyaltyprogram.LoyaltyProgramNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.loyaltyprogram.LoyaltyProgramNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.navigation.bonusHistoryScreen
import ru.livetyping.zarina.feature.profile.ui.impl.navigation.emailChangeScreen
import ru.livetyping.zarina.feature.profile.ui.impl.navigation.loyaltyProgramScreen
import ru.livetyping.zarina.feature.profile.ui.impl.navigation.orderListScreen
import ru.livetyping.zarina.feature.profile.ui.impl.navigation.orderScreen
import ru.livetyping.zarina.feature.profile.ui.impl.navigation.passwordChangeScreen
import ru.livetyping.zarina.feature.profile.ui.impl.navigation.phoneChangeConfirmationScreen
import ru.livetyping.zarina.feature.profile.ui.impl.navigation.phoneChangeScreen
import ru.livetyping.zarina.feature.profile.ui.impl.navigation.profileDetailsScreen
import ru.livetyping.zarina.feature.profile.ui.impl.navigation.profileScreen
import ru.livetyping.zarina.feature.profile.ui.impl.navigation.storeListScreen
import ru.livetyping.zarina.feature.profile.ui.impl.order.OrderNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.order.OrderNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.orderlist.OrderListNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.orderlist.OrderListNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.passwordchange.PasswordChangeNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.passwordchange.PasswordChangeNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.phonechange.PhoneChangeNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.phonechange.PhoneChangeNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.phonechangeconfirmation.PhoneChangeConfirmationNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.phonechangeconfirmation.PhoneChangeConfirmationNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.profiledetails.ProfileDetailsNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.profiledetails.ProfileDetailsNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.storelist.StoreListNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.storelist.StoreListNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.profile.ProfileNavActions as ProfileScreenNavActions

public class ProfileFeatureImpl : ProfileFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: ProfileFeature.NavActions,
        resultRetrievers: ProfileFeature.ProfileNavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?
    ) {
        navigation<ProfileFeature.NavEntry>(
            startDestination = ProfileFeature.NavEntry.StartNavEntry,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            val navigateUp: () -> Unit = { navController.navigateUp() }

            val profileScreenNavActions = ProfileScreenNavActions(
                onBackClicked = actions.onBackClicked,
                onSignInClicked = actions.onSignInClicked,
                onSignUpClicked = actions.onSignUpClicked,
                onProfileDetailsClicked = { navController.navigate(ProfileDetailsNavEntry) },
                onLoyaltyCardInfoClicked = { navController.navigate(LoyaltyProgramNavEntry) },
                onMyOrdersClicked = { navController.navigate(OrderListNavEntry) },
                onChangeCityClicked = actions.onChangeCityClicked,
                onStoresClicked = { navController.navigate(StoreListNavEntry) },
            )
            profileScreen(
                actions = profileScreenNavActions,
                selectedCityResultRetriever = resultRetrievers.selectedCityResultRetriever,
            )

            val profileDetailsNavActions = ProfileDetailsNavActions(
                onBackClicked = navigateUp,
                onChangePhoneClicked = { navController.navigate(PhoneChangeNavEntry) },
                onChangeEmailClicked = { navController.navigate(EmailChangeNavEntry) },
                onChangePasswordClicked = { navController.navigate(PasswordChangeNavEntry) },
                onUserSignedOut = {
                    navController.popBackStack<ProfileFeature.NavEntry.StartNavEntry>(inclusive = false)
                },
                onAccountDeleted = {
                    navController.popBackStack<ProfileFeature.NavEntry.StartNavEntry>(inclusive = false)
                }
            )
            profileDetailsScreen(profileDetailsNavActions)

            val orderListNavActions = OrderListNavActions(
                onBackClicked = navigateUp,
                onOrderClicked = { order ->
                    val orderNavEntry = OrderNavEntry.create(order.id)
                    navController.navigate(orderNavEntry)
                },
            )
            orderListScreen(orderListNavActions)

            val orderNavActions = OrderNavActions(
                onBackClicked = navigateUp,
                onPayClicked = actions.onPayClicked,
            )
            orderScreen(orderNavActions)

            val loyaltyProgramNavActions = LoyaltyProgramNavActions(
                onBackClicked = navigateUp,
                onBonusHistoryClicked = { navController.navigate(BonusHistoryNavEntry) },
            )
            loyaltyProgramScreen(loyaltyProgramNavActions)

            val storeListNavActions = StoreListNavActions(onBackClicked = navigateUp)
            storeListScreen(storeListNavActions)

            val bonusHistoryNavActions = BonusHistoryNavActions(onBackClicked = navigateUp)
            bonusHistoryScreen(bonusHistoryNavActions)

            val emailChangeNavActions = EmailChangeNavActions(
                onBackClicked = navigateUp,
                onEmailChanged = navigateUp,
            )
            emailChangeScreen(emailChangeNavActions)

            val phoneChangeNavActions = PhoneChangeNavActions(
                onBackClicked = navigateUp,
                onPhoneChangeRequested = { phone ->
                    val phoneChangeConfirmationNavEntry = PhoneChangeConfirmationNavEntry.create(phone)
                    navController.navigate(phoneChangeConfirmationNavEntry)
                },
            )
            phoneChangeScreen(phoneChangeNavActions)

            val phoneChangeConfirmationNavActions = PhoneChangeConfirmationNavActions(
                onBackClicked = navigateUp,
                onPhoneChangeConfirmed = {
                    navController.popBackStack<PhoneChangeNavEntry>(inclusive = true)
                },
            )
            phoneChangeConfirmationScreen(phoneChangeConfirmationNavActions)

            val passwordChangeNavActions = PasswordChangeNavActions(
                onBackClicked = navigateUp,
                onPasswordChanged = navigateUp,
            )
            passwordChangeScreen(passwordChangeNavActions)
        }
    }
}
