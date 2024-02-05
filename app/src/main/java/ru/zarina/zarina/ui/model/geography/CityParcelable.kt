package ru.zarina.zarina.ui.model.geography

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.domain.rework.geography.KladrId

@Serializable
@Parcelize
data class CityParcelable(
    val name: String,
    val fullName: String,
    val region: String,
    val kladrId: String,
) : Parcelable {
    fun toCity(): City = City(
        name = name,
        fullName = fullName,
        region = region,
        kladrId = KladrId(kladrId),
    )

    companion object {
        fun from(city: City): CityParcelable = CityParcelable(
            name = city.name,
            fullName = city.fullName,
            region = city.region,
            kladrId = city.kladrId.value,
        )
    }
}
