package ru.zarina.zarina.data.search.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.AutocompleteWord

@Serializable
data class TapDto(
    @SerialName("tap")
    val tap: String? = null,
    @SerialName("relatedSearch")
    val relatedSearch: String? = null,
) {

    fun toDomain(): AutocompleteWord? {
        return if (
            ApiContract.isNotNull(tap, "tap")
            && ApiContract.isNotNull(relatedSearch, "relatedSearch")
        )
            AutocompleteWord(tap, relatedSearch)
        else
            null
    }

}
