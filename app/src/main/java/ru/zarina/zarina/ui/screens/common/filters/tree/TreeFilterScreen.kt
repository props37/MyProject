package ru.zarina.zarina.ui.screens.common.filters.tree

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import ru.zarina.zarina.domain.old.TreeFilter
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.base.textString
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.CollapseButton
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.common.components.toolbar.BackButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.components.toolbar.TextButton
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.utils.domain.toColorOr
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.old.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun TreeFilterScreenContent(
    toolbarTitle: Text,
    isClearButtonVisible: Boolean,
    onClearClick: () -> Unit,
    items: PersistentList<TreeFilter.Item>,
    onItemClick: (TreeFilter.Item) -> Unit,
    onBackClick: () -> Unit,
    isApplyButtonVisible: Boolean,
    onApplyClick: () -> Unit,
) {
    val listState = rememberLazyListState()
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = textString(toolbarTitle),
                startIcon = {
                    BackButton(onClick = onBackClick)
                },
                endIcon = {
                    AnimatedVisibility(
                        visible = isClearButtonVisible,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        TextButton(
                            text = stringResource(id = R.string.reset),
                            onClick = onClearClick,
                        )
                    }
                },
                isElevated = listState.canScrollBackward,
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
            ) {
                items(
                    items = items,
                    key = { item -> item.id },
                ) { item ->
                    FilterTreeItem(
                        item = item,
                        onItemClick = onItemClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item { Spacer(modifier = Modifier.navigationBarsPadding()) }
            }
            AnimatedContent(
                targetState = isApplyButtonVisible,
                label = "apply button visibility",
                modifier = Modifier.fillMaxWidth(),
            ) { isVisible ->
                if (isVisible) {
                    val elevation = if (listState.canScrollForward) 6.dp else 0.dp
                    ZarinaTextButton(
                        text = stringResource(id = R.string.apply),
                        onClick = onApplyClick,
                        modifier = Modifier
                            .shadow(elevation)
                            .background(UiKitTheme.colorsOld.screenBackground)
                            .padding(16.dp)
                            .navigationBarsPadding()
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FilterTreeItem(
    item: TreeFilter.Item,
    onItemClick: (TreeFilter.Item) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isCollapsed by remember {
        mutableStateOf(
            item.getFlattenedChildren().none { it.isSelected })
    }
    Column(
        modifier = modifier
    ) {
        val onClick = remember(item) { { onItemClick(item) } }
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                        .border(2.dp, UiKitTheme.colorsOld.colorPickerCircleBorder, CircleShape),
                )
            }
            Text(
                text = item.name,
                style = UiKitTheme.typography.circle1718,
                color = UiKitTheme.colorsOld.primaryContentColor,
                modifier = Modifier
                    .padding(end = 8.dp),
            )
            if (item.count != null)
                Text(
                    text = item.count.toString(),
                    style = UiKitTheme.typography.circle1718,
                    color = UiKitTheme.colorsOld.hint,
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
                        tint = UiKitTheme.colorsOld.primaryContentColor,
                        modifier = Modifier.padding(end = 8.dp),
                    )
            }
            if (item.children.isNotEmpty()) {
                CollapseButton(
                    isCollapsed = isCollapsed,
                    modifier = Modifier.clickable { isCollapsed = !isCollapsed })
            }
        }
        Divider(
            thickness = 1.dp,
            color = UiKitTheme.colorsOld.listDivider,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        if (!isCollapsed) {
            item.children.forEach { child ->
                FilterTreeItem(
                    item = child,
                    onItemClick = onItemClick,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
fun TreeFilterScreen(
    filtersSavedStateHandle: SavedStateHandle,
    goBack: () -> Unit,
) {
    val viewModel = koinViewModel<TreeFilterViewModel> { parametersOf(filtersSavedStateHandle) }

    val toolbarTitle by viewModel.toolbarTitle.collectAsState()
    val isClearButtonVisible by viewModel.isClearButtonVisible.collectAsState()
    val items by viewModel.items.collectAsState()
    val filterButtonMode by viewModel.isApplyButtonVisible.collectAsState()

    TreeFilterScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
    )

    TreeFilterScreenContent(
        toolbarTitle = toolbarTitle,
        isClearButtonVisible = isClearButtonVisible,
        onClearClick = viewModel::onClearClick,
        items = items,
        onItemClick = viewModel::onItemClick,
        onBackClick = viewModel::onBackClick,
        isApplyButtonVisible = filterButtonMode,
        onApplyClick = viewModel::onApplyClick,
    )
}

@Composable
fun TreeFilterScreenBehavior(
    sideEffects: Flow<TreeFilterViewModel.SideEffect>,
    goBack: () -> Unit,
) {
    NavigationBarState(isVisible = false, isAnimated = false)
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                TreeFilterViewModel.SideEffect.GoBack -> goBack()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun TreeFilterScreenContentPreview() {
    ZarinaTheme {
        TreeFilterScreenContent(
            toolbarTitle = Text.String(text = "Цвет"),
            isClearButtonVisible = true,
            onClearClick = {},
            items = persistentListOf(),
            onItemClick = {},
            onBackClick = {},
            isApplyButtonVisible = true,
            onApplyClick = {},
        )
    }
}

