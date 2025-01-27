package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.core.navigationutil.ScreenResultRetriever
import ru.livetyping.zarina.core.navigationutil.hasRoute
import ru.livetyping.zarina.core.navigationutil.withParent
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorResult
import ru.livetyping.zarina.feature.detectedcity.ui.DetectedCityFeature
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavActions
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavResultRetrievers
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingSelectedCityResult
import ru.livetyping.zarina.presentation.navigation.util.initialDestination
import ru.livetyping.zarina.presentation.navigation.util.targetDestination

fun NavGraphBuilder.onboardingFeature(
    feature: OnboardingFeature,
    actions: OnboardingNavActions,
    resultRetrievers: OnboardingNavResultRetrievers,
) {
    with(feature) {
        composable(
            actions = actions,
            resultRetrievers = resultRetrievers,
            exitTransition = {
                val targetDestinationWithParent = targetDestination.withParent()
                when {
                    targetDestinationWithParent.hasRoute(CitySelectorFeature.NavEntry::class) -> {
                        ExitTransition.KeepUntilTransitionsFinished
                    }

                    else -> null
                }
            },
            popEnterTransition = {
                val initialDestinationWithParent = initialDestination.withParent()
                when {
                    initialDestinationWithParent.hasRoute(CitySelectorFeature.NavEntry::class) -> {
                        EnterTransition.None
                    }

                    else -> null
                }
            },
        )
    }
}

@Composable
fun rememberOnboardingNavActions(
    navController: NavHostController
): OnboardingNavActions {
    return remember(navController) {
        OnboardingNavActions(
            onOnboardingCompleted = { selectedCity ->
                navController.navigate(HomeFeature.NavEntry) {
                    popUpTo(0)
                }

                if (selectedCity == null) {
                    navController.navigate(DetectedCityFeature.getNavEntry())
                }
            },
            onSelectCityClicked = {
                navController.navigate(CitySelectorFeature.NavEntry())
            },
        )
    }
}

@Composable
fun rememberOnboardingNavResultRetrievers(): OnboardingNavResultRetrievers {
    return remember {
        val selectedCityResultRetriever = ScreenResultRetriever { navBackStackEntry ->
            navBackStackEntry.savedStateHandle
                .getStateFlow<CitySelectorResult?>(CitySelectorResult.KEY, null)
                .map { citySelectorResult ->
                    citySelectorResult?.let {
                        OnboardingSelectedCityResult(id = it.id, city = it.city.toCity())
                    }
                }
        }

        OnboardingNavResultRetrievers(
            selectedCityResultRetriever = selectedCityResultRetriever,
        )
    }
}
