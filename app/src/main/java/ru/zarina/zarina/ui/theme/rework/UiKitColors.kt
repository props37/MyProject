package ru.zarina.zarina.ui.theme.rework

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// TODO: [Low] Use single class to represent different buttons, texts etc?

@Immutable
data class UiKitColorsReworked(
    val background: Background = Background(),
    val text: Text = Text(),
    val icon: Icon = Icon(),
    val border: Border = Border(),
) {
    @Immutable
    data class Background(
        val general: General = General(),
        val button: Button = Button(),
    ) {
        @Immutable
        data class General(
            val regular: Regular = Regular(),
            val inverse: Inverse = Inverse(),
        ) {
            @Immutable
            data class Regular(
                val background: Color = Colors.White,
                val muted: Color = Colors.AltoDark,
            )

            @Immutable
            data class Inverse(
                val inverse: Color = Colors.MineShaftDark,
            )
        }

        @Immutable
        data class Button(
            val primary: Primary = Primary(),
            val secondary: Secondary = Secondary(),
            val tertiary: Tertiary = Tertiary(),
            val outline: Outline = Outline(),
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
                val active: Color = Colors.AltoLight,
                val disabled: Color = Colors.WildSand,
            )

            @Immutable
            data class Outline(
                val default: Color = Colors.White,
                val active: Color = Colors.Gallery,
                val disabled: Color = Colors.White,
            )
        }
    }

    @Immutable
    data class Text(
        val general: General = General(),
        val button: Button = Button(),
    ) {
        @Immutable
        data class General(
            val regular: Regular = Regular(),
            val inversed: Inversed = Inversed(),
        ) {
            @Immutable
            data class Regular(
                val default: Color = Colors.MineShaftDark,
                val disabled: Color = Colors.SilverChalice,
            )

            @Immutable
            data class Inversed(
                val default: Color = Colors.White,
            )
        }

        @Immutable
        data class Button(
            val primary: Primary = Primary(),
            val secondary: Secondary = Secondary(),
            val tertiary: Tertiary = Tertiary(),
            val outline: Outline = Outline(),
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

            @Immutable
            data class Outline(
                val default: Color = Colors.MineShaftDark,
                val disabled: Color = Colors.SilverChalice,
            )
        }
    }

    @Immutable
    data class Icon(
        val regular: Regular = Regular(),
    ) {
        @Immutable
        data class Regular(
            val default: Color = Colors.MineShaftDark,
        )
    }

    @Immutable
    data class Border(
        val general: General = General(),
        val button: Button = Button(),
    ) {
        @Immutable
        data class General(
            val default: Color = Colors.Gallery,
            val active: Color = Colors.MineShaftDark,
            val disabled: Color = Colors.SilverChalice,
        )

        @Immutable
        data class Button(
            val default: Color = Colors.MineShaftDark,
            val disabled: Color = Colors.SilverChalice,
        )
    }
}

val LightUiKitColors: UiKitColorsReworked
    get() = UiKitColorsReworked()

val LocalUiKitColorsReworked = staticCompositionLocalOf { LightUiKitColors }
