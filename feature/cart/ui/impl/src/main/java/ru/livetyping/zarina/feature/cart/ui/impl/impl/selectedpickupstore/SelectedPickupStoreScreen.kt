package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore.component.ContinueButton
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore.component.ProductList
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore.component.TopBar

@Composable
internal fun SelectedPickupStoreScreen(
    navActions: SelectedPickupStoreNavActions,
    viewModel: SelectedPickupStoreViewModel = hiltViewModel(),
) {
    val store by viewModel.store.collectAsStateWithLifecycle()
    val availableProducts by viewModel.availableProducts.collectAsStateWithLifecycle()

    ScreenContent(
        store = store,
        availableProducts = availableProducts,
        onBackClicked = viewModel::onBackClicked,
        onContinueClicked = viewModel::onContinueClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    store: Store,
    availableProducts: ImmutableList<CartProduct>,
    onBackClicked: () -> Unit,
    onContinueClicked: () -> Unit,
    sideEffects: Flow<SelectedPickupStoreSideEffect>,
    navActions: SelectedPickupStoreNavActions,
) {
    SelectedPickupStoreScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TopBar(
            store = store,
            onBackClicked = onBackClicked,
        )

        ProductList(
            products = availableProducts,
            modifier = Modifier.weight(1f),
        )

        ContinueButton(onClick = onContinueClicked)
    }
}
