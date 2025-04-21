package ru.livetyping.zarina.feature.product.ui.impl.impl.navigation

import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.ProductNavActions
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.ProductScreen

internal fun NavGraphBuilder.productScreen(
    actions: ProductNavActions,
    deepLinks: List<NavDeepLink>,
) {
    composable<ProductFeature.NavEntry.StartNavEntry>(deepLinks = deepLinks) {
        ProductScreen(actions)
    }
}
