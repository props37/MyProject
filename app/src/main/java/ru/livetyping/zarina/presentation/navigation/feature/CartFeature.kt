package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature
import ru.livetyping.zarina.feature.cart.ui.api.CartNavActions
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavParams
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.product.ui.api.ProductNavParams
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.navigateToBottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.popBackStackToBottomNavBarItem
import ru.livetyping.zarina.core.resource.R as RCommon

fun NavGraphBuilder.cartFeature(
    navController: NavHostController,
    feature: CartFeature,
    actions: CartNavActions,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
            resultRetrievers = Unit,
        )
    }
}

@Composable
fun rememberCartNavActions(
    navController: NavHostController
): CartNavActions {
    return remember(navController) {
        CartNavActions(
            onBackClicked = { navController.navigateToBottomNavBarItem(BottomNavBarItem.Home) },
            onChangeCityClicked = { currentCity ->
                val citySelectorParams = CitySelectorNavParams(
                    title = Text.Resource(RCommon.string.res_change_city),
                    currentCity = currentCity,
                )
                val citySelectorNavEntry = CitySelectorFeature.getNavEntry(citySelectorParams)
                navController.navigate(citySelectorNavEntry)
            },
            onGoToCatalogClicked = {
                val bottomNavBarItem = BottomNavBarItem.Catalog
                navController.navigateToBottomNavBarItem(bottomNavBarItem)
                navController.popBackStackToBottomNavBarItem(bottomNavBarItem)
            },
            onProductClicked = { product ->
                val productParams = ProductNavParams(product.productId)
                val productNavEntry = ProductFeature.getNavEntry(productParams)
                navController.navigate(productNavEntry)
            },
        )
    }
}
