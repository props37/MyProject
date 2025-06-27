package ru.livetyping.zarina.feature.product.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import ru.livetyping.zarina.core.deeplink.ZarinaWebLinkUris
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.AvailabilityInStoresNavActions
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.AvailabilityInStoresNavEntry
import ru.livetyping.zarina.feature.product.ui.impl.impl.navigation.availabilityInStoresScreen
import ru.livetyping.zarina.feature.product.ui.impl.impl.navigation.productScreen
import ru.livetyping.zarina.feature.product.ui.impl.impl.navigation.sizeTableScreen
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.ProductNavActions
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.SizeTableNavActions
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.SizeTableNavEntry

public class ProductFeatureImpl : ProductFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: ProductFeature.NavActions,
        resultRetrievers: EmptyNavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        navigation<ProductFeature.NavEntry>(
            startDestination = ProductFeature.NavEntry.StartNavEntry::class,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            val navigateBack: () -> Unit = { navController.navigateUp() }

            val productNavActions = ProductNavActions(
                onBackClicked = actions.onBackClicked,
                onCheckAvailabilityInStoresClicked = { product ->
                    val availabilityInStoresNavEntry = AvailabilityInStoresNavEntry.from(product)
                    navController.navigate(availabilityInStoresNavEntry)
                },
                onSubscribeToProductClicked = actions.onSubscribeToProductClicked,
                onProductClicked = actions.onProductClicked,
                onSizeTableClicked = { measurements, modelInfo, sizeGuide, gender ->
                    val sizeTableNavEntry = SizeTableNavEntry.from(measurements, modelInfo, sizeGuide, gender)
                    navController.navigate(sizeTableNavEntry)
                },
            )
            productScreen(productNavActions, DeepLinks)

            val availabilityInStoresNavActions = AvailabilityInStoresNavActions(
                onBackClicked = navigateBack,
            )
            availabilityInStoresScreen(availabilityInStoresNavActions)

            val sizeTableNavActions = SizeTableNavActions(
                onBackClicked = navigateBack,
            )
            sizeTableScreen(sizeTableNavActions)
        }
    }

    internal companion object {
        val DeepLinks by lazy {
            buildList {
                val productId = ProductFeature.NavEntry.StartNavEntry.PRODUCT_ID_PROPERTY_NAME
                ZarinaWebLinkUris.forEach { uri ->
                    add(navDeepLink { uriPattern = "$uri/catalog/product/{$productId}" })
                    add(navDeepLink { uriPattern = "$uri/catalog/product/{$productId}/" })
                }
            }
        }
    }
}
