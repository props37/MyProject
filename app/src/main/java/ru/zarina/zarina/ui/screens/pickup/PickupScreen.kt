package ru.zarina.zarina.ui.screens.pickup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import ru.zarina.zarina.ui.common.components.HorizontalProductCard
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.toolbar.CloseButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupScreenContent(
    product: Product?,
) {
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(R.string.find_and_pickup),
                endIcon = {
                    CloseButton(onClick = { /*TODO*/ })
                }
            )
        },
    ) {
        if (product != null)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CityPicker()
                HorizontalProductCard(
                    product = product,
                    onClick = { /*TODO*/ }
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
fun PickupScreen() {
    val viewModel = hiltViewModel<PickupViewModel>()

    val product by viewModel.product.collectAsStateWithLifecycle()

    PickupScreenBehavior(
        sideEffects = viewModel.sideEffects,
    )

    PickupScreenContent(
        product = product,
    )
}

@Composable
fun PickupScreenBehavior(
    sideEffects: Flow<PickupViewModel.SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun PickupScreenContentPreview(
    @PreviewParameter(ProductProvider::class, limit = 1)
    product: Product,
) {
    ZarinaTheme {
        PickupScreenContent(
            product = product,
        )
    }
}
