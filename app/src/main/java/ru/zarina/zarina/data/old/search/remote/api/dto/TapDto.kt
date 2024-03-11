package ru.zarina.zarina.data.old.search.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.old.ApiContract
import ru.zarina.zarina.domain.old.AutocompleteWord
import ru.zarina.zarina.utils.kotlin.capitalize

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
            AutocompleteWord(tap.capitalize(), relatedSearch.capitalize())
        else
            null
    }

}
