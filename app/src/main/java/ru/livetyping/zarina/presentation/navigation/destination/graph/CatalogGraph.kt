package ru.livetyping.zarina.presentation.navigation.destination.graph

import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink
import ru.livetyping.zarina.presentation.navigation.BaseRoute
import ru.livetyping.zarina.presentation.navigation.ZarinaDeepLinkUris
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleGraph

data object CatalogGraph : SimpleGraph(
    baseRoute = BaseRoute.CATALOG_GRAPH,
    startDestination = Catalog,
) {
    data object Catalog : SimpleDestination(BaseRoute.CATALOG) {
        override val deepLinks: List<NavDeepLink>
            get() = buildList {
                ZarinaDeepLinkUris.forEach { uri ->
                    add(navDeepLink { uriPattern = "$uri/catalog" })
                    add(navDeepLink { uriPattern = "$uri/catalog/" })
                }
            }
    }
}
