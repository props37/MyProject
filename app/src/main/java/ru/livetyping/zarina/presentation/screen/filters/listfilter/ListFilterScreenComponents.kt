package ru.livetyping.zarina.presentation.screen.filters.listfilter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.filter.ColorFilterItem
import ru.livetyping.zarina.domain.filter.ListFilterItem
import ru.livetyping.zarina.domain.filter.SortFilterItem
import ru.livetyping.zarina.domain.filter.sorting
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.common.component.color.ZarinaColorIcon
import ru.livetyping.zarina.presentation.common.component.icon.ZarinaCheckmarkAnimatedIcon
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.util.domain.nameResId
import ru.livetyping.zarina.presentation.common.util.domain.toComposeColor
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultExitTransition

object ListFilterScreenComponents {

    @Composable
    fun TopBar(
        title: String,
        isResetButtonVisible: Boolean,
        actions: TopBarActions,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = actions.onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            endContent = {
                AnimatedVisibility(
                    visible = isResetButtonVisible,
                    enter = AnimatedContentDefaultEnterTransition,
                    exit = AnimatedContentDefaultExitTransition,
                ) {
                    ZarinaButton(
                        onClick = actions.onResetClicked,
                        size = ZarinaButtonSize.Small,
                        colors = ZarinaButtonDefaults.backlessColors(),
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.reset).uppercase(),
                            style = UiKitTheme.typography.caption1.regular,
                        )
                    }
                }
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @Composable
    fun FilterItems(
        items: List<ListFilterItem>,
        onItemClicked: (ListFilterItem) -> Unit,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(),
    ) {
        LazyColumn(
            contentPadding = contentPadding,
            modifier = modifier,
        ) {
            itemsIndexed(
                items = items,
                key = { _, item -> item.id.value },
            ) { index, item ->
                FilterItem(
                    item = item,
                    onItemClicked = onItemClicked,
                )

                if (index < items.size - 1) {
                    Divider(
                        color = UiKitTheme.colors.border.general.default,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }

    @Composable
    fun ApplyButton(
        onClick: () -> Unit,
        isVisible: Boolean,
        modifier: Modifier = Modifier,
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = AnimatedContentDefaultEnterTransition,
            exit = AnimatedContentDefaultExitTransition,
            modifier = modifier,
        ) {
            Column {
                Divider(
                    color = UiKitTheme.colors.border.general.default,
                    modifier = Modifier.fillMaxWidth(),
                )

                ZarinaButton(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(text = stringResource(R.string.apply).uppercase())
                }
            }
        }
    }

    @Composable
    private fun FilterItem(
        item: ListFilterItem,
        onItemClicked: (ListFilterItem) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .clickable { onItemClicked(item) }
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            if (item is ColorFilterItem) {
                ZarinaColorIcon(
                    color = item.color.toComposeColor() ?: Color.Unspecified,
                    size = 16.dp,
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            val name = if (item is SortFilterItem) {
                stringResource(item.sorting.nameResId)
            } else {
                item.name
            }

            Text(
                text = name,
                style = UiKitTheme.typography.secondary.light,
                color = UiKitTheme.colors.text.general.regular.default,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))

            ZarinaCheckmarkAnimatedIcon(
                isVisible = item.isSelected,
                iconSize = 16.dp,
                modifier = Modifier.padding(start = if (item.isSelected) 16.dp else 0.dp),
            )
        }
    }

    @Stable
    class TopBarActions(
        val onBackClicked: () -> Unit,
        val onResetClicked: () -> Unit,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TopBarActions

            if (onBackClicked != other.onBackClicked) return false
            return onResetClicked == other.onResetClicked
        }

        override fun hashCode(): Int {
            var result = onBackClicked.hashCode()
            result = 31 * result + onResetClicked.hashCode()
            return result
        }
    }
}
