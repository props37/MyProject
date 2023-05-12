package ru.zarina.zarina.ui.screens.pickup.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.base.ErrorState
import ru.zarina.zarina.ui.common.components.DropdownBar
import ru.zarina.zarina.ui.common.components.HorizontalProductCard
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.toolbar.CloseButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.screens.pickup.PickupViewModel
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupRootScreenContent(
    city: City?,
    product: Product?,
    selectedOffer: Offer?,
    onBackClick: () -> Unit,
    onSelectCityClick: () -> Unit,
    onSelectSizeClick: () -> Unit,
    errorType: PickupViewModel.ErrorType?,
    onRefreshClick: () -> Unit,
) {
    val errorState = when (errorType) {
        PickupViewModel.ErrorType.NETWORK -> ErrorState.NETWORK
        PickupViewModel.ErrorType.GENERIC -> ErrorState.GENERIC
        null -> null
    }
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(R.string.find_and_pickup),
                endIcon = {
                    CloseButton(onClick = onBackClick)
                }
            )
        },
        errorState = errorState,
        onErrorButtonClick = onRefreshClick,
    ) {
        if (product != null)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CityPicker(
                    city = city,
                    onClick = onSelectCityClick,
                )
                HorizontalProductCard(
                    product = product,
                    selectedSize = selectedOffer?.size,
                    onSelectSizeClick = onSelectSizeClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
    }
}

@Composable
private fun CityPicker(
    city: City?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownBar(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = city?.name.orEmpty(),
            style = UiKitTheme.typography.dropdownButton,
            color = UiKitTheme.colors.primaryContentColor,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PickupRootScreen(
    parentEntry: NavBackStackEntry,
    showSelectSize: () -> Unit,
    showSelectCity: () -> Unit,
    goBack: () -> Unit,
) {
    val viewModel: PickupRootViewModel = hiltViewModel()
    val parentViewModel = hiltViewModel<PickupViewModel>(parentEntry)

    val city by parentViewModel.city.collectAsStateWithLifecycle()
    val product by parentViewModel.product.collectAsStateWithLifecycle()
    val selectedOffer by parentViewModel.selectedOffer.collectAsStateWithLifecycle()
    val errorType by parentViewModel.errorType.collectAsStateWithLifecycle()

    PickupRootScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showSelectSize = showSelectSize,
        showSelectCity = showSelectCity,
        goBack = goBack,
    )

    PickupRootScreenContent(
        city = city,
        product = product,
        selectedOffer = selectedOffer,
        onBackClick = viewModel::onBackClick,
        onSelectCityClick = viewModel::onSelectCityClick,
        onSelectSizeClick = viewModel::onSelectSizeClick,
        onRefreshClick = parentViewModel::onRefreshClick,
        errorType = errorType,
    )
}

@Composable
fun PickupRootScreenBehavior(
    sideEffects: Flow<PickupRootViewModel.SideEffect>,
    showSelectSize: () -> Unit,
    showSelectCity: () -> Unit,
    goBack: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                PickupRootViewModel.SideEffect.GoBack -> goBack()
                PickupRootViewModel.SideEffect.ShowSelectSize -> showSelectSize()
                PickupRootViewModel.SideEffect.ShowSelectCity -> showSelectCity()
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
            city = City.DEFAULT,
            product = product,
            selectedOffer = product.offers.first(),
            onBackClick = {},
            onSelectCityClick = {},
            onSelectSizeClick = {},
            onRefreshClick = {},
            errorType = null,
        )
    }
}
