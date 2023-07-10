package ru.zarina.zarina.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * Address id according to "КЛАДР РФ"
 */
@Parcelize
@JvmInline
value class AddressId(val id: String) : Parcelable, Serializable {

    companion object {
        val SAINT_PETERSBURG = AddressId("7800000000000")
        val MOSCOW = AddressId("7700000000000")
    }

}
