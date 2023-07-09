package ru.zarina.zarina.ui.navigation.graphs

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Catalog
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.ui.screens.catalog.categories.CategoriesScreen
import ru.zarina.zarina.ui.screens.catalog.filters.FilterType
import ru.zarina.zarina.ui.screens.catalog.filters.FiltersScreen
import ru.zarina.zarina.ui.screens.catalog.filters.list.ListFilterScreen
import ru.zarina.zarina.ui.screens.catalog.products.ProductsScreen
import ru.zarina.zarina.ui.screens.catalog.selectshop.SelectShopScreen
import ru.zarina.zarina.ui.screens.catalog.selectsort.SelectSortScreen

fun NavGraphBuilder.catalogGraph(
    navController: NavController,
) {
    navigationGraph(Catalog) {
        composableDestination(Catalog.Categories) {
            CategoriesScreen(
                showProducts = { categoryId ->
                    val arguments = Catalog.Products.Arguments(categoryId)
                    navController.navigate(Catalog.Products.createRoute(arguments))
                },
            )
        }
        composableDestination(Catalog.Products) {
            ProductsScreen(
                savedStateHandle = remember(it) {
                    it.savedStateHandle
                        .apply {
                            set(
                                Catalog.Products.ARGUMENT_CATEGORY_ID,
                                it.arguments?.getInt(Catalog.Products.ARGUMENT_CATEGORY_ID)
                            )
                        }
                },
                showProduct = { id ->
                    val arguments = Destinations.Product.Arguments(id)
                    navController.navigate(Destinations.Product.createRoute(arguments))
                },
                showSelectSort = {
                    navController.navigate(Catalog.SelectSort.routeSchema)
                },
                showFilters = {
                    navController.navigate(Catalog.Filters.routeSchema)
                },
                goBack = {
                    navController.popBackStack(Catalog.Products.routeSchema, true)
                }
            )
        }
        bottomSheetDestination(Catalog.SelectSort) {
            val productSavedStateHandle =
                remember(it) { navController.getBackStackEntry(Catalog.Products.routeSchema).savedStateHandle }
            SelectSortScreen(
                productSavedStateHandle = productSavedStateHandle,
                goBack = {
                    navController.popBackStack(Catalog.SelectSort.routeSchema, true)
                }
            )
        }
        composableDestination(Catalog.Filters) {
            val productSavedStateHandle =
                remember(it) { navController.getBackStackEntry(Catalog.Products.routeSchema).savedStateHandle }
            FiltersScreen(
                savedStateHandle = remember(it) { it.savedStateHandle },
                productsSavedStateHandle = productSavedStateHandle,
                showColorFilter = { type ->
                    when (type) {
                        FilterType.PICKUP_SHOP -> navController.navigate(Catalog.SelectPickupShop.routeSchema)
                        else -> {
                            val arguments = Catalog.ListFilter.Arguments(filterType = type)
                            navController.navigate(Catalog.ListFilter.createRoute(arguments))
                        }
                    }
                },
                goBack = {
                    navController.popBackStack(Catalog.Filters.routeSchema, true)
                }
            )
        }
        composableDestination(Catalog.ListFilter) {
            val filtersSavedStateHandle =
                remember(it) { navController.getBackStackEntry(Catalog.Filters.routeSchema).savedStateHandle }
            ListFilterScreen(
                filtersSavedStateHandle = filtersSavedStateHandle,
                goBack = {
                    navController.popBackStack(Catalog.ListFilter.routeSchema, true)
                }
            )
        }
        composableDestination(Catalog.SelectPickupShop) {
            val filtersSavedStateHandle =
                remember(it) { navController.getBackStackEntry(Catalog.Filters.routeSchema).savedStateHandle }
            SelectShopScreen(
                filtersSavedStateHandle = filtersSavedStateHandle,
                goBack = {
                    navController.popBackStack(Catalog.SelectPickupShop.routeSchema, true)
                }
            )
        }
    }
}
