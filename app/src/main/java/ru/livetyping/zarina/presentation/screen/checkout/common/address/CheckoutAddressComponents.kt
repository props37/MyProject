package ru.livetyping.zarina.presentation.screen.checkout.common.address

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.bottomsheet.ZarinaModalBottomSheet
import ru.livetyping.zarina.presentation.common.component.button.ZarinaCloseIconButton
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.util.compose.tryRequestFocus

object CheckoutAddressComponents {

    @Composable
    fun AddressBlock(
        streetTextFieldState: TextFieldState,
        buildingTextFieldState: TextFieldState,
        apartmentTextFieldState: TextFieldState,
        onStreetClicked: () -> Unit,
        onBuildingClicked: () -> Unit,
        onApartmentClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val streetInteractionSource = remember { MutableInteractionSource() }
            LaunchedEffect(streetInteractionSource) {
                streetInteractionSource.interactions.collect {
                    if (it is PressInteraction.Release) {
                        onStreetClicked()
                    }
                }
            }

            ZarinaTextField(
                state = streetTextFieldState,
                isEnabled = false,
                label = {
                    val text = if (streetTextFieldState.text.isNotEmpty()) {
                        stringResource(R.string.street)
                    } else ""
                    Text(text = text)
                },
                placeholder = {
                    Text(text = stringResource(R.string.street))
                },
                colors = ZarinaTextFieldDefaults.colorsIgnoringDisabled(),
                lineLimits = TextFieldLineLimits.SingleLine,
                interactionSource = streetInteractionSource,
                modifier = Modifier
                    .indication(streetInteractionSource, ripple())
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                val buildingInteractionSource = remember { MutableInteractionSource() }
                LaunchedEffect(buildingInteractionSource) {
                    buildingInteractionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            onBuildingClicked()
                        }
                    }
                }

                ZarinaTextField(
                    state = buildingTextFieldState,
                    isEnabled = false,
                    label = {
                        val text = if (buildingTextFieldState.text.isNotEmpty()) {
                            stringResource(R.string.building)
                        } else ""
                        Text(text = text)
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.building))
                    },
                    colors = ZarinaTextFieldDefaults.colorsIgnoringDisabled(),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    interactionSource = buildingInteractionSource,
                    modifier = Modifier
                        .weight(1f)
                        .indication(buildingInteractionSource, ripple())
                        .padding(horizontal = 16.dp),
                )

                val apartmentInteractionSource = remember { MutableInteractionSource() }
                LaunchedEffect(apartmentInteractionSource) {
                    apartmentInteractionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            onApartmentClicked()
                        }
                    }
                }

                ZarinaTextField(
                    state = apartmentTextFieldState,
                    isEnabled = false,
                    label = {
                        val text = if (apartmentTextFieldState.text.isNotEmpty()) {
                            stringResource(R.string.apartment_slash_office)
                        } else ""
                        Text(text = text)
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.apartment_slash_office))
                    },
                    colors = ZarinaTextFieldDefaults.colorsIgnoringDisabled(),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    interactionSource = apartmentInteractionSource,
                    modifier = Modifier
                        .weight(1f)
                        .indication(apartmentInteractionSource, ripple())
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    @Composable
    fun AddressSelectorBottomSheet(
        visibleAddressSlotSelectorBottomSheet: AddressSlot?,
        onDismissRequest: () -> Unit,
        sheetState: SheetState,
        onCloseClicked: () -> Unit,
        streetTextFieldState: TextFieldState,
        buildingTextFieldState: TextFieldState,
        apartmentTextFieldState: TextFieldState,
        modifier: Modifier = Modifier,
    ) {
        if (visibleAddressSlotSelectorBottomSheet != null) {
            ZarinaModalBottomSheet(
                onDismissRequest = onDismissRequest,
                sheetState = sheetState,
                windowInsets = {
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                },
                properties = remember {
                    ModalBottomSheetProperties(shouldDismissOnBackPress = false)
                },
                modifier = modifier,
            ) {
                BackHandler(enabled = !WindowInsets.isImeVisible) {
                    onCloseClicked()
                }

                val titleResId = when (visibleAddressSlotSelectorBottomSheet) {
                    AddressSlot.Street -> R.string.street
                    AddressSlot.Building -> R.string.building
                    AddressSlot.Apartment -> R.string.apartment_or_office
                }
                val textFieldState = when (visibleAddressSlotSelectorBottomSheet) {
                    AddressSlot.Street -> streetTextFieldState
                    AddressSlot.Building -> buildingTextFieldState
                    AddressSlot.Apartment -> apartmentTextFieldState
                }
                val textFieldPlaceholderResId = when (visibleAddressSlotSelectorBottomSheet) {
                    AddressSlot.Street -> R.string.search_streets
                    AddressSlot.Building -> R.string.search_building
                    AddressSlot.Apartment -> R.string.search_apartments_slash_offices
                }

                AddressSelectorBottomSheetContent(
                    title = stringResource(titleResId),
                    onCloseClicked = onCloseClicked,
                    textFieldState = textFieldState,
                    textFieldPlaceholder = stringResource(textFieldPlaceholderResId),
                    sheetState = sheetState,
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AddressSelectorBottomSheetContent(
        title: String,
        onCloseClicked: () -> Unit,
        textFieldState: TextFieldState,
        textFieldPlaceholder: String,
        sheetState: SheetState,
        modifier: Modifier = Modifier,
    ) {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(focusRequester, sheetState) {
            snapshotFlow { sheetState.currentValue }.collect {
                if (it == SheetValue.Expanded) {
                    focusRequester.tryRequestFocus()
                }
            }
        }

        Column(modifier = modifier.fillMaxSize()) {
            ZarinaTopBar(
                centerContent = {
                    Text(text = title)
                },
                endContent = {
                    ZarinaCloseIconButton(
                        onClick = onCloseClicked,
                        iconSize = 20.dp,
                        modifier = Modifier.padding(end = 2.dp),
                    )
                },
                contentPadding = PaddingValues(vertical = 4.dp),
            )

            val focusState = remember { mutableStateOf<FocusState?>(null) }
            ZarinaTextField(
                state = textFieldState,
                placeholder = {
                    Text(
                        text = textFieldPlaceholder,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_magnifying_glass_24),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = textFieldState.text.isNotEmpty(),
                        onClick = { textFieldState.clearText() },
                    )
                },
                outerTrailingContent = {
                    val focusManager = LocalFocusManager.current
                    ZarinaTextFieldDefaults.CancelButton(
                        isVisible = focusState.value?.isFocused == true,
                        onClick = { focusManager.clearFocus() },
                    )
                },
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .onFocusChanged { focusState.value = it }
                    .focusRequester(focusRequester),
            )
        }
    }

    enum class AddressSlot { Street, Building, Apartment }
}
