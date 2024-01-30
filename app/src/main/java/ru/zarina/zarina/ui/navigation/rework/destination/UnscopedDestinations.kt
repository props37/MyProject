package ru.zarina.zarina.ui.navigation.rework.destination

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.model.filter.ListFilterParcelable
import ru.zarina.zarina.ui.model.geography.CityParcelable
import ru.zarina.zarina.ui.navigation.base.bottomSheetDestination
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.dialogDestination
import ru.zarina.zarina.ui.navigation.rework.graph.HomeGraph
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorBottomSheetScreen
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreenResult
import ru.zarina.zarina.ui.screen.defaultcitydialog.DefaultCityDialogScreen
import ru.zarina.zarina.ui.screen.defaultcitydialog.DefaultCityDialogScreenResult
import ru.zarina.zarina.ui.screen.filters.FiltersScreen
import ru.zarina.zarina.ui.screen.filters.FiltersScreenAction
import ru.zarina.zarina.ui.screen.filters.FiltersScreenResult
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterScreen
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterScreenResult
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreen
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreenAction
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel
import ru.zarina.zarina.ui.screen.products.ProductsScreen
import ru.zarina.zarina.ui.screen.products.ProductsScreenAction

fun NavGraphBuilder.onboardingScreen(navController: NavHostController) {
    composableDestination(
        destination = UnscopedDestinations.Onboarding,
        exitTransition = {
            when (targetState.destination.route) {
                HomeGraph.Home.routeSchema -> {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(durationMillis = 300),
                    )
                }

                else -> null
            }
        },
    ) {
        OnboardingScreen(
            viewModel = hiltViewModel { factory : OnboardingViewModel.Factory ->
                factory.create(it.savedStateHandle)
            },
            navigateForward = { action ->
                when (action) {
                    is OnboardingScreenAction.OnboardingCompleted -> {
                        navController.navigate(HomeGraph.route) {
                            popUpTo(0)
                        }

                        if (action.userCity == null) {
                            navController.navigate(UnscopedDestinations.DefaultCityDialog.route)
                        }
                    }

                    is OnboardingScreenAction.SelectCityClicked -> {
                        val args = UnscopedDestinations.CitySelector.Args(action.currentCity)
                        val route = UnscopedDestinations.CitySelector.createRoute(args)
                        navController.navigate(route)
                    }
                }
            },
        )
    }
}

fun NavGraphBuilder.citySelectorBottomSheetScreen(navController: NavHostController) {
    bottomSheetDestination(UnscopedDestinations.CitySelector) {
        CitySelectorBottomSheetScreen(
            navigateBackward = { result ->
                when (result) {
                    CitySelectorScreenResult.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.CitySelector.routeSchema,
                            inclusive = true,
                        )
                    }

                    is CitySelectorScreenResult.CitySelected -> {
                        val cityParcelable = CityParcelable.fromCity(result.city)
                        val result = UnscopedDestinations.CitySelector.Result(cityParcelable)
                        navController.previousBackStackEntry?.savedStateHandle
                            ?.set(UnscopedDestinations.CitySelector.RESULT_KEY, result)
                        navController.popBackStack(
                            route = UnscopedDestinations.CitySelector.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavGraphBuilder.defaultCityDialogScreen(navController: NavHostController) {
    dialogDestination(
        destination = UnscopedDestinations.DefaultCityDialog,
        dialogProperties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        DefaultCityDialogScreen(
            navigateBackward = { result ->
                when (result) {
                    DefaultCityDialogScreenResult.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.DefaultCityDialog.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            }
        )
    }
}

fun NavGraphBuilder.productsScreen(navController: NavHostController) {
    composableDestination(UnscopedDestinations.Products) {
        ProductsScreen(
            navigateForward = { action ->
                when (action) {
                    is ProductsScreenAction.FiltersClicked -> {
                        val args = UnscopedDestinations.Filters.Args(
                            categoryId = action.categoryId,
                            filters = action.filters,
                        )
                        val route = UnscopedDestinations.Filters.createRoute(args)
                        navController.navigate(route)
                    }
                }
            },
            navigateBackward = {
                navController.popBackStack(
                    route = UnscopedDestinations.Products.routeSchema,
                    inclusive = true,
                )
            },
        )
    }
}

fun NavGraphBuilder.filtersScreen(navController: NavHostController) {
    composableDestination(UnscopedDestinations.Filters) {
        FiltersScreen(
            navigateForward = { action ->
                when (action) {
                    is FiltersScreenAction.ListFilterClicked -> {
                        val args = UnscopedDestinations.ListFilter.Args(action.filter)
                        val route = UnscopedDestinations.ListFilter.createRoute(args)
                        navController.navigate(route)
                    }
                }
            },
            navigateBackward = { result ->
                when (result) {
                    FiltersScreenResult.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.Filters.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavGraphBuilder.listFilterScreen(navController: NavHostController) {
    composableDestination(UnscopedDestinations.ListFilter) {
        ListFilterScreen(
            navigateBackward = { result ->
                when (result) {
                    ListFilterScreenResult.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.ListFilter.routeSchema,
                            inclusive = true,
                        )
                    }

                    is ListFilterScreenResult.FilterChanged -> {
                        val filterParcelable = ListFilterParcelable.from(result.filter)
                        val result = UnscopedDestinations.ListFilter.Result(filterParcelable)
                        navController.previousBackStackEntry?.savedStateHandle
                            ?.set(UnscopedDestinations.ListFilter.RESULT_KEY, result)
                        navController.popBackStack(
                            route = UnscopedDestinations.ListFilter.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}
