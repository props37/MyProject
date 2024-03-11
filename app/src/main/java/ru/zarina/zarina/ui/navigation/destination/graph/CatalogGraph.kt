package ru.zarina.zarina.ui.navigation.destination.graph

import ru.zarina.zarina.ui.navigation.BaseRouteReworked
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph

data object CatalogGraph : SimpleGraph(
    baseRoute = BaseRouteReworked.CATALOG_GRAPH,
    startDestination = Catalog,
) {
    data object Catalog : SimpleDestination(BaseRouteReworked.CATALOG)
}
