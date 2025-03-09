package ru.livetyping.zarina.feature.search.ui.impl.impl.search.component

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.core.kotlinutil.findSubstringBounds
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicompose.textString
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.uikit.button.ZarinaIconButton
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.model.SearchState
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.model.SearchSuggestionItem
import ru.livetyping.zarina.core.resource.R as RCommon

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SearchSuggestionsSuccess(
    state: SearchState.SuggestionState.Success,
    query: String,
    onSuggestionItemClicked: (SearchSuggestionItem) -> Unit,
    onClearSearchHistoryClicked: () -> Unit,
    onDeleteHistoryQueryItemClicked: (SearchSuggestionItem.HistoryQueryItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = state.items

    val lazyListState = rememberLazyListState()
    DisposableEffect(items) {
        lazyListState.requestScrollToItem(0)
        onDispose {}
    }

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(bottom = ZarinaScrollableDefaults.ScrollableBottomPadding),
        modifier = modifier,
    ) {
        itemsIndexed(
            items = items,
            key = { _, item -> getSuggestionItemKey(item) },
            contentType = { _, item -> getSuggestionItemContentType(item) },
        ) { index, item ->
            when (item) {
                is SearchSuggestionItem.GenericTitle -> {
                    SuggestionTitle(
                        text = textString(item.text),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .animateZarinaItem(this),
                    )
                }

                SearchSuggestionItem.SearchHistoryTitle -> {
                    SuggestionTitle(
                        text = stringResource(RCommon.string.res_search_history),
                        trailingContent = {
                            ZarinaButton(
                                onClick = onClearSearchHistoryClicked,
                                size = ZarinaButtonSize.Medium,
                                colors = ZarinaButtonDefaults.backlessColors(),
                                modifier = Modifier.heightIn(min = 32.dp),
                            ) {
                                Text(text = stringResource(RCommon.string.res_clear).uppercase())
                            }
                        },
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            top = 8.dp,
                            end = 8.dp,
                            bottom = 8.dp,
                        ),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .animateZarinaItem(this),
                    )
                }

                is SearchSuggestionItem.QuerySuggestionItem -> {
                    val nextItem = items.getOrNull(index + 1)
                    val isDividerVisible =
                        index < items.lastIndex && nextItem is SearchSuggestionItem.QuerySuggestionItem

                    SuggestionItem(
                        itemQuery = item.query,
                        onClick = { onSuggestionItemClicked(item) },
                        searchQuery = query,
                        leadingIconResId = RCommon.drawable.ic_magnifying_glass_24,
                        isDividerVisible = isDividerVisible,
                        modifier = Modifier.animateZarinaItem(this),
                    )
                }

                is SearchSuggestionItem.HistoryQueryItem -> {
                    val nextItem = items.getOrNull(index + 1)
                    val isDividerVisible =
                        index < items.lastIndex && nextItem is SearchSuggestionItem.HistoryQueryItem

                    SuggestionItem(
                        itemQuery = item.query,
                        onClick = { onSuggestionItemClicked(item) },
                        searchQuery = query,
                        leadingIconResId = RCommon.drawable.ic_history_24,
                        trailingContent = {
                            CompositionLocalProvider(
                                LocalMinimumInteractiveComponentEnforcement provides false,
                            ) {
                                val iconSize = 16.dp
                                ZarinaIconButton(
                                    onClick = { onDeleteHistoryQueryItemClicked(item) },
                                    indication = ripple(bounded = false, radius = iconSize),
                                    modifier = Modifier.size(32.dp),
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(RCommon.drawable.ic_cross_24),
                                        contentDescription = stringResource(RCommon.string.res_delete),
                                        modifier = Modifier.size(iconSize),
                                    )
                                }
                            }
                        },
                        isDividerVisible = isDividerVisible,
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            top = 8.dp,
                            end = 8.dp,
                            bottom = 8.dp,
                        ),
                        modifier = Modifier.animateZarinaItem(this),
                    )
                }

                is SearchSuggestionItem.CategoryItem -> {
                    val nextItem = items.getOrNull(index + 1)
                    val isDividerVisible =
                        index < items.lastIndex && nextItem is SearchSuggestionItem.CategoryItem

                    SuggestionCategoryItem(
                        item = item,
                        onClick = onSuggestionItemClicked,
                        query = query,
                        isDividerVisible = isDividerVisible,
                        modifier = Modifier.animateZarinaItem(this),
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestionTitle(
    text: String,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    contentPadding: PaddingValues = ItemContentPadding,
) {
    ZarinaItem(
        startContent = {
            Text(
                text = text,
                style = SuggestionTitleTextStyle,
                color = SuggestionItemTextColor,
            )
        },
        endContent = trailingContent,
        contentPadding = contentPadding,
        modifier = modifier.heightIn(min = 48.dp),
    )
}

@Composable
private fun SuggestionItem(
    itemQuery: String,
    onClick: () -> Unit,
    searchQuery: String,
    leadingIconResId: Int,
    isDividerVisible: Boolean,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    contentPadding: PaddingValues = ItemContentPadding,
) {
    Column(modifier = modifier) {
        ZarinaItem(
            onClick = onClick,
            modifier = Modifier.heightIn(min = 48.dp),
            startContent = {
                val textWithQueryMatch = rememberSearchSuggestionItemTextWithQueryMatch(
                    text = itemQuery,
                    query = searchQuery,
                )

                Icon(
                    imageVector = ImageVector.vectorResource(leadingIconResId),
                    contentDescription = null,
                    tint = UiKitTheme.colors.icon.regular.default,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = textWithQueryMatch,
                    style = SuggestionItemTextStyle,
                    color = SuggestionItemTextColor,
                )
            },
            endContent = trailingContent,
            contentPadding = contentPadding,
        )

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
private fun SuggestionCategoryItem(
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
                    style = SuggestionItemTextStyle,
                    color = SuggestionItemTextColor,
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
    val matchTextStyle = SuggestionItemQueryMatchTextStyle
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

private val SuggestionTitleTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.bold

private val SuggestionItemTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.light

private val SuggestionItemTextColor: Color
    @Composable
    get() = UiKitTheme.colors.text.general.regular.default

private val SuggestionItemQueryMatchTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.regular

private val ItemContentPadding: PaddingValues
    get() = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

private fun getSuggestionItemKey(item: SearchSuggestionItem): SuggestionItemKey {
    return when (item) {
        is SearchSuggestionItem.GenericTitle -> SuggestionItemKey.GenericTitle(item.text)
        SearchSuggestionItem.SearchHistoryTitle -> SuggestionItemKey.SearchHistoryTitle
        is SearchSuggestionItem.QuerySuggestionItem -> SuggestionItemKey.QuerySuggestionItem(item.query)
        is SearchSuggestionItem.HistoryQueryItem -> SuggestionItemKey.HistoryQueryItem(item.query)
        is SearchSuggestionItem.CategoryItem -> SuggestionItemKey.CategoryItem(item.id.value)
    }
}

private fun getSuggestionItemContentType(item: SearchSuggestionItem): SuggestionItemContentType {
    return when (item) {
        is SearchSuggestionItem.GenericTitle -> SuggestionItemContentType.GenericTitle
        SearchSuggestionItem.SearchHistoryTitle -> SuggestionItemContentType.SearchHistoryTitle
        is SearchSuggestionItem.QuerySuggestionItem -> SuggestionItemContentType.QuerySuggestionItem
        is SearchSuggestionItem.HistoryQueryItem -> SuggestionItemContentType.HistoryQueryItem
        is SearchSuggestionItem.CategoryItem -> SuggestionItemContentType.CategoryItem
    }
}

@Parcelize
private sealed class SuggestionItemKey : Parcelable {
    data class GenericTitle(val text: Text) : SuggestionItemKey()

    data object SearchHistoryTitle : SuggestionItemKey()

    data class QuerySuggestionItem(val query: String) : SuggestionItemKey()

    data class HistoryQueryItem(val query: String) : SuggestionItemKey()

    data class CategoryItem(val id: String) : SuggestionItemKey()
}

private enum class SuggestionItemContentType {
    GenericTitle,
    SearchHistoryTitle,
    QuerySuggestionItem,
    HistoryQueryItem,
    CategoryItem,
}
