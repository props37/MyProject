package ru.livetyping.zarina.presentation.screen.productsearch

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldSize
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SearchMode
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec

object ProductSearchScreenComponents {

    @Composable
    fun TopBar(
        searchTextFieldState: TextFieldState,
        onSearchTextFieldSearchClicked: () -> Unit,
        onSearchTextFieldFocused: () -> Unit,
        onSearchTextFieldCancelClicked: () -> Unit,
        searchMode: SearchMode,
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val startPadding by animateDpAsState(
            targetValue = when (searchMode) {
                SearchMode.SEARCH -> 16.dp
                SearchMode.SEARCH_RESULTS -> 2.dp
            },
            label = "start padding",
        )
        val contentPadding = PaddingValues(
            start = startPadding,
            top = 4.dp,
            end = 16.dp,
            bottom = 4.dp,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .heightIn(min = TopBarDefaults.MinHeight)
                .padding(contentPadding),
        ) {
            AnimatedVisibility(
                visible = searchMode == SearchMode.SEARCH_RESULTS,
                enter = remember { fadeIn() + expandHorizontally() },
                exit = remember { fadeOut() + shrinkHorizontally() },
            ) {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                )
            }

            val focusState = remember { mutableStateOf<FocusState?>(null) }
            ZarinaTextField(
                state = searchTextFieldState,
                size = ZarinaTextFieldSize.Small,
                placeholder = {
                    Text(text = stringResource(R.string.find_products))
                },
                leadingContent = {
                    Icon(
                        painter = painterResource(R.drawable.ic_magnifying_glass_24),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = searchTextFieldState.text.isNotEmpty(),
                        onClick = searchTextFieldState::clearText,
                    )
                },
                outerTrailingContent = {
                    val isCancelButtonVisible = focusState.value?.isFocused == true
                    AnimatedContent(
                        targetState = isCancelButtonVisible,
                        transitionSpec = {
                            AnimatedContentDefaultTransitionSpec().using(SizeTransform(clip = false))
                        },
                        contentAlignment = Alignment.Center,
                        label = "SearchBar Cancel button",
                    ) { isVisible ->
                        if (isVisible) {
                            ZarinaTextFieldDefaults.CancelButton(
                                onClick = onSearchTextFieldCancelClicked,
                            )
                        }
                    }
                },
                keyboardOptions = remember {
                    KeyboardOptions(imeAction = ImeAction.Search)
                },
                onKeyboardAction = {
                    onSearchTextFieldSearchClicked()
                },
                modifier = Modifier.onFocusChanged {
                    focusState.value = it
                    if (it.isFocused) onSearchTextFieldFocused()
                },
            )
        }
    }
}
