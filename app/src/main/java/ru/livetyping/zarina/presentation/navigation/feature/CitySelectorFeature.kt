package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.uimodel.geo.CityParcelable
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorResult

fun NavGraphBuilder.citySelectorFeature(
    feature: CitySelectorFeature,
    actions: CitySelectorFeature.NavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            resultRetrievers = EmptyNavResultRetrievers,
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
