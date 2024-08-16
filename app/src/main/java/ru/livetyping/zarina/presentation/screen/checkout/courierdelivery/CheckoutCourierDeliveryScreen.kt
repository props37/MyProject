package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.checkout.common.CheckoutComponents
import ru.livetyping.zarina.presentation.screen.checkout.common.address.CheckoutAddressComponents
import ru.livetyping.zarina.presentation.screen.checkout.common.address.CheckoutAddressComponents.AddressSlot
import ru.livetyping.zarina.presentation.screen.checkout.common.address.CheckoutAddressViewModelComponent
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.CheckoutCourierDeliveryViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun CheckoutCourierDeliveryScreen(
    navigate: (CheckoutCourierDeliveryScreenAction) -> Unit,
    viewModel: CheckoutCourierDeliveryViewModel = hiltViewModel(),
) {
    val step by viewModel.step.collectAsStateWithLifecycle()
    val stepCount by viewModel.stepCount.collectAsStateWithLifecycle()
    val city by viewModel.city.collectAsStateWithLifecycle()
    val streetsState by viewModel.streetsState.collectAsStateWithLifecycle()

    ScreenContent(
        step = step,
        stepCount = stepCount,
        city = city,
        streetTextFieldState = viewModel.streetTextFieldState,
        buildingTextFieldState = viewModel.buildingTextFieldState,
        apartmentTextFieldState = viewModel.apartmentTextFieldState,
        searchStreetTextFieldState = viewModel.searchStreetTextFieldState,
        searchBuildingTextFieldState = viewModel.searchBuildingTextFieldState,
        searchApartmentTextFieldState = viewModel.searchApartmentTextFieldState,
        streetsState = streetsState,
        onBackClicked = viewModel::onBackClicked,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    step: Int,
    stepCount: Int,
    city: City?,
    streetTextFieldState: TextFieldState,
    buildingTextFieldState: TextFieldState,
    apartmentTextFieldState: TextFieldState,
    searchStreetTextFieldState: TextFieldState,
    searchBuildingTextFieldState: TextFieldState,
    searchApartmentTextFieldState: TextFieldState,
    streetsState: CheckoutAddressViewModelComponent.State,
    onBackClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutCourierDeliveryScreenAction) -> Unit,
) {
    CheckoutCourierDeliveryScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    val coroutineScope = rememberCoroutineScope()
    var visibleAddressSlotSelectorBottomSheet by remember { mutableStateOf<AddressSlot?>(null) }
    val addressSelectorBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it == SheetValue.Expanded },
    )
    CheckoutAddressComponents.AddressSelectorBottomSheet(
        visibleAddressSlotSelectorBottomSheet = visibleAddressSlotSelectorBottomSheet,
        onDismissRequest = { visibleAddressSlotSelectorBottomSheet = null },
        sheetState = addressSelectorBottomSheetState,
        onCloseClicked = {
            coroutineScope.launch {
                addressSelectorBottomSheetState.hide()
                visibleAddressSlotSelectorBottomSheet = null
            }
        },
        streetTextFieldState = searchStreetTextFieldState,
        buildingTextFieldState = searchBuildingTextFieldState,
        apartmentTextFieldState = searchApartmentTextFieldState,
        streetsState = streetsState,
        modifier = Modifier.statusBarsPadding(),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            ),
    ) {
        CheckoutComponents.TopBar(
            title = stringResource(R.string.courier_delivery),
            step = step,
            stepCount = stepCount,
            isBackButtonVisible = true,
            onBackClicked = onBackClicked,
            onCloseClicked = onCloseClicked,
        )

        ZarinaItem {
            Text(
                text = city?.name.orEmpty(),
                style = UiKitTheme.typography.secondary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
            )
        }

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(8.dp))

            CheckoutAddressComponents.AddressBlock(
                streetTextFieldState = streetTextFieldState,
                buildingTextFieldState = buildingTextFieldState,
                apartmentTextFieldState = apartmentTextFieldState,
                onStreetClicked = { visibleAddressSlotSelectorBottomSheet = AddressSlot.Street },
                onBuildingClicked = {
                    visibleAddressSlotSelectorBottomSheet = AddressSlot.Building
                },
                onApartmentClicked = {
                    visibleAddressSlotSelectorBottomSheet = AddressSlot.Apartment
                },
            )

            val navigationBarHeight =
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(navigationBarHeight + 20.dp))
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
