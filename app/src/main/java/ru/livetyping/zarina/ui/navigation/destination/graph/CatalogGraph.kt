package ru.livetyping.zarina.ui.navigation.destination.graph

import ru.livetyping.zarina.ui.navigation.BaseRoute
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleGraph

data object CatalogGraph : SimpleGraph(
    baseRoute = BaseRoute.CATALOG_GRAPH,
    startDestination = Catalog,
) {
    data object Catalog : SimpleDestination(BaseRoute.CATALOG)
}
