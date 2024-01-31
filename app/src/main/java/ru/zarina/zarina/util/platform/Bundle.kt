package ru.zarina.zarina.util.platform

import android.os.Bundle
import android.os.Parcel

fun Bundle.getSizeInBytes(): Int {
    val parcel = Parcel.obtain()
    parcel.writeValue(this)
    val bytes = parcel.marshall()
    parcel.recycle()
    return bytes.size
}
