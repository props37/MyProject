package ru.livetyping.zarina.core.uimodel.geo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.geo.FiasId
import ru.livetyping.zarina.core.domain.model.geo.Street

@Serializable
@Parcelize
public data class StreetParcelable(
    val id: String,
    val name: String,
) : Parcelable {
    public fun toStreet(): Street {
        return Street(
            id = FiasId(id),
            name = name,
        )
    }

    public companion object {
        public fun from(street: Street): StreetParcelable {
            return StreetParcelable(
                id = street.id.value,
                name = street.name,
            )
        }
    }
}
