package ru.livetyping.zarina.presentation.model.geography

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.geography.KladrId

@Serializable
@Parcelize
data class CityParcelable(
    val name: String,
    val id: String,
    val fullName: String,
    val region: String,
) : Parcelable {
    fun toCity(): City = City(
        name = name,
        id = KladrId(id),
        fullName = fullName,
        region = region,
    )

    companion object {
        fun from(city: City): CityParcelable = CityParcelable(
            name = city.name,
            id = city.id.value,
            fullName = city.fullName,
            region = city.region,
        )
    }
}
