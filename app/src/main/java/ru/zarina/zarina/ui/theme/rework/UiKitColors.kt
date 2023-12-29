package ru.zarina.zarina.ui.theme.rework

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// TODO: [Low] Use single class to represent different buttons, texts etc?

data class UiKitColorsReworked(
    val background: Background = Background(),
    val text: Text = Text(),
    val icon: Icon = Icon(),
    val border: Border = Border(),
) {
    data class Background(
        val general: General = General(),
        val button: Button = Button(),
    ) {
        data class General(
            val regular: Regular = Regular(),
            val inverse: Inverse = Inverse(),
        ) {
            data class Regular(
                val background: Color = Colors.White,
                val muted: Color = Colors.AltoDark,
            )

            data class Inverse(
                val inverse: Color = Colors.MineShaftDark,
            )
        }

        data class Button(
            val primary: Primary = Primary(),
            val secondary: Secondary = Secondary(),
            val tertiary: Tertiary = Tertiary(),
            val outline: Outline = Outline(),
        ) {
            data class Primary(
                val default: Color = Colors.MineShaftLight,
                val active: Color = Colors.MineShaftDark,
                val disabled: Color = Colors.Scorpion,
            )

            data class Secondary(
                val default: Color = Colors.White,
                val active: Color = Colors.Gallery,
                val disabled: Color = Colors.White,
            )

            data class Tertiary(
                val default: Color = Colors.WildSand,
                val active: Color = Colors.AltoLight,
                val disabled: Color = Colors.WildSand,
            )

            data class Outline(
                val default: Color = Colors.White,
                val active: Color = Colors.Gallery,
                val disabled: Color = Colors.White,
            )
        }
    }

    data class Text(
        val general: General = General(),
        val button: Button = Button(),
    ) {
        data class General(
            val regular: Regular = Regular(),
            val inversed: Inversed = Inversed(),
        ) {
            data class Regular(
                val default: Color = Colors.MineShaftDark,
                val disabled: Color = Colors.SilverChalice,
                val muted: Color = Colors.Boulder,
            )

            data class Inversed(
                val default: Color = Colors.White,
            )
        }

        data class Button(
            val primary: Primary = Primary(),
            val secondary: Secondary = Secondary(),
            val tertiary: Tertiary = Tertiary(),
            val outline: Outline = Outline(),
            val backless: Backless = Backless(),
        ) {
            data class Primary(
                val default: Color = Colors.White,
                val disabled: Color = Colors.Gray,
            )

            data class Secondary(
                val default: Color = Colors.MineShaftDark,
                val disabled: Color = Colors.SilverChalice,
            )

            data class Tertiary(
                val default: Color = Colors.MineShaftDark,
                val disabled: Color = Colors.SilverChalice,
            )

            data class Outline(
                val default: Color = Colors.MineShaftDark,
                val disabled: Color = Colors.SilverChalice,
            )

            data class Backless(
                val default: Color = Colors.MineShaftDark,
                val disabled: Color = Colors.SilverChalice,
            )
        }
    }

    data class Icon(
        val regular: Regular = Regular(),
    ) {
        data class Regular(
            val default: Color = Colors.MineShaftDark,
            val muted: Color = Colors.Boulder,
            val disabled: Color = Colors.SilverChalice,
        )
    }

    data class Border(
        val general: General = General(),
        val button: Button = Button(),
    ) {
        data class General(
            val default: Color = Colors.Gallery,
            val active: Color = Colors.MineShaftDark,
            val disabled: Color = Colors.SilverChalice,
        )

        data class Button(
            val default: Color = Colors.MineShaftDark,
            val disabled: Color = Colors.SilverChalice,
        )
    }
}

val LightUiKitColors: UiKitColorsReworked
    get() = UiKitColorsReworked()

val LocalUiKitColorsReworked = staticCompositionLocalOf { LightUiKitColors }
