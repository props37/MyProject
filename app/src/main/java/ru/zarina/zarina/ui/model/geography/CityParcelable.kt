package ru.zarina.zarina.ui.model.geography

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.zarina.zarina.domain.rework.geography.AddressId
import ru.zarina.zarina.domain.rework.geography.City

@Parcelize
data class CityParcelable(
    val addressId: String,
    val name: String,
) : Parcelable {
    fun toCity(): City = City(
        addressId = AddressId(addressId),
        name = name,
    )

    companion object {
        fun fromCity(city: City): CityParcelable = CityParcelable(
            addressId = city.addressId.value,
            name = city.name,
        )
    }
}
