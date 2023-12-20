package ru.zarina.zarina.ui.common.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaThemeReworked

@Composable
fun ZarinaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    size: ZarinaButtonSize = ZarinaButtonSize.Large,
    colors: ZarinaButtonColors = ZarinaButtonDefaults.primaryColors(),
    shape: Shape = ZarinaButtonDefaults.Shape,
    contentPadding: PaddingValues = ZarinaButtonDefaults.contentPaddingFromSize(size),
    textStyle: TextStyle = ZarinaButtonDefaults.textStyleFromSize(size),
    content: @Composable RowScope.() -> Unit,
) {
    val minHeight = when (size) {
        ZarinaButtonSize.Large -> ZarinaButtonDefaults.HeightLarge
        ZarinaButtonSize.Medium -> ZarinaButtonDefaults.HeightMedium
        ZarinaButtonSize.Small -> ZarinaButtonDefaults.HeightSmall
    }

    val backgroundColor = animateColorAsState(
        targetValue = if (isEnabled) colors.backgroundColor else colors.disabledBackgroundColor,
        label = "$Tag background color",
    )
    val contentColor = animateColorAsState(
        targetValue = if (isEnabled) colors.contentColor else colors.disabledContentColor,
        label = "$Tag content color",
    )

    CompositionLocalProvider(LocalContentColor provides contentColor.value) {
        ProvideTextStyle(textStyle) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .defaultMinSize(minHeight = minHeight)
                    .clip(shape)
                    .drawBehind { drawRect(backgroundColor.value) }
                    .clickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current,
                        enabled = isEnabled,
                        role = Role.Button,
                        onClick = onClick,
                    )
                    .padding(contentPadding),
                content = content,
            )
        }
    }
}

@Immutable
data class ZarinaButtonColors(
    val backgroundColor: Color,
    val contentColor: Color,
    val disabledBackgroundColor: Color,
    val disabledContentColor: Color,
)

@Immutable
enum class ZarinaButtonSize { Large, Medium, Small }

object ZarinaButtonDefaults {
    val Shape = RoundedCornerShape(2.dp)

    val HeightLarge = 56.dp
    val HeightMedium = 48.dp
    val HeightSmall = 36.dp

    val ContentPaddingLarge = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    val ContentPaddingMedium = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    val ContentPaddingSmall = PaddingValues(horizontal = 8.dp, vertical = 8.dp)

    val IconSizeLarge = 20.dp
    val IconSizeMedium = 16.dp
    val IconSizeSmall = 12.dp

    @Composable
    fun primaryColors(
        backgroundColor: Color = UiKitTheme.colorsReworked.background.button.primary.default,
        contentColor: Color = UiKitTheme.colorsReworked.text.button.primary.default,
        disabledBackgroundColor: Color = UiKitTheme.colorsReworked.background.button.primary.disabled,
        disabledContentColor: Color = UiKitTheme.colorsReworked.text.button.primary.disabled,
    ): ZarinaButtonColors = ZarinaButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun secondaryColors(
        backgroundColor: Color = UiKitTheme.colorsReworked.background.button.secondary.default,
        contentColor: Color = UiKitTheme.colorsReworked.text.button.secondary.default,
        disabledBackgroundColor: Color = UiKitTheme.colorsReworked.background.button.secondary.disabled,
        disabledContentColor: Color = UiKitTheme.colorsReworked.text.button.secondary.disabled,
    ): ZarinaButtonColors = ZarinaButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun tertiaryColors(
        backgroundColor: Color = UiKitTheme.colorsReworked.background.button.tertiary.default,
        contentColor: Color = UiKitTheme.colorsReworked.text.button.tertiary.default,
        disabledBackgroundColor: Color = UiKitTheme.colorsReworked.background.button.tertiary.disabled,
        disabledContentColor: Color = UiKitTheme.colorsReworked.text.button.tertiary.disabled,
    ): ZarinaButtonColors = ZarinaButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor,
    )

    @Stable
    fun contentPaddingFromSize(size: ZarinaButtonSize): PaddingValues = when (size) {
        ZarinaButtonSize.Large -> ContentPaddingLarge
        ZarinaButtonSize.Medium -> ContentPaddingMedium
        ZarinaButtonSize.Small -> ContentPaddingSmall
    }

    @Composable
    fun textStyleFromSize(size: ZarinaButtonSize): TextStyle = when (size) {
        ZarinaButtonSize.Large -> UiKitTheme.typographyReworked.tertiaryText.regular
        ZarinaButtonSize.Medium -> UiKitTheme.typographyReworked.caption1.regular
        ZarinaButtonSize.Small -> UiKitTheme.typographyReworked.caption3.regular
    }
}

@Preview
@Composable
private fun Primary() {
    ZarinaThemeReworked {
        Column(verticalArrangement = Arrangement.Center) {
            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Large,
                colors = ZarinaButtonDefaults.primaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "PRIMARY LARGE")
            }

            ZarinaButton(
                onClick = {},
                isEnabled = false,
                size = ZarinaButtonSize.Medium,
                colors = ZarinaButtonDefaults.primaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "PRIMARY MEDIUM DISABLED")
            }

            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Small,
                colors = ZarinaButtonDefaults.primaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "PRIMARY SMALL")
            }
        }
    }
}

@Preview
@Composable
private fun Secondary() {
    ZarinaThemeReworked {
        Column(verticalArrangement = Arrangement.Center) {
            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Large,
                colors = ZarinaButtonDefaults.secondaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "SECONDARY LARGE")
            }

            ZarinaButton(
                onClick = {},
                isEnabled = false,
                size = ZarinaButtonSize.Medium,
                colors = ZarinaButtonDefaults.secondaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "SECONDARY MEDIUM DISABLED")
            }

            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Small,
                colors = ZarinaButtonDefaults.secondaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "SECONDARY SMALL")
            }
        }
    }
}

@Preview
@Composable
private fun Tertiary() {
    ZarinaThemeReworked {
        Column(verticalArrangement = Arrangement.Center) {
            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Large,
                colors = ZarinaButtonDefaults.tertiaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "TERTIARY LARGE")
            }

            ZarinaButton(
                onClick = {},
                isEnabled = false,
                size = ZarinaButtonSize.Medium,
                colors = ZarinaButtonDefaults.tertiaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "TERTIARY MEDIUM DISABLED")
            }

            ZarinaButton(
                onClick = {},
                size = ZarinaButtonSize.Small,
                colors = ZarinaButtonDefaults.tertiaryColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = "TERTIARY SMALL")
            }
        }
    }
}

private const val Tag = "ZarinaButton"
