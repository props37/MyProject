package ru.zarina.zarina.data.search.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.search.remote.api.IAnyQuerySearchApi
import ru.zarina.zarina.data.search.remote.api.dto.SortDto
import ru.zarina.zarina.domain.FilteredProducts
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.ProductSort

@Factory
class AnyQuerySearchRemoteSource(
    private val api: IAnyQuerySearchApi,
) : ISearchRemoteSource {

    override suspend fun getAutocomplete(query: String) = api.getAutocomplete(query).toDomain()

    override suspend fun getSearchPage(
        query: String,
        sort: ProductSort,
        filtration: Filtration?,
        pageIndex: Int,
    ): Page<FilteredProducts> {
        val response = api.getSearchResults(
            query = query,
            offset = pageIndex * PAGE_SIZE,
            sort = SortDto.from(sort),
            filters = filtration?.toDto().orEmpty(),
        )
        return response.toDomain(pageIndex = pageIndex, pageSize = PAGE_SIZE)
    }

    private fun Filtration.toDto(): List<String> {
        return buildList {
            if (price != null)
                add("price:${price.min};${price.max}")
            // TODO categories, sizes and colors
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }

}
