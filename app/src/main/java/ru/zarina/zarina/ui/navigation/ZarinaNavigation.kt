package ru.zarina.zarina.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import ru.zarina.zarina.ui.common.base.behavior.BehaviorController
import ru.zarina.zarina.ui.common.behavior.navigationbar.LocalNavigationBarController
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarBehavior
import ru.zarina.zarina.ui.common.components.ZarinaBottomNavigation
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.graphs.cartGraph
import ru.zarina.zarina.ui.navigation.graphs.catalogGraph
import ru.zarina.zarina.ui.navigation.graphs.favoritesGraph
import ru.zarina.zarina.ui.navigation.graphs.homeGraph
import ru.zarina.zarina.ui.navigation.graphs.orphans
import ru.zarina.zarina.ui.navigation.graphs.pickupGraph
import ru.zarina.zarina.ui.navigation.graphs.profileGraph
import ru.zarina.zarina.ui.navigation.graphs.subscribeGraph
import ru.zarina.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun ZarinaNavigation(
    startDestination: Destination<*>,
    changeStartDestination: (Destination<*>) -> Unit,
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(bottomSheetNavigator) {

        val navigationBarController = remember {
            BehaviorController<NavigationBarBehavior>(NavigationBarBehavior.DEFAULT)
        }

        CompositionLocalProvider(
            LocalNavigationBarController provides navigationBarController
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                NavHost(
                    navController = navController,
                    startDestination = startDestination.routeSchema,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    orphans(
                        navController = navController,
                        changeStartDestination = changeStartDestination,
                    )
                    homeGraph(navController)
                    catalogGraph(navController)
                    pickupGraph(navController)
                    subscribeGraph(navController)
                    favoritesGraph(navController)
                    profileGraph(navController)
                    cartGraph(navController)
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                ) {
                    ZarinaBottomNavigation(
                        navController = navController,
                        navigationBarController = navigationBarController,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(UiKitTheme.colors.screenBackground)
                            .clip(RectangleShape),
                    )
                }
            }
        }
    }
}

