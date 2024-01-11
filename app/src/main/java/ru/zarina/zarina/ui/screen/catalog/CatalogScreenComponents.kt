package ru.zarina.zarina.ui.screen.catalog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.button.IconButtonCustom
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextField
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultExitTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultTransitionSpec

object CatalogScreenComponents {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun SearchBar(
        searchQuery: String,
        onSearchQueryChanged: (String) -> Unit,
        onClearClicked: () -> Unit,
        onCancelClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val focusState = remember { mutableStateOf<FocusState?>(null) }

        ZarinaTextField(
            value = searchQuery,
            onValueChanged = onSearchQueryChanged,
            placeholder = {
                Text(text = stringResource(R.string.find_products))
            },
            leadingContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_search_24),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            },
            innerTrailingContent = {
                // TODO: [High] Extract
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    AnimatedVisibility(
                        visible = searchQuery.isNotEmpty(),
                        enter = remember { AnimatedContentDefaultEnterTransition },
                        exit = remember { AnimatedContentDefaultExitTransition },
                    ) {
                        IconButtonCustom(
                            onClick = onClearClicked,
                            indication = rememberRipple(bounded = false, radius = 8.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_clear_new_24),
                                contentDescription = stringResource(R.string.clear),
                                tint = Color.Unspecified,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                }
            },
            outerTrailingContent = {
                // TODO: [High] Extract
                val isCancelButtonVisible = focusState.value?.isFocused == true
                AnimatedContent(
                    targetState = isCancelButtonVisible,
                    transitionSpec = {
                        AnimatedContentDefaultTransitionSpec().using(SizeTransform(clip = false))
                    },
                    contentAlignment = Alignment.Center,
                    label = "", // TODO: [High] Specify
                ) { isVisible ->
                    if (isVisible) {
                        ZarinaButton(
                            onClick = onCancelClicked,
                            size = ZarinaButtonSize.Small,
                            colors = ZarinaButtonDefaults.backlessColors(),
                        ) {
                            Text(
                                text = stringResource(R.string.cancel).uppercase(),
                                style = UiKitTheme.typographyReworked.caption1.regular,
                            )
                        }
                    }
                }
            },
            singleLine = true,
            modifier = modifier.onFocusChanged { focusState.value = it },
        )
    }
}
