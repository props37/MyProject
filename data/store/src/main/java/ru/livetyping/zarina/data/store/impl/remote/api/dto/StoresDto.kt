package ru.livetyping.zarina.data.store.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.domain.model.store.Store as StoreDomain

@Serializable
internal data class StoresDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("cities")
    val cities: List<City>? = null,
) {
    @Serializable
    data class City(
        @SerialName("id")
        val id: Long? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("kladr_id")
        val kladrId: String? = null,

        @SerialName("shops")
        val shops: List<Store>? = null,
    ) {
        fun getStores(country: String): List<StoreDomain> {
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
        data class Store(
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
            fun toStore(cityKladrId: KladrId?, cityName: String, country: String): StoreDomain {
                checkPropertyNotNull(id) { ::id }
                checkPropertyNotNull(name) { ::name }
                checkPropertyNotNull(address) { ::address }
                checkPropertyNotNull(lat) { ::lat }
                checkPropertyNotNull(lon) { ::lat }
                val city = cityKladrId?.let { StoreDomain.City(cityKladrId, cityName) }
                return StoreDomain(
                    id = StoreDomain.Id(id),
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
