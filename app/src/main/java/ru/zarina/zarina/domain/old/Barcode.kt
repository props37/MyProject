package ru.zarina.zarina.domain.old

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Deprecated("Use Barcode instead.")
@Parcelize
@JvmInline
value class Barcode(val value: String) : Parcelable
