package ru.livetyping.zarina.presentation.navigation.feature

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.signup.ui.api.SignUpFeature
import ru.livetyping.zarina.feature.signup.ui.api.SignUpNavActions

fun NavGraphBuilder.signUpFeature(
    navController: NavHostController,
    feature: SignUpFeature,
    actions: SignUpNavActions,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
            resultRetrievers = EmptyNavResultRetrievers,
        )
    }
}
