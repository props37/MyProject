package ru.livetyping.zarina.presentation.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.CheckoutAddress
import ru.livetyping.zarina.presentation.model.geography.BuildingParcelable
import ru.livetyping.zarina.presentation.model.geography.CityParcelable
import ru.livetyping.zarina.presentation.model.geography.StreetParcelable

@Serializable
@Parcelize
data class CheckoutAddressParcelable(
    val city: CityParcelable,
    val street: StreetParcelable,
    val building: BuildingParcelable,
    val apartment: String?,
) : Parcelable {
    fun toCheckoutAddress(): CheckoutAddress {
        return CheckoutAddress(
            city = city.toCity(),
            street = street.toStreet(),
            building = building.toBuilding(),
            apartment = apartment,
        )
    }

    companion object {
        fun from(address: CheckoutAddress): CheckoutAddressParcelable {
            return CheckoutAddressParcelable(
                city = CityParcelable.from(address.city),
                street = StreetParcelable.from(address.street),
                building = BuildingParcelable.from(address.building),
                apartment = address.apartment,
            )
        }
    }
}
