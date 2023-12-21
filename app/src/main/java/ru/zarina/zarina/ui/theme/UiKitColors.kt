package ru.zarina.zarina.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// TODO: [Low] Use single class to represent different buttons, texts etc?

@Immutable
data class UiKitColorsReworked(
    val background: Background = Background(),
    val text: Text = Text(),
    val borders: Borders = Borders(),
) {
    @Immutable
    data class Background(
        val general: General = General(),
        val button: Button = Button(),
    ) {
        @Immutable
        data class General(
            val regular: Regular = Regular(),
        ) {
            @Immutable
            data class Regular(
                val background: Color = Colors.White,
            )
        }

        @Immutable
        data class Button(
            val primary: Primary = Primary(),
            val secondary: Secondary = Secondary(),
            val tertiary: Tertiary = Tertiary(),
        ) {
            @Immutable
            data class Primary(
                val default: Color = Colors.MineShaftLight,
                val active: Color = Colors.MineShaftDark,
                val disabled: Color = Colors.Scorpion,
            )

            @Immutable
            data class Secondary(
                val default: Color = Colors.White,
                val active: Color = Colors.Gallery,
                val disabled: Color = Colors.White,
            )

            @Immutable
            data class Tertiary(
                val default: Color = Colors.WildSand,
                val active: Color = Colors.Alto,
                val disabled: Color = Colors.WildSand,
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
            val secondary: Secondary = Secondary(),
            val tertiary: Tertiary = Tertiary(),
        ) {
            @Immutable
            data class Primary(
                val default: Color = Colors.White,
                val disabled: Color = Colors.Gray,
            )

            @Immutable
            data class Secondary(
                val default: Color = Colors.MineShaftDark,
                val disabled: Color = Colors.SilverChalice,
            )

            @Immutable
            data class Tertiary(
                val default: Color = Colors.MineShaftDark,
                val disabled: Color = Colors.SilverChalice,
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
