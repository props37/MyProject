package ru.zarina.zarina.ui.screen.catalog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import ru.zarina.zarina.ui.common.component.TopBarDefaults
import ru.zarina.zarina.ui.common.component.ZarinaTabIndicator
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextField
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextFieldDefaults
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel.GenderPickerTab
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultExitTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultTransitionSpec

object CatalogScreenComponents {

    @Composable
    fun SearchBar(
        searchQuery: String,
        onSearchQueryChanged: (String) -> Unit,
        onClearClicked: () -> Unit,
        onCancelClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.heightIn(min = TopBarDefaults.MinHeight),
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
                    AnimatedVisibility(
                        visible = searchQuery.isNotEmpty(),
                        enter = remember { AnimatedContentDefaultEnterTransition },
                        exit = remember { AnimatedContentDefaultExitTransition },
                    ) {
                        ZarinaTextFieldDefaults.ClearButton(onClick = onClearClicked)
                    }
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
                singleLine = true,
                modifier = Modifier.onFocusChanged { focusState.value = it },
            )
        }
    }

    @Composable
    fun GenderPicker(
        tabs: List<GenderPickerTab>,
        currentTab: GenderPickerTab,
        onTabClicked: (GenderPickerTab) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val selectedTabIndex = remember(tabs, currentTab) {
            tabs.indexOf(currentTab)
        }

        TabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = Color.Unspecified,
            indicator = { tabPositions ->
                ZarinaTabIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                )
            },
            divider = {},
            modifier = modifier,
        ) {
            tabs.forEach { tab ->
                ZarinaButton(
                    onClick = { onTabClicked(tab) },
                    size = ZarinaButtonSize.Medium,
                    colors = ZarinaButtonDefaults.backlessColors(),
                    contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
                ) {
                    val textResId = when (tab) {
                        GenderPickerTab.FOR_WOMEN -> R.string.for_women
                        GenderPickerTab.FOR_MEN -> R.string.for_men
                    }

                    val style = if (tab == currentTab) {
                        UiKitTheme.typographyReworked.tertiary.regular
                    } else {
                        UiKitTheme.typographyReworked.tertiary.light
                    }

                    Text(
                        text = stringResource(textResId).uppercase(),
                        style = style,
                        color = UiKitTheme.colorsReworked.text.general.regular.default,
                    )
                }
            }
        }
    }
}
