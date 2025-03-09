package ru.livetyping.zarina.feature.search.ui.impl.impl.search.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.button.ZarinaFilterIconButton
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldSize
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.model.SearchBarEvent
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.model.SearchBarState
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.model.SearchMode
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SearchBar(
    state: SearchBarState,
    onEvent: (SearchBarEvent) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    val searchMode = state.searchMode

    val startPadding by animateDpAsState(
        targetValue = when (searchMode) {
            SearchMode.SEARCH -> 16.dp
            SearchMode.RESULTS -> 2.dp
        },
        label = "start padding",
    )
    val endPadding by animateDpAsState(
        targetValue = when (searchMode) {
            SearchMode.SEARCH -> 16.dp
            SearchMode.RESULTS -> 2.dp
        },
        label = "end padding",
    )
    val contentPadding = PaddingValues(
        start = startPadding,
        top = 4.dp,
        end = endPadding,
        bottom = 4.dp,
    )

    ZarinaTopBar(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        AnimatedVisibility(
            visible = searchMode == SearchMode.RESULTS,
            enter = remember { fadeIn() + expandHorizontally() },
            exit = remember { fadeOut() + shrinkHorizontally() },
        ) {
            ZarinaBackIconButton(
                onClick = { onEvent(SearchBarEvent.BackClicked) },
                iconSize = 20.dp,
            )
        }

        val keyboardController = LocalSoftwareKeyboardController.current

        val focusState = remember { mutableStateOf<FocusState?>(null) }

        val textFieldState = state.textFieldState
        ZarinaTextField(
            state = textFieldState,
            size = ZarinaTextFieldSize.Small,
            placeholder = {
                Text(text = stringResource(RCommon.string.res_find_products))
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
                    isVisible = textFieldState.text.isNotEmpty(),
                    onClick = {
                        textFieldState.clearText()
                        if (focusRequester.tryRequestFocus()) {
                            keyboardController?.show()
                        }
                    },
                )
            },
            outerTrailingContent = {
                ZarinaTextFieldDefaults.CancelButton(
                    isVisible = focusState.value?.isFocused == true,
                    onClick = { onEvent(SearchBarEvent.CancelClicked) },
                )
            },
            keyboardOptions = remember {
                KeyboardOptions(imeAction = ImeAction.Search)
            },
            onKeyboardAction = { defaultAction ->
                defaultAction()
                onEvent(SearchBarEvent.SearchClicked)
            },
            lineLimits = TextFieldLineLimits.SingleLine,
            modifier = Modifier
                .weight(1f)
                .onFocusChanged {
                    focusState.value = it
                    if (it.isFocused) onEvent(SearchBarEvent.Focused)
                }
                .focusRequester(focusRequester),
        )

        AnimatedVisibility(
            visible = searchMode == SearchMode.RESULTS,
            enter = remember { fadeIn() + expandHorizontally(expandFrom = Alignment.Start) },
            exit = remember { fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.Start) },
        ) {
            ZarinaFilterIconButton(
                onClick = { onEvent(SearchBarEvent.FiltersClicked) },
                appliedFilterCount = state.appliedFilterCount,
                iconSize = 20.dp,
            )
        }
    }
}
