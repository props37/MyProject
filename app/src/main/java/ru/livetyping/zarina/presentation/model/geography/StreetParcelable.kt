package ru.livetyping.zarina.presentation.model.geography

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.geography.Street

@Serializable
@Parcelize
data class StreetParcelable(
    val id: String,
    val name: String,
) : Parcelable {
    fun toStreet(): Street {
        return Street(
            id = KladrId(id),
            name = name,
        )
    }

    companion object {
        fun from(street: Street): StreetParcelable {
            return StreetParcelable(
                id = street.id.value,
                name = street.name,
            )
        }
    }
}
