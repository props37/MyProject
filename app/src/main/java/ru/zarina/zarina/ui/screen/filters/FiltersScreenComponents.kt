package ru.zarina.zarina.ui.screen.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.filter.Filter
import ru.zarina.zarina.domain.rework.filter.ListFilterItem
import ru.zarina.zarina.domain.rework.filter.SortFilterItem
import ru.zarina.zarina.domain.rework.filter.sorting
import ru.zarina.zarina.ui.common.component.ZarinaSwitch
import ru.zarina.zarina.ui.common.component.button.BackIconButton
import ru.zarina.zarina.ui.common.component.topbar.TopBarDefaults
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.common.util.domain.nameResId
import ru.zarina.zarina.ui.theme.UiKitTheme

object FiltersScreenComponents {

    @Composable
    fun TopBar(
        isResetButtonVisible: Boolean,
        actions: TopBarActions,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                BackIconButton(
                    onClick = actions.onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = stringResource(R.string.filters),
                    style = UiKitTheme.typographyReworked.primary.regular,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            endContent = {
                // TODO: [High] Implement
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @Composable
    fun SingleSelectionFilterItem(
        type: Filter.Type,
        selected: ListFilterItem?,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = FilterItemMinHeight)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = stringResource(type.nameResId),
                style = FilterTitleTextStyle,
                color = FilterTitleColor,
            )

            Spacer(modifier = Modifier.width(8.dp))

            val selectedText = when (selected) {
                is SortFilterItem -> stringResource(selected.sorting.nameResId)
                else -> ""
            }
            Text(
                text = selectedText,
                style = UiKitTheme.typographyReworked.secondary.light,
                color = UiKitTheme.colorsReworked.text.general.regular.muted,
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))

            FilterItemEndArrowIcon()
        }
    }

    @Composable
    fun MultiSelectionFilterItem(
        type: Filter.Type,
        selectedCount: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = FilterItemMinHeight)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = stringResource(type.nameResId),
                style = FilterTitleTextStyle,
                color = FilterTitleColor,
            )

            Spacer(modifier = Modifier.width(8.dp))

            // TODO: [High] Improve
            if (selectedCount > 0) {
                Text(
                    text = selectedCount.toString(),
                    style = UiKitTheme.typographyReworked.footnote.bold,
                    color = UiKitTheme.colorsReworked.text.general.inversed.default,
                    modifier = Modifier
                        .background(
                            color = UiKitTheme.colorsReworked.background.general.inversed.default,
                            shape = CircleShape,
                        )
                        .padding(horizontal = 8.dp)
                        .padding(top = 1.dp),
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))

            FilterItemEndArrowIcon()
        }
    }

    @Composable
    fun ToggleFilterItem(
        type: Filter.Type,
        isChecked: Boolean,
        onCheckedChanged: (Boolean) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = FilterItemMinHeight)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = stringResource(type.nameResId),
                style = FilterTitleTextStyle,
                color = FilterTitleColor,
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))

            ZarinaSwitch(
                isChecked = isChecked,
                onCheckedChanged = onCheckedChanged,
            )
        }
    }

    @Composable
    private fun FilterItemEndArrowIcon(
        modifier: Modifier = Modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_small_arrow_up_24),
            contentDescription = null,
            modifier = modifier
                .size(16.dp)
                .rotate(degrees = 90f),
        )
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

    private val FilterItemMinHeight: Dp get() = 56.dp

    private val FilterTitleTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typographyReworked.secondary.light

    private val FilterTitleColor: Color
        @Composable
        get() = UiKitTheme.colorsReworked.text.general.regular.default
}
