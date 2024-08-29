package ru.livetyping.zarina.data.geography.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.geography.Street

@Serializable
data class StreetDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("text")
    val name: String? = null,
) {
    fun toStreet(): Street {
        checkNotNull(id) { "id is null" }
        checkNotNull(name) { "name is null" }
        return Street(
            id = KladrId(id),
            name = name,
        )
    }
}
