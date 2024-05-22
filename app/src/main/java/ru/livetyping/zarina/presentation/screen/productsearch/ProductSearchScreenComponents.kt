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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.Divider
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldSize
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SearchMode
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.util.kotlin.findSubstringBounds

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

    @Composable
    fun SearchSuggestions(
        query: String,
        suggestions: ProductSearchSuggestions?,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(modifier = modifier) {
            val resultSuggestions = suggestions?.resultSuggestions
            if (!resultSuggestions.isNullOrEmpty()) {
                item(
                    key = SearchSuggestionsKeyResultsTitle,
                    contentType = SearchSuggestionsContentTypeTitle,
                ) {
                    ZarinaItem(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .animateItem(),
                    ) {
                        Text(
                            text = stringResource(R.string.search_results),
                            style = SearchSuggestionTitleTextStyle,
                            color = UiKitTheme.colors.text.general.regular.default,
                        )
                    }
                }

                itemsIndexed(
                    items = resultSuggestions,
                    key = { _, text -> "$SearchSuggestionsKeyResultItemPrefix $text" },
                    contentType = { _, _ -> SearchSuggestionsContentTypeResultItem },
                ) { index, text ->
                    ZarinaItem(
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .animateItem(),
                    ) {
                        val textWithQueryMatch = rememberSuggestionItemTextWithQueryMatch(
                            text = text,
                            query = query,
                        )

                        Icon(
                            painter = painterResource(R.drawable.ic_magnifying_glass_24),
                            contentDescription = null,
                            tint = UiKitTheme.colors.icon.regular.default,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = textWithQueryMatch,
                            style = SearchSuggestionItemTextStyle,
                            color = UiKitTheme.colors.text.general.regular.default,
                        )
                    }

                    if (index < resultSuggestions.lastIndex) {
                        Divider(
                            color = UiKitTheme.colors.background.skeleton,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .animateItem(),
                        )
                    }
                }
            }

            val categories = suggestions?.categories
            if (!categories.isNullOrEmpty()) {
                item(
                    key = SearchSuggestionsKeyCategoriesTitle,
                    contentType = SearchSuggestionsContentTypeTitle,
                ) {
                    ZarinaItem(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .animateItem(),
                    ) {
                        Text(
                            text = stringResource(R.string.categories),
                            style = SearchSuggestionTitleTextStyle,
                            color = UiKitTheme.colors.text.general.regular.default,
                        )
                    }
                }

                itemsIndexed(
                    items = categories,
                    key = { _, category ->
                        "$SearchSuggestionsKeyCategoryItemPrefix ${category.id}"
                    },
                    contentType = { _, _ -> SearchSuggestionsContentTypeCategoryItem },
                ) { index, category ->
                    ZarinaItem(
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .animateItem(),
                    ) {
                        val categoryNameWithQueryMatch = rememberSuggestionItemTextWithQueryMatch(
                            text = category.name,
                            query = query,
                        )

                        Text(
                            text = categoryNameWithQueryMatch,
                            style = SearchSuggestionItemTextStyle,
                            color = UiKitTheme.colors.text.general.regular.default,
                        )
                    }

                    if (index < categories.lastIndex) {
                        Divider(
                            color = UiKitTheme.colors.background.skeleton,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .animateItem(),
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun rememberSuggestionItemTextWithQueryMatch(
        text: String,
        query: String,
    ): AnnotatedString {
        val matchTextStyle = SearchSuggestionItemQueryMatchTextStyle
        return remember(text, query, matchTextStyle) {
            buildAnnotatedString {
                append(text)
                val queryMatchBounds = text.findSubstringBounds(query, ignoreCase = true)
                if (queryMatchBounds != null) {
                    addStyle(
                        style = matchTextStyle.toSpanStyle(),
                        start = queryMatchBounds.first,
                        end = queryMatchBounds.last,
                    )
                }
            }
        }
    }

    private val SearchSuggestionTitleTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.bold

    private val SearchSuggestionItemTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.light

    private val SearchSuggestionItemQueryMatchTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.regular

    private const val SearchSuggestionsKeyResultsTitle = "SearchSuggestionsKeyResultsTitle"
    private const val SearchSuggestionsKeyCategoriesTitle = "SearchSuggestionsKeyCategoriesTitle"
    private const val SearchSuggestionsKeyResultItemPrefix =
        "SearchSuggestionsKeyResultItemPrefix"
    private const val SearchSuggestionsKeyCategoryItemPrefix =
        "SearchSuggestionsKeyCategoryItemPrefix"

    private const val SearchSuggestionsContentTypeTitle = "SearchSuggestionsKeyResultsTitle"
    private const val SearchSuggestionsContentTypeResultItem =
        "SearchSuggestionsContentTypeResultItem"
    private const val SearchSuggestionsContentTypeCategoryItem =
        "SearchSuggestionsContentTypeCategoryItem"
}
