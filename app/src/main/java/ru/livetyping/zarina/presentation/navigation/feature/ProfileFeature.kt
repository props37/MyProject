package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.profile.ui.ProfileNavActions

@Composable
fun rememberProfileNavActions(
    navController: NavHostController
): ProfileNavActions {
    return remember(navController) {
        ProfileNavActions()
    }
}
