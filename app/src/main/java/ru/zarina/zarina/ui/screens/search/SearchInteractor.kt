package ru.zarina.zarina.ui.screens.search

import org.koin.core.annotation.Factory
import ru.zarina.zarina.usecase.favorites.GetFavoriteIdsUseCase
import ru.zarina.zarina.usecase.search.GetAutocompleteUseCase
import ru.zarina.zarina.usecase.search.GetSearchPageUseCase

@Factory
class SearchInteractor(
    private val getAutocompleteUseCase: GetAutocompleteUseCase,
    val getSearchPageUseCase: GetSearchPageUseCase,
    val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
) {

    suspend fun getAutocomplete(query: String) =
        getAutocompleteUseCase(GetAutocompleteUseCase.Parameters(query))

}
