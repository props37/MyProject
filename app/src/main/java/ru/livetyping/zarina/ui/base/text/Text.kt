package ru.livetyping.zarina.ui.base.text

import android.content.Context
import android.os.Parcelable
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * Returns String value of the given [Text].
 */
@Composable
fun textString(text: Text): String {
    // Will be recomposed when Configuration gets updated.
    LocalConfiguration.current
    val context = LocalContext.current
    return text.getString(context)
}

/**
 * Abstraction that allows to present a text in different forms.
 */
@Stable
sealed interface Text : Parcelable {
    /**
     * Returns a [String] value of the text.
     */
    fun getString(context: Context): kotlin.String

    /**
     * Represents an empty text with a value of empty [String].
     */
    @Parcelize
    @Serializable
    data object Empty : Text {
        override fun getString(context: Context): kotlin.String = ""
    }

    /**
     * Represents a resource text.
     *
     * @property resourceId id of the resource string.
     * @property args parameters for the parametrized string.
     */
    @Parcelize
    @Serializable
    class Resource(
        @StringRes
        val resourceId: Int,
        private vararg val args: @RawValue @Contextual Any = emptyArray(),
    ) : Text {
        override fun getString(context: Context): kotlin.String {
            @Suppress("SpreadOperator")
            return context.resources.getString(this.resourceId, *this.args)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Resource

            if (resourceId != other.resourceId) return false
            return args.contentEquals(other.args)
        }

        override fun hashCode(): Int {
            var result = resourceId
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    /**
     * Represents a plurals resource text.
     *
     * @property resourceId id of the plurals resource string.
     * @property count the number used to get the correct string for the plural rules.
     * @property args parameters for the parametrized string.
     */
    @Parcelize
    @Serializable
    class PluralsResource(
        @PluralsRes
        val resourceId: Int,
        private val count: Int,
        private vararg val args: @RawValue @Contextual Any = emptyArray(),
    ) : Text {
        override fun getString(context: Context): kotlin.String {
            @Suppress("SpreadOperator")
            return context.resources.getQuantityString(this.resourceId, this.count, *this.args)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as PluralsResource

            if (resourceId != other.resourceId) return false
            if (count != other.count) return false
            return args.contentEquals(other.args)
        }

        override fun hashCode(): Int {
            var result = resourceId
            result = 31 * result + count
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    /**
     * Represents the plain [String] text.
     */
    @Parcelize
    @Serializable
    data class String(private val text: kotlin.String) : Text {
        override fun getString(context: Context): kotlin.String = text
    }
}
