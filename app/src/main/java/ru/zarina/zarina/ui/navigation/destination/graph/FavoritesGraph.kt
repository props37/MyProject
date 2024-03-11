package ru.zarina.zarina.ui.navigation.destination.graph

import ru.zarina.zarina.ui.navigation.BaseRouteReworked
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph

data object FavoritesGraph : SimpleGraph(
    baseRoute = BaseRouteReworked.FAVORITES_GRAPH,
    startDestination = Favorites,
) {
    data object Favorites : SimpleDestination(BaseRouteReworked.FAVORITES)
}
