package ru.livetyping.zarina.core.ui.kit.theme

import androidx.compose.ui.graphics.Color
import ru.livetyping.zarina.core.ui.kit.impl.theme.Colors

public data class UiKitColors(
    val background: Background = Background(),
    val text: Text = Text(),
    val icon: Icon = Icon(),
    val border: Border = Border(),
) {
    public data class Background(
        val general: General = General(),
        val accent: Accent = Accent(),
        val button: Button = Button(),
        val tag: Tag = Tag(),
        val skeleton: Color = Colors.Gallery,
    ) {
        public data class General(
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
            public data class Generic(val default: Color, val disabled: Color, val muted: Color)
        }

        public data class Accent(
            val pink: Color = Colors.Chardon,
        )

        public data class Button(
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
            public data class Generic(val default: Color, val active: Color, val disabled: Color)
        }

        public data class Tag(
            val default: Color = Colors.WildSand,
            val active: Color = Colors.MineShaftDark,
        )
    }

    public data class Text(
        val general: General = General(),
        val button: Button = Button(),
        val label: Label = Label(),
        val tag: Tag = Tag(),
    ) {
        public data class General(
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
            public data class Generic(val default: Color, val disabled: Color, val muted: Color)

            public data class Accent(
                val blue: Color = Colors.CuriousBlue,
                val red: Color = Colors.Scarlet,
                val redDisabled: Color = Colors.VividTangerine,
            )
        }

        public data class Button(
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
            public data class Generic(val default: Color, val disabled: Color)
        }

        public data class Label(
            val default: Color = Colors.MineShaftDark,
            val success: Color = Colors.FunGreen,
            val warning: Color = Colors.Flamenco,
            val danger: Color = Colors.Scarlet,
        )

        public data class Tag(
            val default: Color = Colors.MineShaftDark,
            val active: Color = Colors.White,
        )
    }

    public data class Icon(
        val regular: Regular = Regular(),
        val inversed: Inversed = Inversed(),
    ) {
        public data class Regular(
            val default: Color = Colors.MineShaftDark,
            val disabled: Color = Colors.SilverChalice,
            val muted: Color = Colors.Boulder,
            val error: Color = Colors.Scarlet,
            val errorDisabled: Color = Colors.VividTangerine,
        )

        public data class Inversed(
            val default: Color = Colors.White,
            val disabled: Color = Colors.Gray,
        )
    }

    public data class Border(
        val general: General = General(),
        val button: Button = Button(),
    ) {
        public data class General(
            val default: Color = Colors.Gallery,
            val active: Color = Colors.MineShaftDark,
            val disabled: Color = Colors.SilverChalice,
            val error: Color = Colors.Scarlet,
            val errorDisabled: Color = Colors.VividTangerine,
        )

        public data class Button(
            val default: Color = Colors.MineShaftDark,
            val disabled: Color = Colors.SilverChalice,
            val errorDisabled: Color = Colors.VividTangerine,
        )
    }
}
