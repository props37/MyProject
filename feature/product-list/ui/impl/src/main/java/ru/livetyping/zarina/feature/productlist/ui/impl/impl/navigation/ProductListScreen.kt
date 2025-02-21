package ru.livetyping.zarina.feature.productlist.ui.impl.impl.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavEntry
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.FiltrationResult
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ProductListNavActions
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ProductListScreen
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ProductListViewModel
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ProductListNavEntry as ProductListScreenNavEntry

internal fun NavGraphBuilder.productListScreen(actions: ProductListNavActions) {
    composable<ProductListScreenNavEntry>(
        typeMap = ProductListNavEntry.typeMap(),
    ) { navBackStackEntry ->
        ProductListScreen(
            navActions = actions,
            viewModel = hiltViewModel { factory: ProductListViewModel.Factory ->
                val filtrationResultFlow = navBackStackEntry.savedStateHandle
                    .getStateFlow<FiltrationResult?>(
                        key = FiltrationResult.KEY,
                        initialValue = null,
                    )
                factory.create(filtrationResultFlow)
            },
        )
    }
}
