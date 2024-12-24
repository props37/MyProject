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
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging.EmailChangingNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging.EmailChangingNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.LoyaltyProgramNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.LoyaltyProgramNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.bonusHistoryScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.emailChangingScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.loyaltyProgramScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.orderListScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.orderScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.passwordChangingScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.phoneChangingScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.profileDetailsScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.profileScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation.storeListScreen
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.OrderNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.OrderNavParams
import ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist.OrderListNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist.OrderListNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging.PasswordChangingNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging.PasswordChangingNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.PhoneChangingNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging.PhoneChangingNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.ProfileDetailsNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.ProfileDetailsNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.StoreListNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.StoreListNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileNavActions as ProfileScreenNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.ProfileNavEntry as ProfileScreenNavEntry

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
            startDestination = ProfileScreenNavEntry,
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
                onChangePhoneClicked = { navController.navigate(PhoneChangingNavEntry) },
                onChangeEmailClicked = { navController.navigate(EmailChangingNavEntry) },
                onChangePasswordClicked = { navController.navigate(PasswordChangingNavEntry) },
                onUserSignedOut = {
                    navController.popBackStack<ProfileScreenNavEntry>(inclusive = false)
                },
                onAccountDeleted = {
                    navController.popBackStack<ProfileScreenNavEntry>(inclusive = false)
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

            val emailChangingNavActions = EmailChangingNavActions(
                onBackClicked = navigateUp,
                onEmailChanged = navigateUp,
            )
            emailChangingScreen(emailChangingNavActions)

            val phoneChangingNavActions = PhoneChangingNavActions(onBackClicked = navigateUp)
            phoneChangingScreen(phoneChangingNavActions)

            val passwordChangingNavActions = PasswordChangingNavActions(onBackClicked = navigateUp)
            passwordChangingScreen(passwordChangingNavActions)
        }
    }
}
