package ru.livetyping.zarina.ui.navigation.destination.graph

import ru.livetyping.zarina.ui.navigation.BaseRoute
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleGraph

data object FavoritesGraph : SimpleGraph(
    baseRoute = BaseRoute.FAVORITES_GRAPH,
    startDestination = Favorites,
) {
    data object Favorites : SimpleDestination(BaseRoute.FAVORITES)
}
