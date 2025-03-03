package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.feature.cityselector.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun CitySearchTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
) {
    val focusState = remember { mutableStateOf<FocusState?>(null) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ZarinaTextField(
        state = state,
        placeholder = {
            Text(text = stringResource(R.string.city_selector_search_city))
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
                onClick = {
                    state.clearText()
                    if (focusRequester.tryRequestFocus()) {
                        keyboardController?.show()
                    }
                },
            )
        },
        outerTrailingContent = {
            ZarinaTextFieldDefaults.CancelButton(
                isVisible = focusState.value?.isFocused == true,
                onClick = {
                    if (focusState.value?.isFocused == true) {
                        focusManager.clearFocus()
                    }
                },
            )
        },
        lineLimits = TextFieldLineLimits.SingleLine,
        modifier = modifier
            .onFocusChanged { focusState.value = it }
            .focusRequester(focusRequester),
    )
}
