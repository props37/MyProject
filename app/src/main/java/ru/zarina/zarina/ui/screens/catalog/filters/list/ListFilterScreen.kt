package ru.zarina.zarina.ui.screens.catalog.filters.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.ListFilter
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.utils.domain.toColorOr
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun ListFilterScreenContent(
    items: PersistentList<ListFilter.Item>,
    onItemClick: (ListFilter.Item) -> Unit,
) {
    // TODO toolbar title
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        items.forEach { item ->
            FilterListItem(
                item = item,
                onClick = { onItemClick(item) },
                modifier = Modifier.fillMaxWidth()
            )
            // TODO separator
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FilterListItem(
    item: ListFilter.Item,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .height(IntrinsicSize.Min)
            .padding(16.dp),
    ) {
        if (item.color != null) {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(shape = CircleShape)
                    .background(color = item.color.toColorOr(Color.Transparent))
                    .border(2.dp, UiKitTheme.colors.colorPickerCircleBorder, CircleShape),
            )
        }
        Text(
            text = item.name,
            style = UiKitTheme.typography.circle1718,
            color = UiKitTheme.colors.primaryContentColor,
            modifier = Modifier.padding(end = 8.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        AnimatedContent(
            targetState = item.isSelected,
            label = "${item.id} is selected",
        ) {
            if (it)
                Icon(
                    painter = painterResource(id = R.drawable.ic_checkmark_24),
                    contentDescription = stringResource(id = R.string.selected),
                    tint = UiKitTheme.colors.primaryContentColor,
                )
        }
    }
}

@Composable
fun ListFilterScreen(
    filtersSavedStateHandle: SavedStateHandle,
) {
    val viewModel = koinViewModel<ListFilterViewModel> { parametersOf(filtersSavedStateHandle) }

    val items by viewModel.items.collectAsState()

    ListFilterScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    ListFilterScreenContent(
        items = items,
        onItemClick = viewModel::onItemClick,
    )
}

@Composable
fun ListFilterScreenBehavior(
    sideEffects: Flow<ListFilterViewModel.SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun ListFilterScreenContentPreview() {
    ZarinaTheme {
        ListFilterScreenContent(
            items = persistentListOf(),
            onItemClick = {},
        )
    }
}

