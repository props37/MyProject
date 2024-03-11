package ru.zarina.zarina.domain.old

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Size(
    val id: String,
    val name: String,
) : Parcelable
