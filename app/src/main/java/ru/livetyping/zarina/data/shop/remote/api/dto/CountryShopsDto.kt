package ru.livetyping.zarina.data.shop.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.location.Location
import ru.livetyping.zarina.domain.shop.Shop as DomainShop

@Serializable
data class CountryShopsDto(
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
        val shops: List<Shop>? = null,
    ) {
        fun getShops(country: String): List<DomainShop> {
            checkNotNull(shops) { "shops is null" }
            return shops.map {
                checkNotNull(name) { "name is null" }
                it.toShop(
                    cityKladrId = kladrId?.let { KladrId(it) },
                    cityName = name,
                    country = country,
                )
            }
        }

        @Serializable
        data class Shop(
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
            fun toShop(cityKladrId: KladrId?, cityName: String, country: String): DomainShop {
                checkNotNull(id) { "id is null" }
                checkNotNull(name) { "name is null" }
                checkNotNull(address) { "address is null" }
                checkNotNull(latitude) { "latitude is null" }
                checkNotNull(longitude) { "longitude is null" }
                return DomainShop(
                    id = DomainShop.Id(id),
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
