package ru.livetyping.zarina.presentation.model.store

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.presentation.model.location.LocationParcelable

@Serializable
@Parcelize
class StoreParcelable(
    val id: String,
    val name: String,
    val address: String,
    val phone: String?,
    val schedule: String?,
    val location: LocationParcelable,
    val country: String?,
    val cityKladrId: String?,
    val cityName: String?,
) : Parcelable {
    fun toStore(): Store {
        return Store(
            id = Store.Id(id),
            name = name,
            address = address,
            phone = phone?.let { PhoneNumber.create(it) },
            schedule = schedule,
            location = location.toLocation(),
            country = country,
            cityKladrId = cityKladrId?.let { KladrId(it) },
            cityName = cityName,
        )
    }

    companion object {
        fun from(store: Store): StoreParcelable {
            return StoreParcelable(
                id = store.id.value,
                name = store.name,
                address = store.address,
                phone = store.phone?.value,
                schedule = store.schedule,
                location = LocationParcelable.from(store.location),
                country = store.country,
                cityKladrId = store.cityKladrId?.value,
                cityName = store.cityName,
            )
        }
    }
}
