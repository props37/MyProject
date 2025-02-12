package ru.livetyping.zarina.feature.productlist.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavEntry
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ProductListNavActions
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ProductListScreen
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ProductListNavEntry as ProductListScreenNavEntry

internal fun NavGraphBuilder.productListScreen(actions: ProductListNavActions) {
    composable<ProductListScreenNavEntry>(
        typeMap = ProductListNavEntry.typeMap(),
    ) {
        ProductListScreen(actions)
    }
}
