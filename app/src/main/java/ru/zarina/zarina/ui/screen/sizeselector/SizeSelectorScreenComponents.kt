package ru.zarina.zarina.ui.screen.sizeselector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.ZarinaBottomSheet
import ru.zarina.zarina.ui.common.component.button.CloseIconButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.topbar.TopBarDefaults
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorViewModel.Size
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.utils.kotlin.capitalize

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
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClickOutside,
                )
                .windowInsetsPadding(windowInsets),
        ) {
            SizeTableLabel(modifier = Modifier.padding(start = 16.dp))
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaBottomSheet(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
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
                disabledBackgroundColor = UiKitTheme.colorsReworked.background.button.secondary.default,
                disabledContentColor = UiKitTheme.colorsReworked.text.button.secondary.default,
            ),
            modifier = modifier,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_ruler_24),
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
                    style = UiKitTheme.typographyReworked.primary.bold,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                )
            },
            endContent = {
                CloseIconButton(
                    onClick = onCloseClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(end = 2.dp),
                )
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
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

                    if (index < sizes.size - 1) {
                        Divider(
                            color = UiKitTheme.colorsReworked.background.skeleton,
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
                UiKitTheme.colorsReworked.text.general.regular.default
            } else {
                UiKitTheme.colorsReworked.text.general.regular.disabled
            }

            Text(
                text = size.size.capitalize(),
                style = UiKitTheme.typographyReworked.secondary.light,
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
                        text = stringResource(R.string.available_heights_cm, heightsText),
                        style = UiKitTheme.typographyReworked.secondary.light,
                        color = UiKitTheme.colorsReworked.text.general.regular.muted,
                    )
                }

                size.isAvailable -> Unit

                else -> {
                    Text(
                        text = stringResource(R.string.subscribe).uppercase(),
                        style = UiKitTheme.typographyReworked.caption1.regular,
                        color = UiKitTheme.colorsReworked.text.general.regular.default,
                    )
                }
            }
        }
    }

    @Composable
    private fun NavigationBarSpacer(
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
