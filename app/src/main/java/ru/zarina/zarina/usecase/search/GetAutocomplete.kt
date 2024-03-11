package ru.zarina.zarina.usecase.search

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.old.search.ISearchRepository
import ru.zarina.zarina.di.old.Qualifiers
import ru.zarina.zarina.domain.SearchAutocomplete
import ru.zarina.zarina.base.usecase.UseCase

@Factory
class GetAutocompleteUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val searchRepository: ISearchRepository,
) : UseCase<GetAutocompleteUseCase.Parameters, SearchAutocomplete>(dispatcher) {

    override suspend fun execute(params: Parameters): SearchAutocomplete {
        val (query) = params

        return searchRepository.getAutocomplete(query)
    }

    data class Parameters(
        val query: String,
    )

}
