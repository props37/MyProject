package ru.livetyping.zarina.presentation.screen.checkout.common.address

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.bottomsheet.ZarinaModalBottomSheet
import ru.livetyping.zarina.presentation.common.component.button.ZarinaCloseIconButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade
import ru.livetyping.zarina.util.compose.navigationBarsOrIme
import ru.livetyping.zarina.util.compose.tryRequestFocus

@Suppress("ConstPropertyName")
object CheckoutAddressComponents {

    @Composable
    fun AddressBlock(
        streetTextFieldState: TextFieldState,
        buildingTextFieldState: TextFieldState,
        apartmentTextFieldState: TextFieldState,
        isBuildingSelectorClickable: Boolean,
        onStreetClicked: () -> Unit,
        onBuildingClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val focusManager = LocalFocusManager.current

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
                LaunchedEffect(buildingInteractionSource, isBuildingSelectorClickable) {
                    buildingInteractionSource.interactions.collect {
                        if (it is PressInteraction.Release && isBuildingSelectorClickable) {
                            onBuildingClicked()
                        }
                    }
                }
                val buildingIndicationModifier = if (isBuildingSelectorClickable) {
                    Modifier.indication(buildingInteractionSource, ripple())
                } else Modifier

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
                        .then(buildingIndicationModifier)
                        .padding(horizontal = 16.dp),
                )

                ZarinaTextField(
                    state = apartmentTextFieldState,
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
                    keyboardOptions = remember {
                        KeyboardOptions(keyboardType = KeyboardType.Number)
                    },
                    onKeyboardAction = { default ->
                        focusManager.clearFocus()
                        default()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    @Composable
    fun AddressSelectorBottomSheet(
        visibleAddressPartSelectorBottomSheet: AddressPartBottomSheet?,
        onDismissRequest: () -> Unit,
        sheetState: SheetState,
        onCloseClicked: () -> Unit,
        streetTextFieldState: TextFieldState,
        buildingTextFieldState: TextFieldState,
        apartmentTextFieldState: TextFieldState,
        streetsState: CheckoutAddressViewModelComponent.State,
        buildingsState: CheckoutAddressViewModelComponent.State,
        onStreetSelected: (CheckoutAddressViewModelComponent.Item) -> Unit,
        onBuildingSelected: (CheckoutAddressViewModelComponent.Item) -> Unit,
        onStreetsErrorRefreshClicked: () -> Unit,
        onBuildingsErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        if (visibleAddressPartSelectorBottomSheet != null) {
            ZarinaModalBottomSheet(
                onDismissRequest = onDismissRequest,
                sheetState = sheetState,
                properties = remember {
                    ModalBottomSheetProperties(shouldDismissOnBackPress = false)
                },
                modifier = modifier,
            ) {
                BackHandler(enabled = !WindowInsets.isImeVisible) {
                    onCloseClicked()
                }

                val titleResId = when (visibleAddressPartSelectorBottomSheet) {
                    AddressPartBottomSheet.Street -> R.string.street
                    AddressPartBottomSheet.Building -> R.string.building
                }
                val textFieldState = when (visibleAddressPartSelectorBottomSheet) {
                    AddressPartBottomSheet.Street -> streetTextFieldState
                    AddressPartBottomSheet.Building -> buildingTextFieldState
                }
                val textFieldPlaceholderResId = when (visibleAddressPartSelectorBottomSheet) {
                    AddressPartBottomSheet.Street -> R.string.search_streets
                    AddressPartBottomSheet.Building -> R.string.search_building
                }
                val addressSelectorState = when (visibleAddressPartSelectorBottomSheet) {
                    AddressPartBottomSheet.Street -> streetsState
                    AddressPartBottomSheet.Building -> buildingsState
                }
                val onAddressItemClicked = when (visibleAddressPartSelectorBottomSheet) {
                    AddressPartBottomSheet.Street -> onStreetSelected
                    AddressPartBottomSheet.Building -> onBuildingSelected
                }
                val onErrorRefreshClicked = when (visibleAddressPartSelectorBottomSheet) {
                    AddressPartBottomSheet.Street -> onStreetsErrorRefreshClicked
                    AddressPartBottomSheet.Building -> onBuildingsErrorRefreshClicked
                }

                AddressSelectorBottomSheetContent(
                    title = stringResource(titleResId),
                    onCloseClicked = onCloseClicked,
                    textFieldState = textFieldState,
                    textFieldPlaceholder = stringResource(textFieldPlaceholderResId),
                    addressSelectorState = addressSelectorState,
                    onAddressItemClicked = {
                        onAddressItemClicked(it)
                        onCloseClicked()
                    },
                    onErrorRefreshClicked = onErrorRefreshClicked,
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
        addressSelectorState: CheckoutAddressViewModelComponent.State,
        onAddressItemClicked: (CheckoutAddressViewModelComponent.Item) -> Unit,
        onErrorRefreshClicked: () -> Unit,
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

            AddressSelectorTextField(
                state = textFieldState,
                placeholder = textFieldPlaceholder,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester),
            )

            AddressSelectorBottomSheetContentItems(
                addressSelectorState = addressSelectorState,
                onAddressItemClicked = onAddressItemClicked,
                onErrorRefreshClicked = onErrorRefreshClicked,
                sheetState = sheetState,
            )
        }
    }

    @Composable
    private fun AddressSelectorTextField(
        state: TextFieldState,
        placeholder: String,
        modifier: Modifier = Modifier,
    ) {
        val focusState = remember { mutableStateOf<FocusState?>(null) }
        ZarinaTextField(
            state = state,
            placeholder = {
                Text(
                    text = placeholder,
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
                    isVisible = state.text.isNotEmpty(),
                    onClick = { state.clearText() },
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
            modifier = modifier.onFocusChanged { focusState.value = it },
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AddressSelectorBottomSheetContentItems(
        addressSelectorState: CheckoutAddressViewModelComponent.State,
        onAddressItemClicked: (CheckoutAddressViewModelComponent.Item) -> Unit,
        onErrorRefreshClicked: () -> Unit,
        sheetState: SheetState,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = addressSelectorState,
            contentKey = {
                when (it) {
                    is CheckoutAddressViewModelComponent.State.Items -> {
                        AddressSelectorContentKeyItems
                    }

                    CheckoutAddressViewModelComponent.State.Loading -> it
                    is CheckoutAddressViewModelComponent.State.Error -> it
                    CheckoutAddressViewModelComponent.State.Empty -> it
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is CheckoutAddressViewModelComponent.State.Items -> {
                    AddressSelectorBottomSheetContentItemsImpl(
                        items = state.items,
                        onItemClicked = onAddressItemClicked,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                CheckoutAddressViewModelComponent.State.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.navigationBarsOrIme),
                    ) {
                        ZarinaCircularLoader(modifier = Modifier.size(40.dp))
                    }
                }

                is CheckoutAddressViewModelComponent.State.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.navigationBarsOrIme)
                            .padding(16.dp),
                    )
                }

                CheckoutAddressViewModelComponent.State.Empty -> Unit
            }
        }
    }

    @Composable
    private fun AddressSelectorBottomSheetContentItemsImpl(
        items: List<CheckoutAddressViewModelComponent.Item>,
        onItemClicked: (CheckoutAddressViewModelComponent.Item) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 20.dp),
            modifier = modifier,
        ) {
            itemsIndexed(
                items = items,
                key = { _, item -> item.addressPart.id.value },
            ) { index, item ->
                Column(modifier = Modifier.animateItem()) {
                    ZarinaItem(
                        onClick = { onItemClicked(item) },
                    ) {
                        Text(
                            text = item.addressPart.name,
                            style = UiKitTheme.typography.secondary.light,
                            color = UiKitTheme.colors.text.general.regular.default,
                        )
                    }
                }

                if (index < items.lastIndex) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }

    enum class AddressPartBottomSheet { Street, Building }

    private const val AddressSelectorContentKeyItems = "AddressSelectorContentKeyItems"
}
