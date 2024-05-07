package ru.livetyping.zarina.presentation.navigation.destination.graph

import ru.livetyping.zarina.presentation.navigation.BaseRoute
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleGraph

data object FavoritesGraph : SimpleGraph(
    baseRoute = BaseRoute.FAVORITES_GRAPH,
    startDestination = Favorites,
) {
    data object Favorites : SimpleDestination(BaseRoute.FAVORITES)
}
