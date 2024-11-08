package ru.livetyping.zarina.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
@Parcelize
data class LocalDateParcelable(
    val string: String,
) : Parcelable {
    fun toLocalDate(): LocalDate {
        return LocalDate.parse(string)
    }

    companion object {
        fun from(localDate: LocalDate): LocalDateParcelable {
            val string = localDate.toString()
            return LocalDateParcelable(string)
        }
    }
}
