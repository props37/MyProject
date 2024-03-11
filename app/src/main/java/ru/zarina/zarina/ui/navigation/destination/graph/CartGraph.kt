package ru.zarina.zarina.ui.navigation.destination.graph

import ru.zarina.zarina.ui.navigation.BaseRouteReworked
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph

data object CartGraph : SimpleGraph(
    baseRoute = BaseRouteReworked.CART_GRAPH,
    startDestination = Cart,
) {
    data object Cart : SimpleDestination(BaseRouteReworked.CART)
}
