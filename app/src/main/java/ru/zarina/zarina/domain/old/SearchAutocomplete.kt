package ru.zarina.zarina.domain.old

import kotlinx.collections.immutable.ImmutableList

data class SearchAutocomplete(
    val words: ImmutableList<AutocompleteWord>,
    val frequentQueries: ImmutableList<String>,
)
