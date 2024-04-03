package ru.livetyping.zarina.ui.navigation.destination.graph

import ru.livetyping.zarina.ui.navigation.BaseRoute
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleGraph

data object SignUpGraph : SimpleGraph(
    baseRoute = BaseRoute.SIGN_UP_GRAPH,
    startDestination = SignUp,
) {
    data object SignUp : SimpleDestination(BaseRoute.SIGN_UP)
}
