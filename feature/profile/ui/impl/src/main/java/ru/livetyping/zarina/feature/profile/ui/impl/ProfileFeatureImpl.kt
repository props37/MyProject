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
import ru.livetyping.zarina.feature.profile.ui.ProfileNavResultRetrievers
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.BonusHistoryNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.BonusHistoryNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchange.EmailChangeNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchange.EmailChangeNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.LoyaltyProgramNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.LoyaltyProgramNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.bonusHistoryScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.emailChangeScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.loyaltyProgramScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.orderListScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.orderScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.passwordChangeScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.phoneChangeConfirmationScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.phoneChangeScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.profileDetailsScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.profileScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.storeListScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.OrderNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.OrderNavParams
import ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist.OrderListNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist.OrderListNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchange.PasswordChangeNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchange.PasswordChangeNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechange.PhoneChangeNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechange.PhoneChangeNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.PhoneChangeConfirmationNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.PhoneChangeConfirmationNavParams
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.ProfileDetailsNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.ProfileDetailsNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.StoreListNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.StoreListNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileNavActions as ProfileScreenNavActions

public class ProfileFeatureImpl : ProfileFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: ProfileNavActions,
        resultRetrievers: ProfileNavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?
    ) {
        navigation<ProfileNavEntry>(
            startDestination = ProfileNavEntry.StartNavEntry,
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
                    navController.popBackStack<ProfileNavEntry.StartNavEntry>(inclusive = false)
                },
                onAccountDeleted = {
                    navController.popBackStack<ProfileNavEntry.StartNavEntry>(inclusive = false)
                }
            )
            profileDetailsScreen(profileDetailsNavActions)

            val orderListNavActions = OrderListNavActions(
                onBackClicked = navigateUp,
                onOrderClicked = { order ->
                    val orderParams = OrderNavParams(order.id)
                    val orderNavEntry = orderParams.toNavEntry()
                    navController.navigate(orderNavEntry)
                },
            )
            orderListScreen(orderListNavActions)

            val orderNavActions = OrderNavActions(onBackClicked = navigateUp)
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
                    val phoneChangeConfirmationParams = PhoneChangeConfirmationNavParams(phone)
                    val phoneChangeConfirmationNavEntry = phoneChangeConfirmationParams.toNavEntry()
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
