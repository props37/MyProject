package ru.zarina.zarina.domain

import kotlinx.collections.immutable.ImmutableList

data class SearchAutocomplete(
    val words: ImmutableList<AutocompleteWord>,
)
