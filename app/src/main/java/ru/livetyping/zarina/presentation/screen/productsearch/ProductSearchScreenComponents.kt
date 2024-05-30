package ru.livetyping.zarina.presentation.screen.productsearch

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import ru.livetyping.zarina.presentation.base.text.textString
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.tag.ZarinaTag
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldSize
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.common.error.rememberErrorState
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SearchMode
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SearchSuggestionItem
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SearchSuggestionsState
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.util.compose.animation.Crossfade
import ru.livetyping.zarina.util.kotlin.capitalize
import ru.livetyping.zarina.util.kotlin.findSubstringBounds

@Suppress("ConstPropertyName")
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
                        imageVector = ImageVector.vectorResource(R.drawable.ic_magnifying_glass_24),
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
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier.onFocusChanged {
                    focusState.value = it
                    if (it.isFocused) onSearchTextFieldFocused()
                },
            )
        }
    }

    @Composable
    fun SearchSuggestions(
        state: SearchSuggestionsState,
        autocompleteSuggestions: ImmutableList<ProductSearchSuggestions.AutocompleteSuggestion>,
        query: String,
        onSearchSuggestionItemClicked: (SearchSuggestionItem) -> Unit,
        onAutocompleteSuggestionClicked: (ProductSearchSuggestions.AutocompleteSuggestion) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            SearchAutocompleteSuggestions(
                suggestions = autocompleteSuggestions,
                onSuggestionClicked = onAutocompleteSuggestionClicked,
                modifier = Modifier.fillMaxWidth(),
            )

            Crossfade(
                targetState = state,
                contentKey = {
                    when (it) {
                        is SearchSuggestionsState.Suggestions -> SearchSuggestionsContentKeySuggestions

                        SearchSuggestionsState.Empty,
                        is SearchSuggestionsState.Error, SearchSuggestionsState.Loading -> it
                    }
                },
                label = "SearchSuggestions",
            ) { state ->
                when (state) {
                    is SearchSuggestionsState.Suggestions -> {
                        SearchSuggestionList(
                            query = query,
                            items = state.items,
                            onItemClicked = onSearchSuggestionItemClicked,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    SearchSuggestionsState.Loading -> Unit

                    SearchSuggestionsState.Empty -> {
                        NothingFoundPlaceholder(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                        )
                    }

                    is SearchSuggestionsState.Error -> {
                        ZarinaErrorScreen(
                            state = state.state,
                            onButtonClicked = {},
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun NothingFoundPlaceholder(
        modifier: Modifier = Modifier,
    ) {
        val errorState = rememberErrorState(
            iconResId = R.drawable.ic_magnifying_glass_64,
            title = stringResource(R.string.nothing_found),
            body = stringResource(R.string.write_different_search_query),
            isButtonVisible = false,
        )
        ZarinaErrorScreen(
            state = errorState,
            onButtonClicked = {},
            modifier = modifier,
        )
    }

    @Composable
    private fun SearchAutocompleteSuggestions(
        suggestions: ImmutableList<ProductSearchSuggestions.AutocompleteSuggestion>,
        onSuggestionClicked: (ProductSearchSuggestions.AutocompleteSuggestion) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val listState = rememberLazyListState()
        LaunchedEffect(suggestions) {
            listState.scrollToItem(0)
        }

        @Suppress("NAME_SHADOWING")
        AnimatedContent(
            targetState = suggestions,
            transitionSpec = {
                val enter = expandVertically() + fadeIn()
                val exit = shrinkVertically() + fadeOut()
                (enter togetherWith exit).using(SizeTransform(clip = false))
            },
            contentAlignment = Alignment.TopCenter,
            contentKey = { it.isNotEmpty() },
            label = "SearchAutocompleteSuggestions",
            modifier = modifier,
        ) { suggestions ->
            if (suggestions.isNotEmpty()) {
                LazyRow(
                    state = listState,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    items(
                        items = suggestions,
                        key = { it.text },
                    ) { suggestion ->
                        ZarinaTag(
                            onClick = { onSuggestionClicked(suggestion) },
                            modifier = Modifier.animateItem(),
                        ) {
                            Text(text = suggestion.text.capitalize())
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SearchSuggestionList(
        query: String,
        items: ImmutableList<SearchSuggestionItem>,
        onItemClicked: (SearchSuggestionItem) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = modifier,
        ) {
            itemsIndexed(
                items = items,
                key = { _, item -> getSearchSuggestionItemKey(item) },
                contentType = { _, item -> getSearchSuggestionItemContentType(item) },
            ) { index, item ->
                when (item) {
                    is SearchSuggestionItem.GenericTitle -> {
                        SearchSuggestionGenericTitle(
                            item = item,
                            modifier = Modifier.animateItem(),
                        )
                    }

                    is SearchSuggestionItem.SearchQueryItem -> {
                        val nextItem = items.getOrNull(index + 1)
                        val isDividerVisible =
                            index < items.lastIndex && nextItem is SearchSuggestionItem.SearchQueryItem

                        SearchSuggestionSearchQueryItem(
                            item = item,
                            onClick = onItemClicked,
                            query = query,
                            isDividerVisible = isDividerVisible,
                            modifier = Modifier.animateItem(),
                        )
                    }

                    is SearchSuggestionItem.CategoryItem -> {
                        val nextItem = items.getOrNull(index + 1)
                        val isDividerVisible =
                            index < items.lastIndex && nextItem is SearchSuggestionItem.CategoryItem

                        SearchSuggestionCategoryItem(
                            item = item,
                            onClick = onItemClicked,
                            query = query,
                            isDividerVisible = isDividerVisible,
                            modifier = Modifier.animateItem(),
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun SearchSuggestionGenericTitle(
        item: SearchSuggestionItem.GenericTitle,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            modifier = modifier
                .padding(top = 8.dp)
                .heightIn(min = 48.dp),
        ) {
            Text(
                text = textString(item.text),
                style = SearchSuggestionTitleTextStyle,
                color = SearchSuggestionsColor,
            )
        }
    }

    @Composable
    private fun SearchSuggestionSearchQueryItem(
        item: SearchSuggestionItem.SearchQueryItem,
        onClick: (SearchSuggestionItem.SearchQueryItem) -> Unit,
        query: String,
        isDividerVisible: Boolean,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaItem(
                onClick = { onClick(item) },
                modifier = Modifier.heightIn(min = 48.dp),
            ) {
                val textWithQueryMatch = rememberSearchSuggestionItemTextWithQueryMatch(
                    text = item.query,
                    query = query,
                )

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_magnifying_glass_24),
                    contentDescription = null,
                    tint = UiKitTheme.colors.icon.regular.default,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = textWithQueryMatch,
                    style = SearchSuggestionItemTextStyle,
                    color = SearchSuggestionsColor,
                )
            }

            if (isDividerVisible) {
                ZarinaDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }

    @Composable
    private fun SearchSuggestionCategoryItem(
        item: SearchSuggestionItem.CategoryItem,
        onClick: (SearchSuggestionItem.CategoryItem) -> Unit,
        query: String,
        isDividerVisible: Boolean,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaItem(
                onClick = { onClick(item) },
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Column {
                    val nameWithQueryMatch = rememberSearchSuggestionItemTextWithQueryMatch(
                        text = item.name,
                        query = query,
                    )

                    Text(
                        text = nameWithQueryMatch,
                        style = SearchSuggestionItemTextStyle,
                        color = SearchSuggestionsColor,
                    )

                    if (item.parentCategoryChain != null) {
                        Text(
                            text = item.parentCategoryChain,
                            style = UiKitTheme.typography.footnote.light,
                            color = UiKitTheme.colors.text.general.regular.muted,
                        )
                    }
                }
            }

            if (isDividerVisible) {
                ZarinaDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }

    @Composable
    private fun rememberSearchSuggestionItemTextWithQueryMatch(
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

    private fun getSearchSuggestionItemKey(item: SearchSuggestionItem): String {
        return when (item) {
            is SearchSuggestionItem.GenericTitle -> {
                "$SearchSuggestionsKeyTitlePrefix ${item.text.hashCode()}"
            }

            is SearchSuggestionItem.SearchQueryItem -> {
                "$SearchSuggestionsKeyQueryItemPrefix ${item.query}"
            }

            is SearchSuggestionItem.CategoryItem -> {
                "$SearchSuggestionsKeyCategoryItemPrefix ${item.id.value}"
            }
        }
    }

    private fun getSearchSuggestionItemContentType(item: SearchSuggestionItem): String {
        return when (item) {
            is SearchSuggestionItem.GenericTitle -> SearchSuggestionsContentTypeTitle
            is SearchSuggestionItem.SearchQueryItem -> SearchSuggestionsContentTypeQueryItem
            is SearchSuggestionItem.CategoryItem -> SearchSuggestionsContentTypeCategoryItem
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

    private val SearchSuggestionsColor: Color
        @Composable
        get() = UiKitTheme.colors.text.general.regular.default

    private const val SearchSuggestionsContentKeySuggestions =
        "SearchSuggestionsContentKeySuggestions"

    private const val SearchSuggestionsKeyTitlePrefix = "SearchSuggestionsKeyTitlePrefix"
    private const val SearchSuggestionsKeyQueryItemPrefix = "SearchSuggestionsKeyQueryItemPrefix"
    private const val SearchSuggestionsKeyCategoryItemPrefix =
        "SearchSuggestionsKeyCategoryItemPrefix"

    private const val SearchSuggestionsContentTypeTitle = "SearchSuggestionsContentTypeTitle"
    private const val SearchSuggestionsContentTypeQueryItem =
        "SearchSuggestionsContentTypeQueryItem"
    private const val SearchSuggestionsContentTypeCategoryItem =
        "SearchSuggestionsContentTypeCategoryItem"
}
