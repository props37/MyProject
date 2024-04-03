package ru.livetyping.zarina.usecase.old.search

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.search.ISearchRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.SearchAutocomplete

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
