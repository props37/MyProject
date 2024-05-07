package ru.livetyping.zarina.presentation.navigation

import junit.framework.TestCase.assertEquals
import org.junit.Test

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
