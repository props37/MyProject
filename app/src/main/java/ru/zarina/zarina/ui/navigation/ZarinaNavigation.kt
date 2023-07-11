package ru.zarina.zarina.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.graphs.catalogGraph
import ru.zarina.zarina.ui.navigation.graphs.orphans
import ru.zarina.zarina.ui.navigation.graphs.pickupGraph
import ru.zarina.zarina.ui.navigation.graphs.subscribeGraph

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun ZarinaNavigation(
    startDestination: Destination<*>,
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(
            navController = navController,
            startDestination = startDestination.routeSchema,
        ) {
            orphans(navController)
            pickupGraph(navController)
            subscribeGraph(navController)
            catalogGraph(navController)
        }
    }
}

