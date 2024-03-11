package ru.zarina.zarina.domain.old

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Offer(
    val id: Id,
    val isAvailable: Boolean,
    val barcode: Barcode,
    val size: Size,
) : Parcelable {

    @Parcelize
    @JvmInline
    value class Id(val value: String) : Parcelable

}
