package ru.zarina.zarina.ui.screens.search

import org.koin.core.annotation.Factory
import ru.zarina.zarina.usecase.search.GetAutocompleteUseCase

@Factory
class SearchInteractor(
    private val getAutocompleteUseCase: GetAutocompleteUseCase
) {

    suspend fun getAutocomplete(query: String) =
        getAutocompleteUseCase(GetAutocompleteUseCase.Parameters(query))

}
