package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.CartNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.CartNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.CartScreen

internal fun NavGraphBuilder.cartScreen(actions: CartNavActions) {
    composable<CartNavEntry> {
        CartScreen(actions)
    }
}
