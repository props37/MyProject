package ru.zarina.zarina.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class UiKitColorsReworked(
    val background: Background = Background(),
    val text: Text = Text(),
    val borders: Borders = Borders(),
) {
    @Immutable
    data class Background(
        val button: Button = Button(),
    ) {
        @Immutable
        data class Button(
            val primary: Primary = Primary(),
            val outline: Outline = Outline(),
            val secondary: Secondary = Secondary(),
        ) {
            @Immutable
            data class Primary(
                val default: Color = Colors.MineShaftLight,
                val pressed: Color = Colors.Black,
            )

            @Immutable
            data class Outline(
                val default: Color = Colors.White,
                val pressed: Color = Colors.WildSand,
            )

            @Immutable
            data class Secondary(
                val default: Color = Colors.White,
                val pressed: Color = Colors.WildSand,
            )
        }
    }

    @Immutable
    data class Text(
        val button: Button = Button(),
    ) {
        @Immutable
        data class Button(
            val primary: Primary = Primary(),
            val outline: Outline = Outline(),
            val secondary: Secondary = Secondary(),
        ) {
            @Immutable
            data class Primary(
                val default: Color = Colors.White,
            )

            @Immutable
            data class Outline(
                val default: Color = Colors.MineShaftDark,
            )

            @Immutable
            data class Secondary(
                val default: Color = Colors.MineShaftDark,
            )
        }
    }

    @Immutable
    data class Borders(
        val button: Button = Button(),
    ) {
        @Immutable
        data class Button(
            val default: Color = Colors.MineShaftDark,
        )
    }
}

val LightUiKitColors: UiKitColorsReworked
    get() = UiKitColorsReworked()

val LocalUiKitColorsReworked = staticCompositionLocalOf { LightUiKitColors }
