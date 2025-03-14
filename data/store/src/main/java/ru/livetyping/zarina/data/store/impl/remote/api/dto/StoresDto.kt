package ru.livetyping.zarina.data.store.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

@Serializable
internal data class StoresDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("cities")
    val cities: List<CityDto>? = null,
) {
    @Serializable
    data class CityDto(
        @SerialName("id")
        val id: Long? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("kladr_id")
        val kladrId: String? = null,

        @SerialName("shops")
        val shops: List<StoreDto>? = null,
    ) {
        fun getStores(country: String): List<Store> {
            checkPropertyNotNull(shops) { ::shops }
            return shops.map { store ->
                checkPropertyNotNull(name) { ::name }
                store.toStore(
                    cityKladrId = kladrId?.let { KladrId(it) },
                    cityName = name,
                    country = country,
                )
            }
        }

        @Serializable
        data class StoreDto(
            @SerialName("id")
            val id: String? = null,

            @SerialName("name")
            val name: String? = null,

            @SerialName("address")
            val address: String? = null,

            @SerialName("phone")
            val phone: String? = null,

            @SerialName("schedule")
            val schedule: String? = null,

            @SerialName("lat")
            val lat: Double? = null,

            @SerialName("lon")
            val lon: Double? = null,
        ) {
            fun toStore(cityKladrId: KladrId?, cityName: String, country: String): Store {
                checkPropertyNotNull(id) { ::id }
                checkPropertyNotNull(name) { ::name }
                checkPropertyNotNull(address) { ::address }
                checkPropertyNotNull(lat) { ::lat }
                checkPropertyNotNull(lon) { ::lat }
                val city = cityKladrId?.let { Store.City(cityKladrId, cityName) }
                return Store(
                    id = Store.Id(id),
                    name = name,
                    address = address,
                    phone = phone?.let { PhoneNumber.create(it) },
                    schedule = schedule,
                    location = Location(lat, lon),
                    country = country,
                    city = city,
                )
            }
        }
    }
}
