package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.R
import ru.livetyping.zarina.core.navigationutil.ScreenResultRetriever
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavParams
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorResult
import ru.livetyping.zarina.feature.profile.ui.ProfileFeature
import ru.livetyping.zarina.feature.profile.ui.ProfileNavActions
import ru.livetyping.zarina.feature.profile.ui.ProfileNavResultRetrievers
import ru.livetyping.zarina.feature.profile.ui.ProfileSelectedCityResult
import ru.livetyping.zarina.feature.signin.ui.api.SignInFeature
import ru.livetyping.zarina.feature.signup.ui.api.SignUpFeature
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.navigateToBottomNavBarItem

fun NavGraphBuilder.profileFeature(
    navController: NavHostController,
    feature: ProfileFeature,
    actions: ProfileNavActions,
    resultRetrievers: ProfileNavResultRetrievers,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
            resultRetrievers = resultRetrievers,
        )
    }
}

@Composable
fun rememberProfileNavActions(
    navController: NavHostController
): ProfileNavActions {
    return remember(navController) {
        ProfileNavActions(
            onBackClicked = { navController.navigateToBottomNavBarItem(BottomNavBarItem.Home) },
            onSignInClicked = { navController.navigate(SignInFeature.getNavEntry()) },
            onSignUpClicked = { navController.navigate(SignUpFeature.getNavEntry()) },
            onChangeCityClicked = { currentCity ->
                val citySelectorParams = CitySelectorNavParams(
                    title = Text.Resource(R.string.city_change),
                    currentCity = currentCity,
                )
                val citySelectorNavEntry = CitySelectorFeature.getNavEntry(citySelectorParams)
                navController.navigate(citySelectorNavEntry)
            }
        )
    }
}

@Composable
fun rememberProfileNavResultRetrievers(): ProfileNavResultRetrievers {
    return remember {
        val selectedCityResultRetriever = ScreenResultRetriever { navBackStackEntry ->
            navBackStackEntry.savedStateHandle
                .getStateFlow<CitySelectorResult?>(CitySelectorResult.KEY, null)
                .map { citySelectorResult ->
                    citySelectorResult?.let {
                        ProfileSelectedCityResult(id = it.id, city = it.city.toCity())
                    }
                }
        }

        ProfileNavResultRetrievers(
            selectedCityResultRetriever = selectedCityResultRetriever,
        )
    }
}
