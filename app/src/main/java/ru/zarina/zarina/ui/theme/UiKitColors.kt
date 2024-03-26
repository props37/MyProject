package ru.zarina.zarina.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class UiKitColors(
    val background: Background = Background(),
    val text: Text = Text(),
    val icon: Icon = Icon(),
    val border: Border = Border(),
) {
    data class Background(
        val general: General = General(),
        val accent: Accent = Accent(),
        val button: Button = Button(),
        val tag: Tag = Tag(),
        val skeleton: Color = Colors.Gallery,
    ) {
        data class General(
            val regular: Generic = Generic(
                default = Colors.White,
                disabled = Colors.SilverChalice,
                muted = Colors.AltoDark,
            ),
            val inversed: Generic = Generic(
                default = Colors.MineShaftDark,
                disabled = Colors.Gray,
                muted = Colors.Scorpion,
            ),
        ) {
            data class Generic(val default: Color, val disabled: Color, val muted: Color)
        }

        data class Accent(
            val pink: Color = Colors.Chardon,
        )

        data class Button(
            val primary: Generic = Generic(
                default = Colors.MineShaftLight,
                active = Colors.MineShaftDark,
                disabled = Colors.Scorpion,
            ),
            val secondary: Generic = Generic(
                default = Colors.White,
                active = Colors.Gallery,
                disabled = Colors.White,
            ),
            val tertiary: Generic = Generic(
                default = Colors.WildSand,
                active = Colors.AltoLight,
                disabled = Colors.WildSand,
            ),
            val outline: Generic = Generic(
                default = Colors.White,
                active = Colors.Gallery,
                disabled = Colors.White,
            ),
        ) {
            data class Generic(val default: Color, val active: Color, val disabled: Color)
        }

        data class Tag(
            val default: Color = Colors.WildSand,
            val active: Color = Colors.MineShaftDark,
        )
    }

    data class Text(
        val general: General = General(),
        val button: Button = Button(),
        val label: Label = Label(),
        val tag: Tag = Tag(),
    ) {
        data class General(
            val regular: Generic = Generic(
                default = Colors.MineShaftDark,
                disabled = Colors.SilverChalice,
                muted = Colors.Boulder,
            ),
            val inversed: Generic = Generic(
                default = Colors.White,
                disabled = Colors.Gray,
                muted = Colors.Silver,
            ),
            val accent: Accent = Accent(),
        ) {
            data class Generic(val default: Color, val disabled: Color, val muted: Color)

            data class Accent(
                val blue: Color = Colors.CuriousBlue,
                val red: Color = Colors.Scarlet,
                val redDisabled: Color = Colors.VividTangerine,
            )
        }

        data class Button(
            val primary: Generic = Generic(
                default = Colors.White,
                disabled = Colors.Gray
            ),
            val secondary: Generic = Generic(
                default = Colors.MineShaftDark,
                disabled = Colors.SilverChalice
            ),
            val tertiary: Generic = Generic(
                default = Colors.MineShaftDark,
                disabled = Colors.SilverChalice,
            ),
            val outline: Generic = Generic(
                default = Colors.MineShaftDark,
                disabled = Colors.SilverChalice,
            ),
            // Also used for Cell buttons from design kit
            val backless: Generic = Generic(
                default = Colors.MineShaftDark,
                disabled = Colors.SilverChalice,
            ),
            val error: Generic = Generic(
                default = Colors.Scarlet,
                disabled = Colors.VividTangerine,
            ),
        ) {
            data class Generic(val default: Color, val disabled: Color)
        }

        data class Label(
            val default: Color = Colors.MineShaftDark,
            val success: Color = Colors.FunGreen,
            val warning: Color = Colors.Flamenco,
            val danger: Color = Colors.Scarlet,
        )

        data class Tag(
            val default: Color = Colors.MineShaftDark,
            val active: Color = Colors.White,
        )
    }

    data class Icon(
        val regular: Regular = Regular(),
        val inversed: Inversed = Inversed(),
    ) {
        data class Regular(
            val default: Color = Colors.MineShaftDark,
            val disabled: Color = Colors.SilverChalice,
            val muted: Color = Colors.Boulder,
            val error: Color = Colors.Scarlet,
            val errorDisabled: Color = Colors.VividTangerine,
        )

        data class Inversed(
            val default: Color = Colors.White,
            val disabled: Color = Colors.Gray,
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
            val error: Color = Colors.Scarlet,
            val errorDisabled: Color = Colors.VividTangerine,
        )

        data class Button(
            val default: Color = Colors.MineShaftDark,
            val disabled: Color = Colors.SilverChalice,
            val errorDisabled: Color = Colors.VividTangerine,
        )
    }
}

val LightUiKitColors: UiKitColors
    get() = UiKitColors()

val LocalUiKitColors = staticCompositionLocalOf { LightUiKitColors }
