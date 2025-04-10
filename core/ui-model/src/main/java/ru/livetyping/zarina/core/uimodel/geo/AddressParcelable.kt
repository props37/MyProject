package ru.livetyping.zarina.core.uimodel.geo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.geo.Address

@Serializable
@Parcelize
public data class AddressParcelable(
    val city: CityParcelable,
    val street: StreetParcelable,
    val building: BuildingParcelable,
    val apartment: String?,
) : Parcelable {
    public fun toAddress(): Address {
        return Address(
            city = city.toCity(),
            street = street.toStreet(),
            building = building.toBuilding(),
            apartment = apartment,
        )
    }

    public companion object {
        public fun from(address: Address): AddressParcelable {
            return AddressParcelable(
                city = CityParcelable.from(address.city),
                street = StreetParcelable.from(address.street),
                building = BuildingParcelable.from(address.building),
                apartment = address.apartment,
            )
        }
    }
}
