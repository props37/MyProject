package ru.livetyping.zarina.feature.cart.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.DeliveryMethodSelectorNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.DeliveryMethodSelectorNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation.cartScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation.deliveryMethodSelectorScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation.pickupPointSelectorScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation.pickupStoreSelectorScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation.recipientScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation.selectedPickupPointScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation.selectedPickupStoreScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.PickupPointSelectorNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.PickupPointSelectorNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.PickupStoreSelectorNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.PickupStoreSelectorNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.recipient.RecipientNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.recipient.RecipientNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.SelectedPickupPointNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.SelectedPickupPointNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore.SelectedPickupStoreNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore.SelectedPickupStoreNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.CartNavActions as CartScreenNavActions

public class CartFeatureImpl : CartFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: CartFeature.NavActions,
        resultRetrievers: CartFeature.NavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?
    ) {
        navigation<CartFeature.NavEntry>(
            startDestination = CartFeature.NavEntry.StartNavEntry,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            val cartScreenNavActions = CartScreenNavActions(
                onBackClicked = actions.onBackClicked,
                onChangeCityClicked = actions.onChangeCityClicked,
                onGoToCatalogClicked = actions.onGoToCatalogClicked,
                onProductClicked = actions.onProductClicked,
                onCheckoutClicked = { cartType ->
                    val navEntry = RecipientNavEntry.from(cartType)
                    navController.navigate(navEntry)
                },
            )
            cartScreen(
                actions = cartScreenNavActions,
                selectedCityResultRetriever = resultRetrievers.selectedCityResultRetriever,
            )

            val navigateBack: () -> Unit = { navController.navigateUp() }
            val closeCheckout: () -> Unit = {
                navController.popBackStack<CartFeature.NavEntry.StartNavEntry>(inclusive = false)
            }

            val recipientNavActions = RecipientNavActions(
                onCloseClicked = closeCheckout,
                onContinueClicked = { cartType, currentStep, recipient ->
                    val deliveryMethodSelectorNavEntry = DeliveryMethodSelectorNavEntry.from(
                        cartType = cartType,
                        checkoutStep = currentStep + 1,
                        recipient = recipient,
                    )
                    navController.navigate(deliveryMethodSelectorNavEntry)
                },
            )
            recipientScreen(recipientNavActions)

            val deliveryMethodSelectorNavActions = DeliveryMethodSelectorNavActions(
                onBackClicked = navigateBack,
                onCloseClicked = closeCheckout,
                onDeliveryMethodSelected = { cartType, currentStep, recipient, deliveryMethod ->
                    val checkoutStep = currentStep + 1
                    when (deliveryMethod.type) {
                        DeliveryMethodType.COURIER_EXPRESS -> TODO()
                        DeliveryMethodType.POST -> TODO()
                        DeliveryMethodType.PICKUP_FROM_PICKUP_POINT -> {
                            val pickupPointSelectorNavEntry = PickupPointSelectorNavEntry.from(
                                cartType = cartType,
                                checkoutStep = checkoutStep,
                                recipient = recipient,
                                deliveryMethod = deliveryMethod,
                            )
                            navController.navigate(pickupPointSelectorNavEntry)
                        }

                        DeliveryMethodType.PICKUP_FROM_STORE, DeliveryMethodType.PICKUP_FROM_STORE_WAREHOUSE -> {
                            val pickupStoreSelectorNavEntry = PickupStoreSelectorNavEntry.from(
                                cartType = cartType,
                                checkoutStep = checkoutStep,
                                recipient = recipient,
                                deliveryMethod = deliveryMethod,
                            )
                            navController.navigate(pickupStoreSelectorNavEntry)
                        }

                        else -> Unit
                    }
                },
            )
            deliveryMethodSelectorScreen(deliveryMethodSelectorNavActions)

            val pickupStoreSelectorNavActions = PickupStoreSelectorNavActions(
                onBackClicked = navigateBack,
                onCloseClicked = closeCheckout,
                onStoreSelected = { cartType, currentStep, recipient, deliveryMethod, city, store, availableProducts ->
                    val selectedPickupStoreNavEntry = SelectedPickupStoreNavEntry.from(
                        cartType = cartType,
                        checkoutStep = currentStep + 1,
                        recipient = recipient,
                        deliveryMethod = deliveryMethod,
                        city = city,
                        store = store,
                        availableProducts = availableProducts,
                    )
                    navController.navigate(selectedPickupStoreNavEntry)
                },
            )
            pickupStoreSelectorScreen(pickupStoreSelectorNavActions)

            val selectedPickupStoreNavActions = SelectedPickupStoreNavActions(
                onBackClicked = navigateBack,
                onContinueClicked = { currentStep, checkoutParams ->
                    // TODO: [Top] Implement
                },
            )
            selectedPickupStoreScreen(selectedPickupStoreNavActions)

            val pickupPointSelectorNavActions = PickupPointSelectorNavActions(
                onBackClicked = navigateBack,
                onCloseClicked = closeCheckout,
                onPickupPointSelected = { cartType, currentStep, recipient, deliveryMethod, pickupPoint ->
                    val selectedPickupPointNavEntry = SelectedPickupPointNavEntry.from(
                        cartType = cartType,
                        checkoutStep = currentStep + 1,
                        recipient = recipient,
                        deliveryMethod = deliveryMethod,
                        pickupPoint = pickupPoint,
                    )
                    navController.navigate(selectedPickupPointNavEntry)
                },
            )
            pickupPointSelectorScreen(pickupPointSelectorNavActions)

            val selectedPickupPointNavActions = SelectedPickupPointNavActions()
            selectedPickupPointScreen(selectedPickupPointNavActions)
        }
    }
}
