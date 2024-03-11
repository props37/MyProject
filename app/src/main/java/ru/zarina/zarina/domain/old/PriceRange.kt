package ru.zarina.zarina.domain.old

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceRange(val min: Int, val max: Int) : Parcelable
