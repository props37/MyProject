package ru.livetyping.zarina.ui.navigation.destination.graph

import ru.livetyping.zarina.ui.navigation.BaseRoute
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleGraph

data object SignInGraph : SimpleGraph(
    baseRoute = BaseRoute.SIGN_IN_GRAPH,
    startDestination = SignIn,
) {
    data object SignIn : SimpleDestination(BaseRoute.SIGN_IN)

    data object PasswordRecovery : SimpleDestination(BaseRoute.PASSWORD_RECOVERY)
}
