package ru.zarina.zarina.ui.navigation

import org.junit.Test
import kotlin.test.assertEquals

class BaseRouteTest {
    @Test
    fun baseRoutes_haveUniqueHashCodes() {
        val routes = BaseRoute.entries
        val hashCodes = routes
            .map { it.route.hashCode() }
            .toSet()

        assertEquals(routes.size, hashCodes.size)
    }
}
