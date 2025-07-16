package ru.livetyping.zarina.feature.productlist.ui.impl.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavEntry
import ru.livetyping.zarina.feature.productlist.ui.impl.filtration.FiltrationResult
import ru.livetyping.zarina.feature.productlist.ui.impl.productlist.ProductListNavActions
import ru.livetyping.zarina.feature.productlist.ui.impl.productlist.ProductListScreen
import ru.livetyping.zarina.feature.productlist.ui.impl.productlist.ProductListViewModel
import ru.livetyping.zarina.feature.productlist.ui.impl.productlist.ProductListNavEntry as ProductListScreenNavEntry

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
