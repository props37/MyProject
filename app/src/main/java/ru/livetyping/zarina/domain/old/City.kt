package ru.livetyping.zarina.domain.old

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class City(
    val id: AddressId,
    val name: String,
    val region: String?,
) : Parcelable, Serializable {

    val priority: Int?
        get() = when (id) {
            AddressId.SAINT_PETERSBURG -> 0
            AddressId.MOSCOW -> 1
            else -> null
        }

    companion object {
        val DEFAULT
            get() = City(
                id = AddressId.SAINT_PETERSBURG,
                name = "Санкт-Петербург",
                region = "г. Санкт-Петербург",
            )
    }

}
