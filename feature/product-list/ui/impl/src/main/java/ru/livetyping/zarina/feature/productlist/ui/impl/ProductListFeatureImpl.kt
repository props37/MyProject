package ru.livetyping.zarina.feature.productlist.ui.impl

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
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavEntry
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.FiltrationNavActions
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.FiltrationNavEntry
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.navigation.filtrationScreen
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.navigation.productListScreen
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ProductListNavActions
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ProductListNavEntry as ProductListScreenNavEntry

public class ProductListFeatureImpl : ProductListFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: ProductListFeature.NavActions,
        resultRetrievers: EmptyNavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        navigation<ProductListFeature.NavEntry>(
            startDestination = ProductListScreenNavEntry::class,
            typeMap = ProductListNavEntry.typeMap(),
            deepLinks = DeepLinks,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            val productListNavActions = ProductListNavActions(
                onBackClicked = actions.onBackClicked,
                onFiltersClicked = { categoryId, filters ->
                    val filtrationNavEntry = FiltrationNavEntry.create(categoryId, filters)
                    navController.navigate(filtrationNavEntry)
                },
                onTagClicked = { tag, filters ->
                    val productListNavEntry = ProductListScreenNavEntry.create(
                        categoryId = tag.id,
                        filters = filters,
                    )
                    navController.navigate(productListNavEntry)
                },
                onProductClicked = actions.onProductClicked,
                onSubscribeToProductClicked = actions.onSubscribeToProductClicked,
            )
            productListScreen(productListNavActions)

            val filtrationNavActions = FiltrationNavActions(
                onBackClicked = { navController.navigateUp() },
            )
            filtrationScreen(filtrationNavActions)
        }
    }

    private companion object {
        private val DeepLinks = buildList {
            val categoryId = ProductListNavEntry.CATEGORY_ID_PROPERTY_NAME
            ZarinaWebLinkUris.forEach { uri ->
                // TODO: [Low] Migrate to navDeepLink<ProductListNavEntry>?
                add(navDeepLink { uriPattern = "$uri/catalog/product/{$categoryId}" })
                add(navDeepLink { uriPattern = "$uri/catalog/product/{$categoryId}/" })
            }
        }
    }
}
