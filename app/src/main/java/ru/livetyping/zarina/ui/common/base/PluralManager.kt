package ru.livetyping.zarina.ui.common.base

import androidx.annotation.StringRes
import ru.livetyping.zarina.ui.base.text.Text

/* The plural string management is done through this class as a workaround to Android not allowing
 to specify the locale (and, in turn, the plural rules) of the default string resources. It uses
 the rules of the intended default language */
class PluralManager(private val resources: PluralResources) {

    fun getText(
        count: Int,
        vararg args: Any,
    ): Text.Resource {
        val resource = when {
            (count % 10 == 1) && (count % 100 != 11) -> resources.one
            (count % 10 in 2..4) && (count % 100 !in 12..14) -> resources.few
            (count % 10 == 0) || (count % 10 in 5..9) || (count % 100 in 11..14) -> resources.many
            else -> resources.other
        }
        return Text.Resource(resource, *args)
    }

}

data class PluralResources(
    @StringRes
    val other: Int,

    @StringRes
    val zero: Int = other,

    @StringRes
    val one: Int = other,

    @StringRes
    val two: Int = other,

    @StringRes
    val few: Int = other,

    @StringRes
    val many: Int = other,
)
