package ru.zarina.zarina.ui.navigation.destination.graph

import ru.zarina.zarina.ui.navigation.BaseRoute
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph

data object CartGraph : SimpleGraph(
    baseRoute = BaseRoute.CART_GRAPH,
    startDestination = Cart,
) {
    data object Cart : SimpleDestination(BaseRoute.CART)
}
