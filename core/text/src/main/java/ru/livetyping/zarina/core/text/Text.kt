package ru.livetyping.zarina.core.text

import android.content.Context
import android.os.Parcelable
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// Marked as stable on config/compose/stability_config.txt
@Parcelize
@Serializable
public sealed interface Text : Parcelable {
    public fun getString(context: Context): kotlin.String

    // Marked as stable on config/compose/stability_config.txt
    @Parcelize
    @Serializable
    public data object Empty : Text {
        override fun getString(context: Context): kotlin.String = ""

        override fun toString(): kotlin.String = ""
    }

    // Marked as stable on config/compose/stability_config.txt
    @Parcelize
    @Serializable
    public class Resource(
        @StringRes
        private val resId: Int,
        private vararg val args: @RawValue @Contextual Any = emptyArray(),
    ) : Text {
        override fun getString(context: Context): kotlin.String {
            return context.resources.getString(resId, *args)
        }

        override fun toString(): kotlin.String {
            return "Resource(resId=$resId, args=${args.contentToString()})"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Resource

            if (resId != other.resId) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = resId
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    // Marked as stable on config/compose/stability_config.txt
    @Parcelize
    @Serializable
    public class PluralResource(
        @PluralsRes
        private val resId: Int,
        private val count: Int,
        private vararg val args: @RawValue @Contextual Any = emptyArray()
    ) : Text {
        override fun getString(context: Context): kotlin.String {
            return context.resources.getQuantityString(resId, count, *args)
        }

        override fun toString(): kotlin.String {
            return "PluralResource(resId=$resId, count=$count, args=${args.contentToString()})"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as PluralResource

            if (resId != other.resId) return false
            if (count != other.count) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = resId
            result = 31 * result + count
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    // Marked as stable on config/compose/stability_config.txt
    @Parcelize
    @Serializable
    public data class String(val text: kotlin.String) : Text {
        override fun getString(context: Context): kotlin.String = text
    }
}
