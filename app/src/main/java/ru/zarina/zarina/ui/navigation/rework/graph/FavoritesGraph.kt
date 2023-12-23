package ru.zarina.zarina.ui.navigation.rework.graph

import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph
import ru.zarina.zarina.ui.navigation.rework.destination.BaseRouteReworked

data object FavoritesGraph : SimpleGraph(
    baseRoute = BaseRouteReworked.FAVORITES_GRAPH,
    startDestination = Favorites,
) {
    data object Favorites : SimpleDestination(BaseRouteReworked.FAVORITES)
}
