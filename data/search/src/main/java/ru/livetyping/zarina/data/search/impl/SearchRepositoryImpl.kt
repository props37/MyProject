package ru.livetyping.zarina.data.search.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions
import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.data.search.impl.remote.SearchRemoteDataSource
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: SearchRemoteDataSource,
) : SearchRepository {
    override fun getSearchSuggestionsFlow(query: String): Flow<SearchSuggestions> {
        return remoteDataSource.getSearchSuggestionsFlow(query)
    }
}
