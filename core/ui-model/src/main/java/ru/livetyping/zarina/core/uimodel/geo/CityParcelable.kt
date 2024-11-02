package ru.livetyping.zarina.core.uimodel.geo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.geo.KladrId

@Serializable
@Parcelize
public data class CityParcelable(
    val id: String,
    val name: String,
    val fullName: String?,
    val region: String?,
) : Parcelable {
    public fun toCity(): City {
        return City(
            id = KladrId(id),
            name = name,
            fullName = fullName,
            region = region,
        )
    }

    public companion object {
        public fun from(city: City): CityParcelable {
            return CityParcelable(
                name = city.name,
                id = city.id.value,
                fullName = city.fullName,
                region = city.region,
            )
        }
    }
}