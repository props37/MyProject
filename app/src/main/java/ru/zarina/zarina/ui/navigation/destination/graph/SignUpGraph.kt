package ru.zarina.zarina.ui.navigation.destination.graph

import ru.zarina.zarina.ui.navigation.BaseRoute
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph

data object SignUpGraph : SimpleGraph(
    baseRoute = BaseRoute.SIGN_UP_GRAPH,
    startDestination = SignUp,
) {
    data object SignUp : SimpleDestination(BaseRoute.SIGN_UP)
}
