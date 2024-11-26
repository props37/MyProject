package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.R
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavParams
import ru.livetyping.zarina.feature.profile.ui.ProfileFeature
import ru.livetyping.zarina.feature.profile.ui.ProfileNavActions
import ru.livetyping.zarina.feature.signin.ui.api.SignInFeature

fun NavGraphBuilder.profileFeature(
    navController: NavHostController,
    feature: ProfileFeature,
    actions: ProfileNavActions,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
        )
    }
}

@Composable
fun rememberProfileNavActions(
    navController: NavHostController
): ProfileNavActions {
    return remember(navController) {
        ProfileNavActions(
            onSignInClicked = {
                val signInNavEntry = SignInFeature.getNavEntry()
                navController.navigate(signInNavEntry)
            },
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
