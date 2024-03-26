package ru.zarina.zarina.util.library.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import timber.log.Timber

fun NavController.navigate(
    route: String,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val currentDestinationGraph = this.currentDestination?.parent
    val destinationId = currentDestinationGraph?.findNode(route)?.id
    if (destinationId != null) {
        navigate(destinationId, args, navOptions, navigatorExtras)
    } else {
        Timber.e("Can not navigate to route $route because its destination ID was not found")
    }
}
