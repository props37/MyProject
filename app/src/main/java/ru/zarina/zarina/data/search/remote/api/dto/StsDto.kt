package ru.zarina.zarina.data.search.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract

@Serializable
data class StsDto(
    @SerialName("st")
    val st: String? = null,
) {

    fun toDomain(): String? {
        return if (ApiContract.isNotNull(st, "st"))
            st
        else
            null
    }

}
