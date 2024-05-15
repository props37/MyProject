package ru.livetyping.zarina.presentation.navigation.destination.graph

import ru.livetyping.zarina.presentation.navigation.BaseRoute
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleGraph

data object CatalogGraph : SimpleGraph(
    baseRoute = BaseRoute.CATALOG_GRAPH,
    startDestination = Catalog,
) {
    data object Catalog : SimpleDestination(BaseRoute.CATALOG)
}
