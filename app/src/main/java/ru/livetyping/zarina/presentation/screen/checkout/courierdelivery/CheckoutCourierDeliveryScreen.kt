package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.checkout.DeliveryOption
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.checkout.common.CheckoutComponents
import ru.livetyping.zarina.presentation.screen.checkout.common.DeliveryOptionsState
import ru.livetyping.zarina.presentation.screen.checkout.common.address.CheckoutAddressComponents
import ru.livetyping.zarina.presentation.screen.checkout.common.address.CheckoutAddressComponents.AddressPartBottomSheet
import ru.livetyping.zarina.presentation.screen.checkout.common.address.CheckoutAddressViewModelComponent
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.CheckoutCourierDeliveryViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun CheckoutCourierDeliveryScreen(
    viewModel: CheckoutCourierDeliveryViewModel,
    navigate: (CheckoutCourierDeliveryScreenAction) -> Unit,
) {
    val step by viewModel.step.collectAsStateWithLifecycle()
    val stepCount by viewModel.stepCount.collectAsStateWithLifecycle()
    val city by viewModel.city.collectAsStateWithLifecycle()
    val streetsState by viewModel.streetsState.collectAsStateWithLifecycle()
    val buildingsState by viewModel.buildingsState.collectAsStateWithLifecycle()
    val isBuildingSelectionEnabled by viewModel.isBuildingSelectionEnabled.collectAsStateWithLifecycle()
    val deliveryOptionsState by viewModel.deliveryOptionsState.collectAsStateWithLifecycle()
    val isContinueButtonVisible by viewModel.isContinueButtonVisible.collectAsStateWithLifecycle()

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
        isBuildingSelectionEnabled = isBuildingSelectionEnabled,
        streetsState = streetsState,
        buildingsState = buildingsState,
        onStreetSelected = viewModel::onStreetSelected,
        onBuildingSelected = viewModel::onBuildingSelected,
        onStreetsErrorRefreshClicked = viewModel::onStreetsErrorRefreshClicked,
        onBuildingsErrorRefreshClicked = viewModel::onBuildingsErrorRefreshClicked,
        deliveryOptionsState = deliveryOptionsState,
        onDeliveryOptionClicked = viewModel::onDeliveryOptionClicked,
        onDeliveryOptionDateClicked = viewModel::onDeliveryOptionDateClicked,
        onDeliveryOptionTimeClicked = viewModel::onDeliveryOptionTimeClicked,
        isContinueButtonVisible = isContinueButtonVisible,
        onDeliveryOptionsErrorRefreshClicked = viewModel::onDeliveryOptionsErrorRefreshClicked,
        onContinueClicked = viewModel::onContinueClicked,
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
    isBuildingSelectionEnabled: Boolean,
    streetsState: CheckoutAddressViewModelComponent.State,
    buildingsState: CheckoutAddressViewModelComponent.State,
    onStreetSelected: (CheckoutAddressViewModelComponent.Item) -> Unit,
    onBuildingSelected: (CheckoutAddressViewModelComponent.Item) -> Unit,
    onStreetsErrorRefreshClicked: () -> Unit,
    onBuildingsErrorRefreshClicked: () -> Unit,
    deliveryOptionsState: DeliveryOptionsState?,
    onDeliveryOptionClicked: (DeliveryOption) -> Unit,
    onDeliveryOptionDateClicked: (DeliveryOption) -> Unit,
    onDeliveryOptionTimeClicked: (DeliveryOption) -> Unit,
    onDeliveryOptionsErrorRefreshClicked: () -> Unit,
    isContinueButtonVisible: Boolean,
    onContinueClicked: () -> Unit,
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
    var visibleAddressPartSelectorBottomSheet by remember {
        mutableStateOf<AddressPartBottomSheet?>(null)
    }
    val addressSelectorBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it == SheetValue.Expanded },
    )
    CheckoutAddressComponents.AddressSelectorBottomSheet(
        visibleAddressPartSelectorBottomSheet = visibleAddressPartSelectorBottomSheet,
        onDismissRequest = { visibleAddressPartSelectorBottomSheet = null },
        sheetState = addressSelectorBottomSheetState,
        onCloseClicked = {
            coroutineScope.launch {
                addressSelectorBottomSheetState.hide()
                visibleAddressPartSelectorBottomSheet = null
            }
        },
        streetTextFieldState = searchStreetTextFieldState,
        buildingTextFieldState = searchBuildingTextFieldState,
        apartmentTextFieldState = searchApartmentTextFieldState,
        streetsState = streetsState,
        buildingsState = buildingsState,
        onStreetSelected = onStreetSelected,
        onBuildingSelected = onBuildingSelected,
        onStreetsErrorRefreshClicked = onStreetsErrorRefreshClicked,
        onBuildingsErrorRefreshClicked = onBuildingsErrorRefreshClicked,
        modifier = Modifier.statusBarsPadding(),
    )

    var visibleDeliveryOptionDetails by remember { mutableStateOf<DeliveryOption?>(null) }
    CheckoutComponents.DeliveryOptionDetailsBottomSheet(
        visibleDeliveryOptionDetails = visibleDeliveryOptionDetails,
        onDismissRequest = { visibleDeliveryOptionDetails = null },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
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

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            CheckoutAddressComponents.AddressBlock(
                streetTextFieldState = streetTextFieldState,
                buildingTextFieldState = buildingTextFieldState,
                apartmentTextFieldState = apartmentTextFieldState,
                isBuildingSelectorClickable = isBuildingSelectionEnabled,
                onStreetClicked = {
                    visibleAddressPartSelectorBottomSheet = AddressPartBottomSheet.Street
                },
                onBuildingClicked = {
                    visibleAddressPartSelectorBottomSheet = AddressPartBottomSheet.Building
                },
            )

            CheckoutComponents.DeliveryOptions(
                state = deliveryOptionsState,
                onDeliveryOptionClicked = onDeliveryOptionClicked,
                onDeliveryOptionDateClicked = onDeliveryOptionDateClicked,
                onDeliveryOptionTimeClicked = onDeliveryOptionTimeClicked,
                onDeliveryOptionShowDetailsClicked = { visibleDeliveryOptionDetails = it },
                onDeliveryOptionsErrorRefreshClicked = onDeliveryOptionsErrorRefreshClicked,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            val navigationBarHeight = if (isContinueButtonVisible) {
                0.dp
            } else {
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            }
            Spacer(modifier = Modifier.height(20.dp + navigationBarHeight))
        }

        AnimatedVisibility(
            visible = isContinueButtonVisible,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
        ) {
            Column {
                ZarinaDivider(modifier = Modifier.fillMaxWidth())
                ZarinaButton(
                    onClick = onContinueClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                ) {
                    Text(text = stringResource(R.string.continue_).uppercase())
                }
            }
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
