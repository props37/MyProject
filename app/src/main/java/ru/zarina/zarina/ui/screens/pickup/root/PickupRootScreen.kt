package ru.zarina.zarina.ui.screens.pickup.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Size
import ru.zarina.zarina.ui.common.components.HorizontalProductCard
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.toolbar.CloseButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupRootScreenContent(
    product: Product?,
    selectedSize: Size?,
    onBackClick: () -> Unit,
    onSelectSizeClick: (Product) -> Unit,
) {
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(R.string.find_and_pickup),
                endIcon = {
                    CloseButton(onClick = onBackClick)
                }
            )
        },
    ) {
        if (product != null)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CityPicker()
                HorizontalProductCard(
                    product = product,
                    selectedSize = selectedSize,
                    onSelectSizeClick = { onSelectSizeClick(product) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
    }
}

@Composable
private fun CityPicker(
    modifier: Modifier = Modifier,
) {

}

@Composable
fun PickupRootScreen(
    showSelectSize: (Product.Id) -> Unit,
    goBack: () -> Unit,
) {
    val viewModel = hiltViewModel<PickupRootViewModel>()

    val product by viewModel.product.collectAsStateWithLifecycle()
    val selectedSize by viewModel.selectedSize.collectAsStateWithLifecycle()

    PickupRootScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showSelectSize = showSelectSize,
        goBack = goBack,
    )

    PickupRootScreenContent(
        product = product,
        selectedSize = selectedSize,
        onBackClick = viewModel::onBackClick,
        onSelectSizeClick = viewModel::onSelectSizeClick,
    )
}

@Composable
fun PickupRootScreenBehavior(
    sideEffects: Flow<PickupRootViewModel.SideEffect>,
    showSelectSize: (Product.Id) -> Unit,
    goBack: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                PickupRootViewModel.SideEffect.GoBack -> goBack()
                is PickupRootViewModel.SideEffect.ShowSelectSize -> showSelectSize(effect.product.id)
            }
        }
    }
}

@Preview("multiple sizes")
@Composable
fun PickupRootScreenContentPreview(
    @PreviewParameter(ProductProvider::class, limit = 1)
    product: Product,
) {
    ZarinaTheme {
        PickupRootScreenContent(
            product = product,
            selectedSize = product.offers.first().size,
            onBackClick = {},
            onSelectSizeClick = {},
        )
    }
}
