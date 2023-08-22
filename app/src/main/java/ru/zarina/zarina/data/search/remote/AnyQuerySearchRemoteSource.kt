package ru.zarina.zarina.data.search.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.search.remote.api.IAnyQuerySearchApi
import ru.zarina.zarina.data.search.remote.api.dto.SortDto
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort

@Factory
class AnyQuerySearchRemoteSource(
    private val api: IAnyQuerySearchApi,
) : ISearchRemoteSource {

    override suspend fun getAutocomplete(query: String) = api.getAutocomplete(query).toDomain()

    override suspend fun getSearchPage(
        query: String,
        sort: ProductSort,
        pageIndex: Int
    ): Page<List<Product>> {
        val response = api.getSearchResults(
            query = query,
            offset = pageIndex * PAGE_SIZE,
            sort = SortDto.from(sort)
        )
        return response.toDomain(pageIndex = pageIndex, pageSize = PAGE_SIZE)
    }

    companion object {
        private const val PAGE_SIZE = 10
    }

}
