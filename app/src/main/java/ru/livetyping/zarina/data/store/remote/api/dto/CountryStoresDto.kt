package ru.livetyping.zarina.data.store.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.location.Location
import ru.livetyping.zarina.domain.store.Store as DomainStore

@Serializable
data class CountryStoresDto(
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
        val stores: List<Store>? = null,
    ) {
        fun getStores(country: String): List<DomainStore> {
            checkNotNull(stores) { "stores is null" }
            return stores.map {
                checkNotNull(name) { "name is null" }
                it.toStore(
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
            val latitude: Double? = null,

            @SerialName("lon")
            val longitude: Double? = null,
        ) {
            fun toStore(cityKladrId: KladrId?, cityName: String, country: String): DomainStore {
                checkNotNull(id) { "id is null" }
                checkNotNull(name) { "name is null" }
                checkNotNull(address) { "address is null" }
                checkNotNull(latitude) { "latitude is null" }
                checkNotNull(longitude) { "longitude is null" }
                return DomainStore(
                    id = DomainStore.Id(id),
                    name = name,
                    address = address,
                    phone = phone?.let { PhoneNumber.create(it) },
                    schedule = schedule,
                    location = Location(latitude, longitude),
                    cityKladrId = cityKladrId,
                    cityName = cityName,
                    country = country,
                )
            }
        }
    }
}
