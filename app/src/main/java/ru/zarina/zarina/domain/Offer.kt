package ru.zarina.zarina.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Offer(
    val id: String,
    val isAvailable: Boolean,
    val barcode: String,
    val size: Size,
) : Parcelable
