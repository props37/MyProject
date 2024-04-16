package ru.livetyping.zarina.ui.screen.productcountselector

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.common.component.button.ZarinaCloseIconButton
import ru.livetyping.zarina.ui.common.component.icon.ZarinaCheckmarkAnimatedIcon
import ru.livetyping.zarina.ui.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.ui.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.ui.screen.productcountselector.ProductCountSelectorViewModel.CountItem
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec

object ProductCountSelectorScreenComponents {

    @Composable
    fun TopBar(
        onCloseClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            centerContent = {
                Text(
                    text = stringResource(R.string.select_count),
                    style = UiKitTheme.typography.primary.bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            endContent = {
                ZarinaCloseIconButton(
                    onClick = onCloseClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(end = 2.dp),
                )
            },
            contentPadding = PaddingValues(TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @Composable
    fun CountItem(
        item: CountItem,
        onClick: (item: CountItem) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .heightIn(min = 56.dp)
                .clickable { onClick(item) }
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = item.count.toString(),
                style = UiKitTheme.typography.secondary.light,
                color = UiKitTheme.colors.text.general.regular.default,
            )

            Spacer(modifier = Modifier.weight(1f))

            val state = when {
                item.isLoading -> CountItemState.Loading
                item.isSelected -> CountItemState.Selected
                else -> CountItemState.Default
            }
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    AnimatedContentDefaultTransitionSpec().using(sizeTransform = null)
                },
                contentAlignment = Alignment.Center,
                label = "CountItem icon",
            ) { state ->
                when (state) {
                    CountItemState.Default -> Unit
                    CountItemState.Selected -> {
                        ZarinaCheckmarkAnimatedIcon(
                            isVisible = true,
                            iconSize = 16.dp,
                        )
                    }

                    CountItemState.Loading -> {
                        ZarinaCircularLoader(
                            strokeWidth = 1.dp,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }
        }
    }

    private enum class CountItemState { Default, Selected, Loading }
}
