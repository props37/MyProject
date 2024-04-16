package ru.livetyping.zarina.ui.navigation.destination.graph

import ru.livetyping.zarina.ui.navigation.BaseRoute
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleGraph

data object ProfileGraph : SimpleGraph(
    baseRoute = BaseRoute.PROFILE_GRAPH,
    startDestination = Profile,
) {
    data object Profile : SimpleDestination(BaseRoute.PROFILE)

    data object ProfileDetails : SimpleDestination(BaseRoute.PROFILE_DETAILS)

    data object SignOutConfirmation : SimpleDestination(BaseRoute.SIGN_OUT_CONFIRMATION)

    data object AccountDeletionConfirmation :
        SimpleDestination(BaseRoute.ACCOUNT_DELETION_CONFIRMATION)

    data object MyOrders : SimpleDestination(BaseRoute.MY_ORDERS)
}
