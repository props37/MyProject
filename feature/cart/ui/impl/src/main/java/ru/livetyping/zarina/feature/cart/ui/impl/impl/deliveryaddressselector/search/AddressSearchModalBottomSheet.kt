package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uicompose.none
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaModalBottomSheet
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddressSearchModalBottomSheet(
    state: AddressSearchBottomSheetState,
    onEvent: (AddressSearchEvent) -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing }
) {
    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (state is AddressSearchBottomSheetState.Visible) {
        val type = state.type

        ZarinaModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { onEvent(AddressSearchEvent.CloseClicked) },
            windowInsets = WindowInsets.none,
            modifier = modifier
                .windowInsetsPadding(windowInsetsProvider().only(WindowInsetsSides.Top)),
        ) {
            val titleResId = when (type) {
                AddressSearchType.Street -> R.string.cart_street
                AddressSearchType.Building -> R.string.cart_building
            }
            val textFieldPlaceholderResId = when (type) {
                AddressSearchType.Street -> R.string.cart_search_streets
                AddressSearchType.Building -> R.string.cart_search_building
            }

            val onCloseClicked: () -> Unit = {
                coroutineScope
                    .launch { sheetState.hide() }
                    .invokeOnCompletion { onEvent(AddressSearchEvent.CloseClicked) }
            }

            Content(
                title = stringResource(titleResId),
                onCloseClicked = onCloseClicked,
                textFieldState = state.searchTextFieldState,
                textFieldPlaceholder = stringResource(textFieldPlaceholderResId),
                addressSearchState = state.searchState,
                onAddressItemClicked = {
                    onEvent(AddressSearchEvent.AddressItemClicked(it, type))
                    onCloseClicked()
                },
                onErrorRefreshClicked = {
                    onEvent(AddressSearchEvent.ErrorRefreshClicked(type))
                },
                sheetState = sheetState,
                windowInsetsProvider = windowInsetsProvider,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    title: String,
    onCloseClicked: () -> Unit,
    textFieldState: TextFieldState,
    textFieldPlaceholder: String,
    addressSearchState: AddressSearchState,
    onAddressItemClicked: (AddressSearchItem) -> Unit,
    onErrorRefreshClicked: () -> Unit,
    sheetState: SheetState,
    windowInsetsProvider: @Composable () -> WindowInsets,
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
                Text(
                    text = title.uppercase(),
                    style = UiKitTheme2.typography.h4,
                )
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

        SearchTextField(
            state = textFieldState,
            placeholder = textFieldPlaceholder,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .focusRequester(focusRequester),
        )

        AddressSearch(
            state = addressSearchState,
            onAddressItemClicked = onAddressItemClicked,
            onErrorRefreshClicked = onErrorRefreshClicked,
            windowInsetsProvider = windowInsetsProvider,
        )
    }
}

@Composable
private fun SearchTextField(
    state: TextFieldState,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val focusState = remember { mutableStateOf<FocusState?>(null) }

    ZarinaTextField(
        state = state,
        placeholder = {
            Text(
                text = placeholder.uppercase(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingContent = {
            Icon(
                imageVector = ImageVector.vectorResource(RCommon.drawable.ic_magnifying_glass_24),
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
        modifier = modifier.onFocusChanged { focusState.value = it },
    )
}
