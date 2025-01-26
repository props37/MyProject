package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.core.navigationutil.ScreenResultRetriever
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature
import ru.livetyping.zarina.feature.cart.ui.api.CartSelectedCityResult
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.CartNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.CartScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.CartViewModel

internal fun NavGraphBuilder.cartScreen(
    actions: CartNavActions,
    selectedCityResultRetriever: ScreenResultRetriever<CartSelectedCityResult>,
) {
    composable<CartFeature.NavEntry.StartNavEntry> { navBackStackEntry ->
        CartScreen(
            navActions = actions,
            viewModel = hiltViewModel { factory: CartViewModel.Factory ->
                val selectedCityResultFlow = selectedCityResultRetriever.get(navBackStackEntry)
                factory.create(selectedCityResultFlow)
            },
        )
    }
}
