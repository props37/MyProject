package ru.livetyping.zarina.data.old.search.remote.api.dto

import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.old.SearchAutocomplete

@Serializable
data class SearchAutocompleteDto(
    @SerialName("taps")
    val taps: List<TapDto>? = null,
    @SerialName("sts")
    val sts: List<StsDto>? = null
) {

    fun toDomain(): SearchAutocomplete {
        return SearchAutocomplete(
            words = taps?.mapNotNull { it.toDomain() }.orEmpty().toImmutableList(),
            frequentQueries = sts?.mapNotNull { it.toDomain() }.orEmpty().toImmutableList()
        )
    }

}
