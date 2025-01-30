package ru.livetyping.zarina.presentation.screen.sizeselector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.bottomsheet.ZarinaBottomSheet
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.common.component.button.ZarinaCloseIconButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.screen.sizeselector.SizeSelectorViewModel.Size
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.kotlin.capitalize

object SizeSelectorScreenComponents {

    @Composable
    fun SizeSelectorScaffold(
        onClickOutside: () -> Unit,
        modifier: Modifier = Modifier,
        windowInsets: WindowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout),
        content: @Composable () -> Unit,
    ) {
        Column(
            modifier = modifier
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = onClickOutside,
                )
                .windowInsetsPadding(windowInsets),
        ) {
            ZarinaBottomSheet(
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {},
                ),
                content = content,
            )
        }
    }

    @Composable
    fun SizeTableLabel(modifier: Modifier = Modifier) {
        ZarinaButton(
            onClick = {},
            isEnabled = false,
            size = ZarinaButtonSize.Medium,
            colors = ZarinaButtonDefaults.secondaryColors(
                disabledBackgroundColor = UiKitTheme.colors.background.button.secondary.default,
                disabledContentColor = UiKitTheme.colors.text.button.secondary.default,
            ),
            modifier = modifier,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_ruler_24),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.size_table).uppercase(),
                modifier = Modifier.padding(top = 2.dp), // Circe font padding
            )
        }
    }

    @Composable
    fun TopBar(
        onCloseClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            centerContent = {
                Text(
                    text = stringResource(R.string.choose_size),
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
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    fun Sizes(
        sizes: ImmutableList<Size>,
        onSizeClicked: (Size) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            sizes.forEachIndexed { index, size ->
                key(size.id) {
                    Size(
                        size = size,
                        onClick = { onSizeClicked(size) },
                    )

                    if (index < sizes.lastIndex) {
                        ZarinaDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )
                    }
                }
            }

            NavigationBarSpacer()
        }
    }

    @Composable
    private fun Size(
        size: Size,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            val sizeColor = if (size.isAvailable) {
                UiKitTheme.colors.text.general.regular.default
            } else {
                UiKitTheme.colors.text.general.regular.disabled
            }

            Text(
                text = size.size.capitalize(),
                style = UiKitTheme.typography.secondary.light,
                color = sizeColor,
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))

            when {
                size.availableHeights.isNotEmpty() -> {
                    val heightsText = remember(size.availableHeights) {
                        size.availableHeights.reduce { acc, height -> "$acc, $height" }
                    }
                    Text(
                        text = stringResource(R.string.available_height_cm, heightsText),
                        style = UiKitTheme.typography.secondary.light,
                        color = UiKitTheme.colors.text.general.regular.muted,
                    )
                }

                size.isAvailable -> Unit

                else -> {
                    Text(
                        text = stringResource(R.string.subscribe).uppercase(),
                        style = UiKitTheme.typography.caption1.regular,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )
                }
            }
        }
    }

    @Composable
    fun NavigationBarSpacer(
        modifier: Modifier = Modifier,
    ) {
        Spacer(
            modifier = modifier
                .height(
                    WindowInsets.navigationBars
                        .union(WindowInsets.displayCutout)
                        .only(WindowInsetsSides.Bottom)
                        .asPaddingValues()
                        .calculateBottomPadding()
                )
        )
    }
}
