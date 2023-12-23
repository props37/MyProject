package ru.zarina.zarina.ui.navigation.rework.graph

import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph
import ru.zarina.zarina.ui.navigation.rework.destination.BaseRouteReworked

data object CatalogGraph : SimpleGraph(
    baseRoute = BaseRouteReworked.CATALOG_GRAPH,
    startDestination = Catalog,
) {
    data object Catalog : SimpleDestination(BaseRouteReworked.CATALOG)
}
