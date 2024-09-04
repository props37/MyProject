package ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTab
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryViewModel.ViewMode

object CheckoutPickupPointDeliveryComponents {

    @Composable
    fun FilterBlock(
        nameOrAddressFilterTextFieldState: TextFieldState,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            var focusState by remember { mutableStateOf<FocusState?>(null) }
            ZarinaTextField(
                state = nameOrAddressFilterTextFieldState,
                lineLimits = TextFieldLineLimits.SingleLine,
                leadingContent = {
                   Icon(
                       imageVector = ImageVector.vectorResource(R.drawable.ic_magnifying_glass_24),
                       contentDescription = null,
                       modifier = Modifier.size(16.dp),
                   )
                },
                placeholder = {
                    Text(text = stringResource(R.string.address_or_name))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = nameOrAddressFilterTextFieldState.text.isNotBlank(),
                        onClick = { nameOrAddressFilterTextFieldState.clearText() },
                    )
                },
                outerTrailingContent = {
                    val focusManager = LocalFocusManager.current
                    ZarinaTextFieldDefaults.CancelButton(
                        isVisible = focusState?.isFocused == true,
                        onClick = { focusManager.clearFocus() },
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .onFocusChanged { focusState = it },
            )
        }
    }

    @Composable
    fun ViewModeTabRow(
        modes: List<ViewMode>,
        currentMode: ViewMode,
        onModeChanged: (ViewMode) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTabRow(
            selectedTabIndex = modes.indexOf(currentMode),
            modifier = modifier,
        ) {
            modes.forEach { mode ->
                val textResId = when (mode) {
                    ViewMode.MAP -> R.string.map
                    ViewMode.LIST -> R.string.list
                }

                ZarinaTab(
                    text = stringResource(textResId),
                    isSelected = mode == currentMode,
                    onClick = { onModeChanged(mode) },
                )
            }
        }
    }

    @Composable
    fun ViewModeHorizontalPager(
        pagerState: PagerState,
        viewModes: List<ViewMode>,
        modifier: Modifier = Modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            val viewMode = viewModes[page]
            // TODO: [High] Implement
        }
    }
}
