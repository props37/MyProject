package ru.livetyping.zarina.data.old.search.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.old.ApiContract
import ru.livetyping.zarina.utils.kotlin.capitalize

@Serializable
data class StsDto(
    @SerialName("st")
    val st: String? = null,
) {

    fun toDomain(): String? {
        return if (ApiContract.isNotNull(st, "st"))
            st.capitalize()
        else
            null
    }

}
