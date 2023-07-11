package ru.zarina.zarina.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import ru.zarina.zarina.ui.common.components.ZarinaBottomNavigation
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.graphs.cartGraph
import ru.zarina.zarina.ui.navigation.graphs.catalogGraph
import ru.zarina.zarina.ui.navigation.graphs.favouritesGraph
import ru.zarina.zarina.ui.navigation.graphs.orphans
import ru.zarina.zarina.ui.navigation.graphs.pickupGraph
import ru.zarina.zarina.ui.navigation.graphs.profileGraph
import ru.zarina.zarina.ui.navigation.graphs.subscribeGraph
import ru.zarina.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun ZarinaNavigation(
    startDestination: Destination<*>,
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(bottomSheetNavigator) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination.routeSchema,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                orphans(navController)
                catalogGraph(navController)
                pickupGraph(navController)
                subscribeGraph(navController)
                favouritesGraph(navController)
                profileGraph(navController)
                cartGraph(navController)
            }
            ZarinaBottomNavigation(
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(UiKitTheme.colors.screenBackground)
                    .clip(RectangleShape)
                    .navigationBarsPadding(),
            )
        }
    }
}

