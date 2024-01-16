package ru.zarina.zarina.ui.navigation

import org.junit.Test
import ru.zarina.zarina.ui.navigation.rework.BaseRouteReworked
import kotlin.test.assertEquals

class BaseRouteTest {
    @Test
    fun baseRoutes_haveUniqueHashCodes() {
        val routes = BaseRouteReworked.entries
        val hashCodes = routes
            .map { it.route.hashCode() }
            .toSet()

        assertEquals(routes.size, hashCodes.size)
    }
}
