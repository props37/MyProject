package ru.livetyping.zarina.core.uimodel.store

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.uimodel.common.LocationParcelable

@Serializable
@Parcelize
public data class StoreParcelable(
    val id: String,
    val name: String,
    val address: String,
    val phone: String?,
    val schedule: String?,
    val location: LocationParcelable,
    val country: String?,
    val city: CityParcelable?,
) : Parcelable {
    public fun toStore(): Store {
        return Store(
            id = Store.Id(id),
            name = name,
            address = address,
            phone = phone?.let { PhoneNumber.create(it) },
            schedule = schedule,
            location = location.toLocation(),
            country = country,
            city = city?.let { city ->
                Store.City(
                    kladrId = KladrId(city.kladrId),
                    name = city.name,
                )
            },
        )
    }

    @Serializable
    @Parcelize
    public data class CityParcelable(
        val kladrId: String,
        val name: String,
    ) : Parcelable

    public companion object {
        public fun from(store: Store): StoreParcelable {
            return StoreParcelable(
                id = store.id.value,
                name = store.name,
                address = store.address,
                phone = store.phone?.value,
                schedule = store.schedule,
                location = LocationParcelable.from(store.location),
                country = store.country,
                city = store.city?.let { city ->
                    CityParcelable(
                        kladrId = city.kladrId.value,
                        name = city.name,
                    )
                },
            )
        }
    }
}
