package ru.livetyping.zarina.domain.old

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Color(
    val id: String,
    val name: String,
    val code: Code,
) : Parcelable {

    @Parcelize
    @JvmInline
    value class Code(val value: String) : Parcelable

}
