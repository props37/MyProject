package ru.livetyping.zarina.presentation.screen.productsearch

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec

object ProductSearchScreenComponents {

    @Composable
    fun SearchBar(
        query: String,
        onQueryChanged: (String) -> Unit,
        onCancelClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val focusState = remember { mutableStateOf<FocusState?>(null) }

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.heightIn(min = TopBarDefaults.MinHeight),
        ) {
            ZarinaTextField(
                value = query,
                onValueChanged = onQueryChanged,
                textStyle = UiKitTheme.typography.secondary.light,
                placeholder = { Text(text = stringResource(R.string.find_products)) },
                leadingContent = {
                    Icon(
                        painter = painterResource(R.drawable.ic_magnifying_glass_24),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = query.isNotEmpty(),
                        onClick = { onQueryChanged("") },
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
                            ZarinaTextFieldDefaults.CancelButton(onClick = onCancelClicked)
                        }
                    }
                },
                modifier = Modifier.onFocusChanged { focusState.value = it },
            )
        }
    }
}
