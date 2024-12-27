package ru.livetyping.zarina.core.domain.model.store

import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.geo.City as DomainCity

// Marked as stable on config/compose/stability_config.txt
public data class Store(
    val id: Id,
    val name: String,
    val address: String,
    val phone: PhoneNumber?,
    val schedule: String?,
    val location: Location,
    val country: String?,
    val city: City?,
) {
    public fun getCity(): DomainCity? {
        return city?.let { city ->
            DomainCity(
                id = city.kladrId,
                name = city.name,
                fullName = null,
                region = null,
            )
        }
    }

    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Id(public val value: String)

    // Marked as stable on config/compose/stability_config.txt
    public data class City(
        val kladrId: KladrId,
        val name: String,
    )
}
