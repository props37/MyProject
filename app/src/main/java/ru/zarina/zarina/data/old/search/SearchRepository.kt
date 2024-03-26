package ru.zarina.zarina.data.old.search

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.search.local.ISearchLocalSource
import ru.zarina.zarina.data.old.search.remote.ISearchRemoteSource
import ru.zarina.zarina.domain.old.Filtration
import ru.zarina.zarina.domain.old.ProductSort

@Factory
class SearchRepository(
    private val local: ISearchLocalSource,
    private val remote: ISearchRemoteSource,
) : ISearchRepository {

    override suspend fun getAutocomplete(query: String) = remote.getAutocomplete(query)

    override suspend fun getSearchPage(
        query: String,
        sort: ProductSort,
        filtration: Filtration?,
        pageIndex: Int,
    ) =
        remote.getSearchPage(query, sort, filtration, pageIndex)

    override fun getHistory(limit: Int) = local.getHistory(limit)

    override suspend fun addToHistory(query: String) = local.addToHistory(query)

    override suspend fun removeFromHistory(query: String) = local.removeFromHistory(query)

}
