package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigationutil.hasRoute
import ru.livetyping.zarina.core.navigationutil.withParent
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaEnterSlideTransition
import ru.livetyping.zarina.core.uikit.navigation.transition.zarinaPopExitSlideTransition
import ru.livetyping.zarina.core.uimodel.geo.CityParcelable
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorResult
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.presentation.navigation.util.initialDestination
import ru.livetyping.zarina.presentation.navigation.util.targetDestination

fun NavGraphBuilder.citySelectorFeature(
    feature: CitySelectorFeature,
    actions: CitySelectorFeature.NavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            resultRetrievers = EmptyNavResultRetrievers,
            enterTransition = {
                val initialDestinationWithParent = initialDestination.withParent()
                when {
                    initialDestinationWithParent.hasRoute(OnboardingFeature.NavEntry::class) -> {
                        zarinaEnterSlideTransition(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        )
                    }

                    else -> null
                }
            },
            popExitTransition = {
                val targetDestinationWithParent = targetDestination.withParent()
                when {
                    targetDestinationWithParent.hasRoute(OnboardingFeature.NavEntry::class) -> {
                        zarinaPopExitSlideTransition(
                            towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        )
                    }

                    else -> null
                }
            },
        )
    }
}

@Composable
fun rememberCitySelectorNavActions(
    navController: NavHostController
): CitySelectorFeature.NavActions {
    return remember(navController) {
        CitySelectorFeature.NavActions(
            onBackClicked = { navController.navigateUp() },
            onCitySelected = { city ->
                val cityParcelable = CityParcelable.from(city)
                val result = CitySelectorResult(city = cityParcelable)
                navController.navigateUp()
                navController.currentBackStackEntry?.savedStateHandle
                    ?.set(CitySelectorResult.KEY, result)
            },
        )
    }
}
