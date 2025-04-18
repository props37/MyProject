package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.core.navigationutil.ScreenResultRetriever
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature
import ru.livetyping.zarina.feature.cart.ui.api.CartSelectedCityResult
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorResult
import ru.livetyping.zarina.feature.payment.ui.api.PaymentFeature
import ru.livetyping.zarina.feature.payment.ui.api.PaymentResult
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.navigateToBottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.popBackStackToBottomNavBarItem
import ru.livetyping.zarina.core.resource.R as RCommon
import ru.livetyping.zarina.feature.cart.ui.api.PaymentResult as CartPaymentResult

fun NavGraphBuilder.cartFeature(
    navController: NavHostController,
    feature: CartFeature,
    actions: CartFeature.NavActions,
    resultRetrievers: CartFeature.NavResultRetrievers,
) {
    with(feature) {
        navigation(
            navController = navController,
            actions = actions,
            resultRetrievers = resultRetrievers,
        )
    }
}

@Composable
fun rememberCartNavActions(
    navController: NavHostController
): CartFeature.NavActions {
    return remember(navController) {
        val navigateToHome = { navController.navigateToBottomNavBarItem(BottomNavBarItem.Home) }
        CartFeature.NavActions(
            onBackClicked = navigateToHome,
            onReturnToHomeClicked = navigateToHome,
            onChangeCityClicked = { currentCity ->
                val citySelectorNavEntry = CitySelectorFeature.NavEntry.create(
                    title = Text.Resource(RCommon.string.res_change_city),
                    currentCity = currentCity,
                )
                navController.navigate(citySelectorNavEntry)
            },
            onGoToCatalogClicked = {
                val bottomNavBarItem = BottomNavBarItem.Catalog
                navController.navigateToBottomNavBarItem(bottomNavBarItem)
                navController.popBackStackToBottomNavBarItem(bottomNavBarItem)
            },
            onProductClicked = { product ->
                val productNavEntry = ProductFeature.NavEntry.create(product.productId)
                navController.navigate(productNavEntry)
            },
            onPaymentStarted = { url ->
                val paymentNavEntry = PaymentFeature.NavEntry(url.value)
                navController.navigate(paymentNavEntry)
            },
        )
    }
}

@Composable
fun rememberCartNavResultRetrievers(): CartFeature.NavResultRetrievers {
    return remember {
        val selectedCityResultRetriever = ScreenResultRetriever { navBackStackEntry ->
            navBackStackEntry.savedStateHandle
                .getStateFlow<CitySelectorResult?>(CitySelectorResult.KEY, initialValue = null)
                .map { citySelectorResult ->
                    citySelectorResult?.let {
                        CartSelectedCityResult(id = it.id, city = it.city.toCity())
                    }
                }
        }
        val paymentResultRetriever = ScreenResultRetriever { navBackStackEntry ->
            navBackStackEntry.savedStateHandle
                .getStateFlow<PaymentResult?>(PaymentResult.KEY, initialValue = null)
                .map { paymentResult ->
                    paymentResult?.let {
                        CartPaymentResult(it.id)
                    }
                }
        }

        CartFeature.NavResultRetrievers(
            selectedCityResultRetriever = selectedCityResultRetriever,
            paymentResultRetriever = paymentResultRetriever,
        )
    }
}
