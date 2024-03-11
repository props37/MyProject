package ru.zarina.zarina.ui.navigation.destination.graph

import ru.zarina.zarina.ui.navigation.BaseRoute
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph

data object FavoritesGraph : SimpleGraph(
    baseRoute = BaseRoute.FAVORITES_GRAPH,
    startDestination = Favorites,
) {
    data object Favorites : SimpleDestination(BaseRoute.FAVORITES)
}
